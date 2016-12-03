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

  protected val Customers = TableQuery[CustomersTable]
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
    val couponId = column[Int]("couponId")
    def * = (isSubmitted, isPaid, customerId, id, status, couponId) <> (Order.tupled , Order.unapply)
  }

  protected val Orders = TableQuery[OrdersTable]
}

/**
 * ProductsTable
 */
private[repo] trait ProductsTable { self : HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  private[ProductsTable] class ProductsTable(tag: Tag) extends Table[Product](tag, "Products") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val name = column[String]("name")
    val price = column[Double]("price")
    val quantity = column[Int]("quantity")
    def * = (id, name, price, quantity) <> (Product.tupled , Product.unapply)
  }

  protected val Products = TableQuery[ProductsTable]
}

/**
 * CouponsTable
 */
private[repo] trait CouponsTable { self : HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  private[CouponsTable] class CouponsTable(tag: Tag) extends Table[Coupon](tag, "Coupons") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val code = column[String]("code")
    val quantity = column[Int]("quantity")
    val validFrom = column[String]("validFrom")
    val validUntil = column[String]("validUntil")
    val discountAmount = column[Double]("discountAmount")
    val discountType = column[String]("discountType")
    def * = (quantity, discountType, validUntil, validFrom, code, id, discountAmount) <> (Coupon.tupled , Coupon.unapply)
  }

  protected val Coupons = TableQuery[CouponsTable]
}

/**
 * LineItemsTable
 */
private[repo] trait LineItemsTable { self : HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  private[LineItemsTable] class LineItemsTable(tag: Tag) extends Table[LineItem](tag, "LineItems") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val orderId = column[Int]("orderId")
    val productId = column[Int]("productId")
    val productName = column[String]("productName")
    val productPrice = column[Double]("productPrice")
    val quantity = column[Int]("quantity")
    def * = (quantity, productPrice, orderId, id, productName, productId) <> (LineItem.tupled, LineItem.unapply)
  }

  protected val LineItems = TableQuery[LineItemsTable]
}

/**
 * ShippingAddressesTable
 */
private[repo] trait ShippingAddressesTable { self : HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  private[ShippingAddressesTable] class ShippingAddressesTable(tag: Tag) extends Table[ShippingAddress](tag, "ShippingAddresses") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val orderId = column[Int]("orderId")
    val address = column[String]("address")
    val name = column[String]("name")
    val phoneNumber = column[String]("phoneNumber")
    val email = column[String]("email")
    def * = (name, phoneNumber, email, orderId, id, address) <> (ShippingAddress.tupled, ShippingAddress.unapply)
  }

  protected val ShippingAdresses = TableQuery[ShippingAddressesTable]
}

/**
 * PaymentProffsTable
 */
private[repo] trait PaymentProffsTable { self : HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  private[PaymentProffsTable] class PaymentProffsTable(tag: Tag) extends Table[PaymentProff](tag, "PaymentProffs") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val orderId = column[Int]("orderId")
    val amount = column[Double]("amount")
    val note = column[String]("note")
    val paymentDate = column[String]("paymentDate")
    def * = (orderId, amount, id, note, paymentDate) <> (PaymentProff.tupled, PaymentProff.unapply)
  }

  protected val PaymentProffs = TableQuery[PaymentProffsTable]
}
