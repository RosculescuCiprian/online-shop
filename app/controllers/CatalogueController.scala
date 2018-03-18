package controllers

import javax.inject.Inject
import models.catalogues.CatalogueItemRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

class CatalogueController @Inject()(repo: CatalogueItemRepository, cc: MessagesControllerComponents)
                                   (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {


  val catalogueForm: Form[CreateCatalogueForm] = Form {
    mapping(
      "sellerId" -> longNumber,
      "productId" -> longNumber,
      "price" -> longNumber,
      "desc" -> nonEmptyText.verifying(minLength(12))
    )(CreateCatalogueForm.apply)(CreateCatalogueForm.unapply)
  }

  def createCatalogue = Action.async { implicit request =>
    catalogueForm.bindFromRequest().fold(
      errorForm => Future.successful(BadRequest(errorForm.errorsAsJson)),
      correctForm => repo.createCatalogueItemEntry(correctForm.sellerId, correctForm.productId, correctForm.price, correctForm.desc).map(
        _ match {
          case Left(error) => BadRequest(Json.obj("error" -> error.getMessage))
          case Right(catalogueItem) => Created("id" -> catalogueItem.id)
        }
      )
    )
  }

  def getCatalogueBySellerId(sellerId: Long) = Action.async {
    repo.getCatalogueItemsBySellerId(sellerId).map { listOfCatalogueItems => Ok(Json.toJson(listOfCatalogueItems)) }
  }

  def getCatalogue(catalogueId: Long) = Action.async {
    repo.getCatalogueById(catalogueId).map { optionalCatalogue =>
      optionalCatalogue.fold(
        NotFound(Json.obj("error" -> s"The catalogue with id $catalogueId was not found")))(
        catalogue => Ok(Json.toJson(catalogue))
      )
    }
  }

  def getCataloguesWithProduct(productId: Long) = Action.async {
    repo.getCataloguesByProductId(productId).map(listOfCatalogues => Ok(Json.toJson(listOfCatalogues)))
  }

}

case class CreateCatalogueForm(sellerId: Long, productId: Long, price: Long, desc: String)