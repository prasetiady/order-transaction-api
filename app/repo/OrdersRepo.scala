package	com.prasetiady.repo

import scala.concurrent.{Future,Await}
import v1.yaml._
import javax.inject.{Inject,Singleton}
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import java.text.SimpleDateFormat;
import java.util.Date;

@Singleton()
class OrdersRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends OrdersTable with LineItemsTable with ProductsTable with CouponsTable with ShippingAddressesTable with PaymentProffsTable with HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  /**
   * @return
   * Get all orders
   */
  def getAllOrders(): Future[List[Order]] = db.run { Orders.to[List].result }

  /**
   * @param orderId
   * @return
   * Get order detail
   */
  def getOrderDetail(orderId: Int): Future[OrderDetail] = Future {
    val order = orderMustBeExists(orderId)
    val query: Future[(List[LineItem], Option[PaymentProff], Option[ShippingAddress])] =
    for {
      l <- db.run { LineItems.filter(_.orderId === orderId).to[List].result }
      p <- db.run { PaymentProffs.filter(_.orderId === orderId).result.headOption }
      s <- db.run { ShippingAdresses.filter(_.orderId === orderId).result.headOption }
    } yield (l, p, s)
    val result: (List[LineItem], Option[PaymentProff], Option[ShippingAddress]) = Await.result(query, Duration.Inf)
    OrderDetail(order, result._1, result._2, result._3)
  }

  /**
   * @param orderId
   * @param productId
   * @return
   * Add a product to an order
   */
  def addProduct(orderId: Int, productId: Int): Future[Unit] = Future {
    val order = orderMustBeExists(orderId)
    orderMustHasNotBeenSubmitted(order)
    val productOption: Option[Product] = Await.result(getProductById(productId), Duration.Inf)
    productOption match {
      case None => throw new Exception("Not found product with id: " + productId)
      case Some(product) => {
        productQuantityMustBeGreaterThanZero(product)
        val lineItemOption: Option[LineItem] = Await.result(getLineItem(orderId, productId), Duration.Inf)
        lineItemOption match {
          case Some(lineItem) => {
            productQuantityMustBeGreaterThanLineItemQuantity(product,lineItem)
            increaseLineItemQuantityByOne(lineItem)
          }
          case None => Await.ready(createNewLineItem(orderId, product), Duration.Inf)
        }
      }
    }
  }

  /**
   * @param orderId
   * @param couponCode
   * @return
   * Apply coupon to an order
   */
  def applyCoupon(orderId: Int, couponCode: String): Future[Unit] = Future {
    val order = orderMustBeExists(orderId)
    orderCanOnlyHaveOneCoupon(order)
    orderMustHasNotBeenSubmitted(order)
    val couponOption: Option[Coupon] = Await.result(findActiveCoupon(couponCode), Duration.Inf)
    couponOption match {
      case Some(coupon) => {
        couponQuantityMustBeGreaterThanZero(coupon)
        Await.ready(applyCoupon(orderId, coupon.id), Duration.Inf)
      }
      case None => throw new Exception("Not found active coupon with code: " + couponCode)
    }
  }

  /**
   * @param orderId
   * @param shippingAddress
   * @return
   * Submit order
   */
  def submitOrder(orderId: Int, shippingAddress: NewShippingAddress): Future[Unit] = Future {
    val order = orderMustBeExists(orderId)
    orderMustHasNotBeenSubmitted(order)
    val q1 = for {
      l <- LineItems.filter(_.orderId === orderId)
      p <- Products if l.productId === p.id
    } yield (p.id, p.quantity, l.quantity)
    val q2 = q1.result
    val items: Seq[(Int,Int,Int)] = Await.result(db.run(q2), Duration.Inf)
    shoppingCartMustNotEmpty(items.length)
    Await.ready( Future {
      items foreach (item =>
        if (item._2 < item._3) throw new Exception("Product with id " + item._1 + " not available")
      )
      if (order.couponId != 0) {
        val couponOption: Option[Coupon] = Await.result(getCoupon(order.couponId), Duration.Inf)
        couponOption match {
          case None => throw new Exception("Not found coupon with id " + order.couponId)
          case Some(coupon) => {
            couponQuantityMustBeGreaterThanZero(coupon)
            reduceCouponQuantityByOne(coupon)
          }
        }
      }
      reduceProductQuantity(items)
      markOrderAsSubmitted(order)
      addShippingAddress(orderId, shippingAddress)
    }, Duration.Inf)
  }

  /**
   * @param orderId
   * @param paymentProff
   * Submit Payment Proff
   */
  def submitPaymentProff(orderId: Int, paymentProff: NewPaymentProff): Future[Unit] = Future {
    val order = orderMustBeExists(orderId)
    Await.ready( Future {
      markOrderAsPaid(orderId)
      addPaymentProff(orderId, paymentProff)
    } , Duration.Inf)
  }

  /**
   * @param orderId
   * Verify Order
   */
  def verifyOrder(orderId: Int): Future[Unit] = Future {
    val order = orderMustBeExists(orderId)
    Await.ready( Future { markOrderAsVerified(orderId) } , Duration.Inf)
  }

  /**
   * @param orderId
   * Ship Order
   */
  def shipOrder(orderId: Int): Future[Unit] = Future {
    val order = orderMustBeExists(orderId)
    Await.ready( Future { markOrderAsShipped(orderId) } , Duration.Inf)
  }

  /**
   * @param orderId
   * Cancel Order
   */
  def cancelOrder(orderId: Int): Future[Unit] = Future {
    val order = orderMustBeExists(orderId)
    Await.ready( Future { markOrderAsCanceled(orderId) } , Duration.Inf)
  }

  /*
   * Rule: Product with quantity 0 can not be ordered
   */
  private[OrdersRepo] def productQuantityMustBeGreaterThanLineItemQuantity(product: Product, lineItem: LineItem): Boolean =
    if (product.quantity > lineItem.quantity) true else throw new Exception("Cannot add more product with id: " + product.id)

  /*
   * Rule: Product with quantity 0 can not be ordered
   */
  private[OrdersRepo] def productQuantityMustBeGreaterThanZero(product: Product): Boolean =
    if (product.quantity > 0) true else throw new Exception("Product with quantity 0 can not be ordered")

  /*
   * Rule: Coupon with quantity 0 can not be applied
   */
  private[OrdersRepo] def couponQuantityMustBeGreaterThanZero(coupon: Coupon): Boolean =
    if (coupon.quantity > 0) true else throw new Exception("Coupon with quantity 0 can not be applied")

  /*
   * Rule: Order can only have one coupon applied
   */
  private[OrdersRepo] def orderCanOnlyHaveOneCoupon(order: Order): Boolean =
    if (order.couponId == 0) true else throw new Exception("Order already have coupon applied")

  /*
   * Rule: Order Must Has not Been Submitted
   */
  private[OrdersRepo] def orderMustHasNotBeenSubmitted(order: Order): Boolean =
    if (!order.isSubmitted) true else throw new Exception("Order already submitted")

  /*
   * Rule: Order must be exists
   */
  private[OrdersRepo] def orderMustBeExists(orderId: Int): Order = {
    val orderOption: Option[Order] = Await.result(db.run(Orders.filter(_.id === orderId).result.headOption), Duration.Inf)
    orderOption match {
      case Some(order) => order
      case None => throw new Exception("Not found order with id: " + orderId)
    }
  }

  /*
   * Rule : Shopping cart must not empty
   */
  private[OrdersRepo] def shoppingCartMustNotEmpty(itemsCount: Int): Boolean =
    if (itemsCount > 0) true else throw new Exception("Shopping cart is empty")

  /**
   * @param productId
   * Get product by productId
   */
  private[OrdersRepo] def getProductById(productId: Int): Future[Option[Product]] = db.run {
    Products.filter(_.id === productId).result.headOption
  }

  /**
   * @param orderId
   * @param productId
   * Get LineItem
   */
  private[OrdersRepo] def getLineItem(orderId: Int, productId: Int): Future[Option[LineItem]] = db.run {
    LineItems.filter(i => i.orderId === orderId && i.productId === productId).result.headOption
  }

  /**
   * @param orderId
   * @param product
   * Create New LineItem
   */
  private[OrdersRepo] def createNewLineItem(orderId: Int, product: Product): Future[Int] = db.run {
    (LineItems.map(i => (i.orderId, i.productId, i.productName, i.productPrice, i.quantity))
      returning LineItems.map(_.id)) += (orderId, product.id, product.name, product.price, 1)
  }

  /**
   * @param lineItem
   * Increase LineItem quantity by one
   */
  private[OrdersRepo] def increaseLineItemQuantityByOne(lineItem: LineItem): Future[Int] = db.run {
    LineItems.filter(_.id === lineItem.id).map(_.quantity).update(lineItem.quantity + 1)
  }

  /**
   * @param lineItem
   * Reduce Coupon quantity by one
   */
  private[OrdersRepo] def reduceCouponQuantityByOne(coupon: Coupon): Future[Int] = db.run {
    Coupons.filter(_.id === coupon.id).map(_.quantity).update(coupon.quantity - 1)
  }

  /**
   * @param lineItem
   * Reduce Product quantity
   */
  private[OrdersRepo] def reduceProductQuantity(items: Seq[(Int, Int, Int)]): Future[Unit] = Future {
    items foreach ( item =>
      db.run { Products.filter(_.id === item._1).map(_.quantity).update(item._2 - item._3) }
    )
  }

  /**
   * @param order
   * Mark Order as Submitted
   */
  private[OrdersRepo] def markOrderAsSubmitted(order: Order): Future[Unit] = Future {
    db.run{ Orders.filter(_.id === order.id).map(o => (o.isSubmitted, o.status)).update(true,"HOLD") }
  }

  /**
   * @param order
   * Mark Order as Paid
   */
  private[OrdersRepo] def markOrderAsPaid(orderId: Int): Future[Unit] = Future {
    db.run{ Orders.filter(_.id === orderId).map(o => (o.isPaid)).update(true) }
  }

  /**
   * @param order
   * Mark Order as Verified
   */
  private[OrdersRepo] def markOrderAsVerified(orderId: Int): Future[Unit] = Future {
    db.run{ Orders.filter(_.id === orderId).map(o => (o.status)).update("VERIFIED") }
  }

  /**
   * @param order
   * Mark Order as Shipped
   */
  private[OrdersRepo] def markOrderAsShipped(orderId: Int): Future[Unit] = Future {
    db.run{ Orders.filter(_.id === orderId).map(o => (o.status)).update("SHIPPED") }
  }

  /**
   * @param order
   * Mark Order as Canceled
   */
  private[OrdersRepo] def markOrderAsCanceled(orderId: Int): Future[Unit] = Future {
    db.run{ Orders.filter(_.id === orderId).map(o => (o.status)).update("CANCELED") }
  }

  /**
   * @param orderId
   * @param couponId
   * Update Order couponId value
   */
  private[OrdersRepo] def applyCoupon(orderId: Int, couponId: Int): Future[Int] = db.run {
    Orders.filter(_.id === orderId).map(_.couponId).update(couponId)
  }

  /**
   * @param couponCode
   * Find active coupon
   */
  private[OrdersRepo] def findActiveCoupon(couponCode: String): Future[Option[Coupon]] = db.run {
    val now: String = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    Coupons.filter(c => c.code === couponCode && c.validFrom <= now && c.validUntil >= now).result.headOption
  }

  /**
   * @param couponId
   */
  private[OrdersRepo] def getCoupon(couponId: Int): Future[Option[Coupon]] = db.run {
    Coupons.filter(_.id === couponId).result.headOption
  }

  /**
   * @param orderId
   * @param shippingAddress
   * Add shipping address
   */
  private[OrdersRepo] def addShippingAddress(orderId: Int, shippingAddress: NewShippingAddress): Future[Int] = db.run {
    (ShippingAdresses.map(sa => (sa.orderId, sa.address, sa.name, sa.phoneNumber, sa.email)) returning ShippingAdresses.map(_.id)) +=
      (orderId, shippingAddress.address, shippingAddress.name, shippingAddress.phoneNumber, shippingAddress.email)
  }

  /**
   * @param orderId
   * @param paymentProff
   * Add Payment Proff
   */
  private[OrdersRepo] def addPaymentProff(orderId: Int, paymentProff: NewPaymentProff): Future[Int] = db.run {
    (PaymentProffs.map(pf => (pf.orderId, pf.amount, pf.note)) returning PaymentProffs.map(_.id)) +=
      (orderId, paymentProff.amount, paymentProff.note)
  }
}
