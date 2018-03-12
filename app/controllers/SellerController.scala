package controllers

import javax.inject.Inject

import models.sellers.SellerRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

class SellerController @Inject()(repo:SellerRepository,
                                 cc: MessagesControllerComponents)
                                (implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  val sellerForm: Form[CreateSellerForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "desc" -> nonEmptyText
    )(CreateSellerForm.apply)(CreateSellerForm.unapply)
  }

  def createSeller = Action.async { implicit request =>
    sellerForm.bindFromRequest().fold(
      errorForm => Future.successful(BadRequest(errorForm.errorsAsJson)),
      validSupplierForm => repo.createSeller(validSupplierForm.name, validSupplierForm.desc)
        .map(seller => Ok(Json.obj("id" -> seller.id)))
    )
  }

  def getSellers() = Action.async { implicit request =>
    repo.getSellers().map { seller => Ok(Json.toJson(seller))}
  }

  def getSellerById(id: Long) = Action.async { implicit request =>
    repo.getSellerById(id).map {sellerOption => sellerOption
      .fold(NotFound(Json.obj("error" -> s"Seller with id $id was not found")))(seller => Ok(Json.toJson(seller)))}
  }
}

case class CreateSellerForm(name: String, desc: String)