package	com.prasetiady.repo

import scala.concurrent.Future
import v1.yaml._
import javax.inject.{Inject,Singleton}
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile

@Singleton()
class OrdersRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends OrdersTable with HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._
}
