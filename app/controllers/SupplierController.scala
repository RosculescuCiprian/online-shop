package controllers

import javax.inject._

import models.suppliers.SupplierRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}


class SupplierController @Inject()(repo: SupplierRepository,
                                   cc: MessagesControllerComponents
                                  )(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val supplierForm: Form[CreateSupplierForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "desc" -> nonEmptyText
    )(CreateSupplierForm.apply)(CreateSupplierForm.unapply)
  }

  def createSupplier() = Action.async { implicit request =>
    supplierForm.bindFromRequest().fold(
      errorForm => Future.successful(BadRequest(errorForm.errorsAsJson)),
      createdSupplierForm => repo.create(createdSupplierForm.name, createdSupplierForm.desc)
        .map{supplier => Ok(Json.obj("id" -> supplier.id))}
    )
  }

  def getSuppliers() = Action.async { implicit request =>
    repo.getAllSuppliers().map(supplierList => Ok(Json.toJson(supplierList)))
  }

  def getSupplierById(supplierId: Long) = Action.async { implicit request =>
    repo.getSupplierById(supplierId).map(optionSupplier =>
      optionSupplier.fold(NotFound(Json.obj("error" -> s"supplier with id $supplierId not found")))(supplier =>
        Ok(Json.toJson(supplier))))
  }

}

case class CreateSupplierForm(name: String, desc: String)
