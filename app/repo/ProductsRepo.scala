package	com.prasetiady.repo

import scala.concurrent.Future
import v1.yaml._
import javax.inject.{Inject,Singleton}
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile

@Singleton()
class ProductsRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends ProductsTable with HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  /**
   * @return
   * Get all products
   */
  def getAllProducts(): Future[List[Product]] = db.run { Products.to[List].result }

}
