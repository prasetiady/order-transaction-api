package	com.prasetiady.repo

import v1.yaml._
import javax.inject.Inject
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile

/**
 * CustomersTable
 */
private[repo] trait CustomersTable { self : HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  private[CustomersTable] class CustomersTable(tag: Tag) extends Table[Customer](tag, "Customers") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val name = column[String]("name")
    def * = (id, name) <> (Customer.tupled , Customer.unapply)
  }

  protected val customersTableQuery = TableQuery[CustomersTable]
}

/**
 * OrdersTable
 */
private[repo] trait OrdersTable { self : HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  private[OrdersTable] class OrdersTable(tag: Tag) extends Table[Order](tag, "Orders") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val customerId = column[Int]("customerId")
    val status = column[String]("status")
    val isSubmitted = column[Boolean]("isSubmitted")
    val isPaid = column[Boolean]("isPaid")
    def * = (isPaid, isSubmitted, customerId, id, status) <> (Order.tupled , Order.unapply)
  }

  protected val ordersTableQuery = TableQuery[OrdersTable]
}
