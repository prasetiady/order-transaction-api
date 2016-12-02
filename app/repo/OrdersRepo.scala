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
class OrdersRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends OrdersTable with LineItemsTable with ProductsTable with CouponsTable with HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  /**
   * @param orderId
   * @param productId
   * @return
   * Add a product to an order
   */
  def addProduct(orderId: Int, productId: Int): Future[Int] = Future {
    orderMustBeExists(orderId)
    val getProductResult: Option[Product] = Await.result(getProductById(productId), Duration.Inf)
    getProductResult match {
      case None => throw new Exception("Not found product with id: " + productId)
      case Some(product) => {
        productQuantityMustBeGreaterThanZero(product)
        val getLineItemResult: Option[LineItem] = Await.result(getLineItem(orderId, productId), Duration.Inf)
        getLineItemResult match {
          case Some(lineItem) => {
            productQuantityMustBeGreaterThanLineItemQuantity(product,lineItem)
            increaseLineItemQuantityByOne(lineItem)
            lineItem.id
          }
          case None => Await.result(createNewLineItem(orderId, product), Duration.Inf)
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
  def applyCoupon(orderId: Int, couponCode: String): Future[Int] = Future {
    val order = orderMustBeExists(orderId)
    orderCanOnlyHaveOneCoupon(order)
    val getCouponResult: Option[Coupon] = Await.result(findActiveCoupon(couponCode), Duration.Inf)
    getCouponResult match {
      case Some(coupon) => {
        couponQuantityMustBeGreaterThanZero(coupon)
        Await.result(applyCoupon(orderId, coupon.id), Duration.Inf)
        coupon.id
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
  def submitOrder(orderId: Int, shippingAddress: ShippingAddress): Future[Int] = Future {
    1
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
   * Rule: Order must be exists
   */
  private[OrdersRepo] def orderMustBeExists(orderId: Int): Order = {
    val orderOption: Option[Order] = Await.result(db.run(ordersTableQuery.filter(_.id === orderId).result.headOption), Duration.Inf)
    orderOption match {
      case Some(order) => order
      case None => throw new Exception("Not found order with id: " + orderId)
    }
  }

  /**
   * @param productId
   * Get product by productId
   */
  private[OrdersRepo] def getProductById(productId: Int): Future[Option[Product]] = db.run {
    productsTableQuery.filter(_.id === productId).result.headOption
  }

  /**
   * @param orderId
   * @param productId
   * Get LineItem
   */
  private[OrdersRepo] def getLineItem(orderId: Int, productId: Int): Future[Option[LineItem]] = db.run {
    lineItemsTableQuery.filter(i => i.orderId === orderId && i.productId === productId).result.headOption
  }

  /**
   * @param orderId
   * @param product
   * Create New LineItem
   */
  private[OrdersRepo] def createNewLineItem(orderId: Int, product: Product): Future[Int] = db.run {
    (lineItemsTableQuery.map(i => (i.orderId, i.productId, i.productName, i.productPrice, i.quantity))
      returning lineItemsTableQuery.map(_.id)) += (orderId, product.id, product.name, product.price, 1)
  }

  /**
   * @param lineItem
   * Increase LineItem quantity by one
   */
  private[OrdersRepo] def increaseLineItemQuantityByOne(lineItem: LineItem): Future[Int] = db.run {
    lineItemsTableQuery.filter(_.id === lineItem.id).map(_.quantity).update(lineItem.quantity + 1)
  }

  /**
   * @param orderId
   * @param couponId
   * Update Order couponId value
   */
  private[OrdersRepo] def applyCoupon(orderId: Int, couponId: Int): Future[Int] = db.run {
    ordersTableQuery.filter(_.id === orderId).map(_.couponId).update(couponId)
  }

  /**
   * @param couponCode
   * Find active coupon
   */
  private[OrdersRepo] def findActiveCoupon(couponCode: String): Future[Option[Coupon]] = db.run {
    val now: String = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    couponsTableQuery.filter(c => c.code === couponCode && c.validFrom <= now && c.validUntil >= now).result.headOption
  }
}
