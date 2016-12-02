
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

/**
 * This controller is re-generated after each change in the specification.
 * Please only place your hand-written code between appropriate comments in the body of the controller.
 */

package v1.yaml {

    class CustomerHandler @Inject() (lifecycle: ApplicationLifecycle, config: ConfigurationProvider) extends CustomerHandlerBase {
        // ----- Start of unmanaged code area for constructor CustomerHandler
        @Inject private var customersRepo: CustomersRepo = null
        // ----- End of unmanaged code area for constructor CustomerHandler
        val ordersGet = ordersGetAction { (customerId: Int) =>  
            // ----- Start of unmanaged code area for action  CustomerHandler.ordersGet
            OrdersGet200(customersRepo.getAllCustomerOrders(customerId))
            // ----- End of unmanaged code area for action  CustomerHandler.ordersGet
        }
        val cartGet = cartGetAction { (customerId: Int) =>  
            // ----- Start of unmanaged code area for action  CustomerHandler.cartGet
            CartGet200(Future[ShoppingCart]{
              val orderId = Await.result(customersRepo.getShoppingCart(customerId), Duration.Inf)
              ShoppingCart(orderId)
            })
            // ----- End of unmanaged code area for action  CustomerHandler.cartGet
        }
        val customersGet = customersGetAction {  _ =>  
            // ----- Start of unmanaged code area for action  CustomerHandler.customersGet
            CustomersGet200(customersRepo.getAllCustomers())
            // ----- End of unmanaged code area for action  CustomerHandler.customersGet
        }
    
    }
}
package v1.yaml {

    class OrderHandler @Inject() (lifecycle: ApplicationLifecycle, config: ConfigurationProvider) extends OrderHandlerBase {
        // ----- Start of unmanaged code area for constructor OrderHandler

        // ----- End of unmanaged code area for constructor OrderHandler
        val couponPost = couponPostAction { input: (Int, String) =>
            val (orderId, coupon_code) = input
            // ----- Start of unmanaged code area for action  OrderHandler.couponPost
            NotImplementedYet
            // ----- End of unmanaged code area for action  OrderHandler.couponPost
        }
        val orderGet = orderGetAction { (orderId: Int) =>  
            // ----- Start of unmanaged code area for action  OrderHandler.orderGet
            NotImplementedYet
            // ----- End of unmanaged code area for action  OrderHandler.orderGet
        }
        val shipPut = shipPutAction { (orderId: Int) =>  
            // ----- Start of unmanaged code area for action  OrderHandler.shipPut
            NotImplementedYet
            // ----- End of unmanaged code area for action  OrderHandler.shipPut
        }
        val verifyPut = verifyPutAction { (orderId: Int) =>  
            // ----- Start of unmanaged code area for action  OrderHandler.verifyPut
            NotImplementedYet
            // ----- End of unmanaged code area for action  OrderHandler.verifyPut
        }
        val paymentProffPost = paymentProffPostAction { input: (Int, String) =>
            val (orderId, payment_proff) = input
            // ----- Start of unmanaged code area for action  OrderHandler.paymentProffPost
            NotImplementedYet
            // ----- End of unmanaged code area for action  OrderHandler.paymentProffPost
        }
        val cancelPut = cancelPutAction { (orderId: Int) =>  
            // ----- Start of unmanaged code area for action  OrderHandler.cancelPut
            NotImplementedYet
            // ----- End of unmanaged code area for action  OrderHandler.cancelPut
        }
        val checkoutPut = checkoutPutAction { input: (Int, ShippingAddress) =>
            val (orderId, shippingAddress) = input
            // ----- Start of unmanaged code area for action  OrderHandler.checkoutPut
            NotImplementedYet
            // ----- End of unmanaged code area for action  OrderHandler.checkoutPut
        }
        val lineItemPost = lineItemPostAction { input: (Int, Int) =>
            val (orderId, product_id) = input
            // ----- Start of unmanaged code area for action  OrderHandler.lineItemPost
            NotImplementedYet
            // ----- End of unmanaged code area for action  OrderHandler.lineItemPost
        }
    
    }
}
