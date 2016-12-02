
import play.api.mvc.{Action, Controller}

import play.api.data.validation.Constraint

import play.api.inject.{ApplicationLifecycle,ConfigurationProvider}

import de.zalando.play.controllers._

import PlayBodyParsing._

import PlayValidations._

import scala.util._

import javax.inject._

import scala.concurrent.duration._
import scala.concurrent.{Future,Await}
import scala.concurrent.ExecutionContext.Implicits.global
import com.prasetiady.repo._
import ItemRepo.newItemToItem

/**
 * This controller is re-generated after each change in the specification.
 * Please only place your hand-written code between appropriate comments in the body of the controller.
 */

package v1.yaml {

    class Customer @Inject() (lifecycle: ApplicationLifecycle, config: ConfigurationProvider) extends CustomerBase {
        // ----- Start of unmanaged code area for constructor Customer

        // ----- End of unmanaged code area for constructor Customer
        val ordersGet = ordersGetAction { (customer_id: String) =>  
            // ----- Start of unmanaged code area for action  Customer.ordersGet
            NotImplementedYet
            // ----- End of unmanaged code area for action  Customer.ordersGet
        }
        val cartGet = cartGetAction { (customer_id: String) =>  
            // ----- Start of unmanaged code area for action  Customer.cartGet
            NotImplementedYet
            // ----- End of unmanaged code area for action  Customer.cartGet
        }
    
    }
}
package v1.yaml {

    class Items @Inject() (lifecycle: ApplicationLifecycle, config: ConfigurationProvider) extends ItemsBase {
        // ----- Start of unmanaged code area for constructor Items
        @Inject private var itemRepo: ItemRepo = null
        // ----- End of unmanaged code area for constructor Items
        val itemGet = itemGetAction { (id: Int) =>  
            // ----- Start of unmanaged code area for action  Items.itemGet
            val f: Future[Item] = Future {
              Await.result(itemRepo.getById(id), Duration.Inf) match {
                case Some(item) => item
                case None => throw new Exception("Not found item with id: " + id)
              }
            }
            ItemGet200(f)
            // ----- End of unmanaged code area for action  Items.itemGet
        }
        val itemPut = itemPutAction { input: (Int, NewItem) =>
            val (id, item) = input
            // ----- Start of unmanaged code area for action  Items.itemPut
            val tempItem: Item = item
            val newItem = tempItem.copy(id = id)
            val f: Future[Item] = Future {
              Await.result(itemRepo.update(newItem), Duration.Inf)
              newItem
            }
            ItemPut200(f)
            // ----- End of unmanaged code area for action  Items.itemPut
        }
        val itemDelete = itemDeleteAction { (id: Int) =>  
            // ----- Start of unmanaged code area for action  Items.itemDelete
            Await.result(itemRepo.delete(id), Duration.Inf)
            ItemDelete200()
            // ----- End of unmanaged code area for action  Items.itemDelete
        }
        val itemsGet = itemsGetAction {  _ =>  
            // ----- Start of unmanaged code area for action  Items.itemsGet
            ItemsGet200(itemRepo.getAll())
            // ----- End of unmanaged code area for action  Items.itemsGet
        }
        val itemsPost = itemsPostAction { (item: NewItem) =>  
            // ----- Start of unmanaged code area for action  Items.itemsPost
            val f: Future[Item] = Future {
              val id = Await.result(itemRepo.create(item), Duration.Inf)
              val newItem: Item = item
              newItem.copy(id = id)
            }
            ItemsPost200(f)
            // ----- End of unmanaged code area for action  Items.itemsPost
        }
    
    }
}
package v1.yaml {

    class Order @Inject() (lifecycle: ApplicationLifecycle, config: ConfigurationProvider) extends OrderBase {
        // ----- Start of unmanaged code area for constructor Order

        // ----- End of unmanaged code area for constructor Order
        val paymentProffPost = paymentProffPostAction { input: (Int, String) =>
            val (order_id, payment_proff) = input
            // ----- Start of unmanaged code area for action  Order.paymentProffPost
            NotImplementedYet
            // ----- End of unmanaged code area for action  Order.paymentProffPost
        }
        val shipPut = shipPutAction { (order_id: Int) =>  
            // ----- Start of unmanaged code area for action  Order.shipPut
            NotImplementedYet
            // ----- End of unmanaged code area for action  Order.shipPut
        }
        val orderGet = orderGetAction { (order_id: Int) =>  
            // ----- Start of unmanaged code area for action  Order.orderGet
            NotImplementedYet
            // ----- End of unmanaged code area for action  Order.orderGet
        }
        val cancelPut = cancelPutAction { (order_id: Int) =>  
            // ----- Start of unmanaged code area for action  Order.cancelPut
            NotImplementedYet
            // ----- End of unmanaged code area for action  Order.cancelPut
        }
        val couponPost = couponPostAction { input: (Int, String) =>
            val (order_id, coupon_code) = input
            // ----- Start of unmanaged code area for action  Order.couponPost
            NotImplementedYet
            // ----- End of unmanaged code area for action  Order.couponPost
        }
        val lineItemPost = lineItemPostAction { input: (Int, Int) =>
            val (order_id, product_id) = input
            // ----- Start of unmanaged code area for action  Order.lineItemPost
            NotImplementedYet
            // ----- End of unmanaged code area for action  Order.lineItemPost
        }
        val checkoutPut = checkoutPutAction { input: (Int, ShippingAddress) =>
            val (order_id, shipping_address) = input
            // ----- Start of unmanaged code area for action  Order.checkoutPut
            NotImplementedYet
            // ----- End of unmanaged code area for action  Order.checkoutPut
        }
    
    }
}