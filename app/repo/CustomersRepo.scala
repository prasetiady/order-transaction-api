package	com.prasetiady.repo

import scala.concurrent.{Future,Await}
import v1.yaml._
import javax.inject.{Inject,Singleton}
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

@Singleton()
class CustomersRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends CustomersTable with OrdersTable with HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  /**
   * @return
   * Get all customers
   */
  def getAllCustomers(): Future[List[Customer]] = db.run { customersTableQuery.to[List].result }

  /**
   * @param customerId
   * Get all order for given customerId
   */
  def getAllCustomerOrders(customerId: Int): Future[List[Order]] = db.run { ordersTableQuery.filter(_.customerId === customerId).to[List].result }

  /**
   * @param customerId
   * @return
   * Return shopping cart id based on customerId ( shopping cart is order with
   * isSubmitted value false) crete new order if customer exist but shopping cart not found
   */
  def getShoppingCart(customerId: Int): Future[Int] = Future {
    val checkIfCustomerExist: Boolean = Await.result(isCustomerExists(customerId), Duration.Inf)
    if(checkIfCustomerExist) {
      val orderIdOption: Option[Int] = Await.result(getActiveOrder(customerId), Duration.Inf)
      orderIdOption match {
        case Some(orderId) => orderId
        case None => Await.result(createNewOrder(customerId) , Duration.Inf)
      }
    } else {
      throw new Exception("Not found customer with id: " + customerId)
    }
  }

  /**
   * @param customerId
   * Check if customer with given id exists
   */
  private[CustomersRepo] def isCustomerExists(customerId: Int): Future[Boolean] = db.run {
    customersTableQuery.filter(_.id === customerId).exists.result
  }

  /**
   * @param customerId
   * Return order id with isSubmitted value false
   */
  private[CustomersRepo] def getActiveOrder(customerId: Int): Future[Option[Int]] = db.run {
    ordersTableQuery.filter(o =>
      o.customerId === customerId && o.isSubmitted === false
    ).map(_.id).result.headOption
  }

  /**
   * @param customerId
   * Return order id with isSubmitted value false
   */
  private[CustomersRepo] def createNewOrder(customerId: Int): Future[Int] = db.run {
    (ordersTableQuery.map(o => (o.customerId)) returning ordersTableQuery.map(_.id)) += (customerId)
  }

}
