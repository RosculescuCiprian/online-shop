package controllers

import javax.inject._

import models._
import models.clients.ClientRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class ClientController @Inject()(repo: ClientRepository,
                                 cc: MessagesControllerComponents
                                )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  /**
   * The mapping for the person form.
   */
  val personForm: Form[CreatePersonForm] = Form {
    mapping(
      "email" -> email,
      "age" -> number.verifying(min(0), max(140))
    )(CreatePersonForm.apply)(CreatePersonForm.unapply)
  }

  /**
   * The index action.
   */
  def index = Action { implicit request =>
    Ok(Json.prettyPrint(Json.obj("message" -> "Welcome to the online store")))
  }

  /**
   * The add person action.
   *
   * This is asynchronous, since we're invoking the asynchronous methods on PersonRepository.
   */
  def addPerson = Action.async { implicit request =>
    // Bind the form first, then fold the result, passing a function to handle errors, and a function to handle succes.
    personForm.bindFromRequest.fold(
      // The error function. We return the index page with the error form, which will render the errors.
      // We also wrap the result in a successful future, since this action is synchronous, but we're required to return
      // a future because the person creation function returns a future.
      errorForm => {
        Future.successful(BadRequest(errorForm.errorsAsJson))
      },
      // There were no errors in the from, so create the person.
      person => {
        repo.create(person.name, person.age).map { person => Ok(Json.obj("id"-> person.id))
        }
      }
    )
  }

  /**
   * A REST endpoint that gets all the people as JSON.
   */
  def getPersons = Action.async { implicit request =>
    repo.list().map { people =>
      Ok(Json.toJson(people))
    }
  }

  def getPersonById(id: Long) = Action.async { implicit request =>
    repo.getClientById(id).map { clientOption =>
      clientOption
        .fold(NotFound(Json.obj("error" -> s"The client with id $id was not found")))(client => Ok(Json.toJson(client)))
    }
  }
}

/**
 * The create person form.
 *
 * Generally for forms, you should define separate objects to your models, since forms very often need to present data
 * in a different way to your models.  In this case, it doesn't make sense to have an id parameter in the form, since
 * that is generated once it's created.
 */
case class CreatePersonForm(name: String, age: Int)
