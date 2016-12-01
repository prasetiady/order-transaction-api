
import play.api.mvc.{Action, Controller}

import play.api.data.validation.Constraint

import play.api.inject.{ApplicationLifecycle,ConfigurationProvider}

import de.zalando.play.controllers._

import PlayBodyParsing._

import PlayValidations._

import scala.util._

import javax.inject._

import play.api.mvc.Session
import scala.concurrent.duration._
import scala.concurrent.{Future,Await}
import scala.concurrent.ExecutionContext.Implicits.global
import com.prasetiady.repo._
import ItemRepo.newItemToItem

/**
 * This controller is re-generated after each change in the specification.
 * Please only place your hand-written code between appropriate comments in the body of the controller.
 */

package shop.yaml {

    class ShopYaml @Inject() (lifecycle: ApplicationLifecycle, config: ConfigurationProvider) extends ShopYamlBase {
        // ----- Start of unmanaged code area for constructor ShopYaml
        @Inject private var itemRepo: ItemRepo = null
        // ----- End of unmanaged code area for constructor ShopYaml
        val getAll = getAllAction {  _ =>  
            // ----- Start of unmanaged code area for action  ShopYaml.getAll
            GetAll200(itemRepo.getAll())
            // ----- End of unmanaged code area for action  ShopYaml.getAll
        }
        val create = createAction { (item: NewItem) =>  
            // ----- Start of unmanaged code area for action  ShopYaml.create
            val f: Future[Item] = Future {
              val id = Await.result(itemRepo.create(item), Duration.Inf)
              val newItem: Item = item
              newItem.copy(id = id)
            }
            Create200(f)
            // ----- End of unmanaged code area for action  ShopYaml.create
        }
        val details = detailsAction { (id: Int) =>  
            // ----- Start of unmanaged code area for action  ShopYaml.details
            val f: Future[Item] = Future {
              Await.result(itemRepo.getById(id), Duration.Inf) match {
                case Some(item) => item
                case None => throw new Exception("Not found item with id: " + id)
              }
            }
            Details200(f)
            // ----- End of unmanaged code area for action  ShopYaml.details
        }
        val update = updateAction { input: (Int, NewItem) =>
            val (id, item) = input
            // ----- Start of unmanaged code area for action  ShopYaml.update
            val tempItem: Item = item
            val newItem = tempItem.copy(id = id)
            val f: Future[Item] = Future {
              Await.result(itemRepo.update(newItem), Duration.Inf)
              newItem
            }
            Update200(f)
            // ----- End of unmanaged code area for action  ShopYaml.update
        }
        val delete = deleteAction { (id: Int) =>  
            // ----- Start of unmanaged code area for action  ShopYaml.delete
            Await.result(itemRepo.delete(id), Duration.Inf)
            Delete200()
            // ----- End of unmanaged code area for action  ShopYaml.delete
        }
    
    }
}
