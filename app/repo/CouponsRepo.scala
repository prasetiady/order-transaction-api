package	com.prasetiady.repo

import scala.concurrent.Future
import v1.yaml._
import javax.inject.{Inject,Singleton}
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile

@Singleton()
class CouponsRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends CouponsTable with HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  /**
   * @return
   * Get all products
   */
  def getAllCoupons(): Future[List[Coupon]] = db.run { Coupons.to[List].result }

}
