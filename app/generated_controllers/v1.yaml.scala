
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
            CartGet200(Future[SimpleOrder]{
              val orderId = Await.result(customersRepo.getShoppingCart(customerId), Duration.Inf)
              SimpleOrder(orderId)
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
        @Inject private var ordersRepo: OrdersRepo = null
        // ----- End of unmanaged code area for constructor OrderHandler
        val productPost = productPostAction { (body: OrderProductPostBody) =>  
            // ----- Start of unmanaged code area for action  OrderHandler.productPost
            Await.result(ordersRepo.addProduct(body.orderId, body.productId), Duration.Inf)
            ProductPost200()
            // ----- End of unmanaged code area for action  OrderHandler.productPost
        }
        val couponPost = couponPostAction { (body: OrderCouponPostBody) =>  
            // ----- Start of unmanaged code area for action  OrderHandler.couponPost
            Await.result(ordersRepo.applyCoupon(body.orderId, body.couponCode), Duration.Inf)
            CouponPost200()
            // ----- End of unmanaged code area for action  OrderHandler.couponPost
        }
        val orderGet = orderGetAction { (orderId: Int) =>  
            // ----- Start of unmanaged code area for action  OrderHandler.orderGet
            OrderGet200(ordersRepo.getOrderDetail(orderId))
            // ----- End of unmanaged code area for action  OrderHandler.orderGet
        }
        val verifyPut = verifyPutAction { (body: SimpleOrder) =>  
            // ----- Start of unmanaged code area for action  OrderHandler.verifyPut
            Await.result(ordersRepo.verifyOrder(body.orderId), Duration.Inf)
            VerifyPut200()
            // ----- End of unmanaged code area for action  OrderHandler.verifyPut
        }
        val cancelPut = cancelPutAction { (body: SimpleOrder) =>  
            // ----- Start of unmanaged code area for action  OrderHandler.cancelPut
            Await.result(ordersRepo.cancelOrder(body.orderId), Duration.Inf)
            CancelPut200()
            // ----- End of unmanaged code area for action  OrderHandler.cancelPut
        }
        val paymentProffPost = paymentProffPostAction { (body: OrderPaymentProffPostBody) =>  
            // ----- Start of unmanaged code area for action  OrderHandler.paymentProffPost
            Await.result(ordersRepo.submitPaymentProff(body.orderId, body.paymentProff), Duration.Inf)
            PaymentProffPost200()
            // ----- End of unmanaged code area for action  OrderHandler.paymentProffPost
        }
        val shipPut = shipPutAction { (body: SimpleOrder) =>  
            // ----- Start of unmanaged code area for action  OrderHandler.shipPut
            Await.result(ordersRepo.shipOrder(body.orderId), Duration.Inf)
            ShipPut200()
            // ----- End of unmanaged code area for action  OrderHandler.shipPut
        }
        val ordersGet = ordersGetAction {  _ =>  
            // ----- Start of unmanaged code area for action  OrderHandler.ordersGet
            OrdersGet200(ordersRepo.getAllOrders())
            // ----- End of unmanaged code area for action  OrderHandler.ordersGet
        }
        val submitPut = submitPutAction { (body: OrderSubmitPutBody) =>  
            // ----- Start of unmanaged code area for action  OrderHandler.submitPut
            Await.result(ordersRepo.submitOrder(body.orderId, body.shippingAddress), Duration.Inf)
            SubmitPut200()
            // ----- End of unmanaged code area for action  OrderHandler.submitPut
        }
    
    }
}
package v1.yaml {

    class ProductHandler @Inject() (lifecycle: ApplicationLifecycle, config: ConfigurationProvider) extends ProductHandlerBase {
        // ----- Start of unmanaged code area for constructor ProductHandler
        @Inject private var productsRepo: ProductsRepo = null
        // ----- End of unmanaged code area for constructor ProductHandler
        val productsGet = productsGetAction {  _ =>  
            // ----- Start of unmanaged code area for action  ProductHandler.productsGet
            ProductsGet200(productsRepo.getAllProducts())
            // ----- End of unmanaged code area for action  ProductHandler.productsGet
        }
    
    }
}
package v1.yaml {

    class CouponHandler @Inject() (lifecycle: ApplicationLifecycle, config: ConfigurationProvider) extends CouponHandlerBase {
        // ----- Start of unmanaged code area for constructor CouponHandler
        @Inject private var couponsRepo: CouponsRepo = null
        // ----- End of unmanaged code area for constructor CouponHandler
        val couponsGet = couponsGetAction {  _ =>  
            // ----- Start of unmanaged code area for action  CouponHandler.couponsGet
            CouponsGet200(couponsRepo.getAllCoupons())
            // ----- End of unmanaged code area for action  CouponHandler.couponsGet
        }
    
    }
}
