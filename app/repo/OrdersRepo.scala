package	com.prasetiady.repo

import scala.concurrent.{Future,Await}
import v1.yaml._
import javax.inject.{Inject,Singleton}
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

@Singleton()
class OrdersRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends OrdersTable with LineItemsTable with ProductsTable with HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  /**
   * @param orderId
   * @param productId
   * @return
   * Add a product to an order
   */
  def addProductToOrder(orderId: Int, productId: Int): Future[Int] = Future {
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
   * Rule: Order must be exists
   */
  private[OrdersRepo] def orderMustBeExists(orderId: Int): Boolean =
    if (Await.result(db.run(ordersTableQuery.filter(_.id === orderId).exists.result), Duration.Inf))
      true else throw new Exception("Not found order with id: " + orderId)

  /**
   * @param productId
   * Get product by productId
   */
  private[OrdersRepo] def getProductById(productId: Int): Future[Option[Product]] = db.run {
    productsTableQuery.filter(_.id === productId).result.headOption
  }

  private[OrdersRepo] def getLineItem(orderId: Int, productId: Int): Future[Option[LineItem]] = db.run {
    lineItemsTableQuery.filter(i => i.orderId === orderId && i.productId === productId).result.headOption
  }

  private[OrdersRepo] def createNewLineItem(orderId: Int, product: Product): Future[Int] = db.run {
    (lineItemsTableQuery.map(i => (i.orderId, i.productId, i.productName, i.productPrice, i.quantity))
      returning lineItemsTableQuery.map(_.id)) += (orderId, product.id, product.name, product.price, 1)
  }

  private[OrdersRepo] def increaseLineItemQuantityByOne(lineItem: LineItem): Future[Int] = db.run {
    lineItemsTableQuery.filter(_.id === lineItem.id).map(_.quantity).update(lineItem.quantity + 1)
  }

}
