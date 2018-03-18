package controllers

import javax.inject.Inject
import models.catalogues.CatalogueRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

class CatalogueController @Inject()(repo: CatalogueRepository, cc: MessagesControllerComponents)
                                   (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {


  val catalogueItemForm: Form[CreateCatalogueItemForm] = Form {
    mapping(
      "productId" -> longNumber,
      "price" -> longNumber,
      "desc" -> nonEmptyText.verifying(minLength(12))
    )(CreateCatalogueItemForm.apply)(CreateCatalogueItemForm.unapply)
  }

  val catalogueForm: Form[CreateCatalogueForm] = Form {
    mapping(
      "sellerId" -> longNumber
    )(CreateCatalogueForm.apply)(CreateCatalogueForm.unapply)
  }

  val updateCatalogueItemForm: Form[UpdateCatalogueItemForm] = Form {
    mapping(
      "price" -> longNumber,
      "desc" -> nonEmptyText
    )(UpdateCatalogueItemForm.apply)(UpdateCatalogueItemForm.unapply)
  }


  def createCatalogue = Action.async { implicit request =>
    catalogueForm.bindFromRequest().fold(
      errorForm => Future.successful(BadRequest(errorForm.errorsAsJson)),
      correctForm => repo.createCatalogue(correctForm.sellerId).map {
        _ match {
          case Left(error) => BadRequest(Json.obj("error" -> error.getMessage))
          case Right(catalogueItem) => Created(Json.obj("id" -> catalogueItem.catalogueId))
        }
      }
    )
  }

  def addProductsToCatalogue(catalogueId: Long) = Action.async { implicit request =>
    catalogueItemForm.bindFromRequest().fold(
      errorForm => Future.successful(BadRequest(errorForm.errorsAsJson)),
      correctForm => repo.createCatalogueItemEntry(catalogueId, correctForm.productId, correctForm.price, correctForm.desc).map(
        _ match {
          case Left(error) => BadRequest(Json.obj("error" -> error.getMessage))
          case Right(catalogueItem) => Created(Json.obj("id" -> catalogueItem.catalogueId))
        }
      )
    )
  }

  def updateProductCatalogue(cid: Long, pid: Long) = Action.async { implicit request =>
    updateCatalogueItemForm.bindFromRequest().fold(
      errorForm => Future.successful(BadRequest(errorForm.errorsAsJson)),
      correctForm => repo.updateProductFromCatalogue(cid, pid, correctForm.price, correctForm.desc).map {
        _ match {
          case Left(error) => BadRequest(Json.obj("error" -> error.getMessage))
          case Right(number) => Ok(Json.obj("number_of_rows_updated" -> number))
        }
      }
    )
  }

  def getCatalogueBySellerId(sellerId: Long) = Action.async {
    repo.getCatalogueBySellerId(sellerId).map { listOfCatalogueItems => Ok(Json.toJson(listOfCatalogueItems)) }
  }

  def getCatalogueItemsForCatalogue(catalogueId: Long) = Action.async {
    repo.getCatalogueItemsByCatalogueId(catalogueId).map( list => Ok(Json.toJson(list)))
  }

  def getCataloguesWithProduct(productId: Long) = Action.async {
    repo.getCataloguesByProductId(productId).map(listOfCatalogues => Ok(Json.toJson(listOfCatalogues)))
  }

}

case class CreateCatalogueItemForm(productId: Long, price: Long, desc: String)

case class CreateCatalogueForm(sellerId: Long)

case class UpdateCatalogueItemForm(price: Long, desc: String)