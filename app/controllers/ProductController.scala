package controllers

import javax.inject.Inject

import models.products.ProductRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

class ProductController @Inject()(repo: ProductRepository, cc: MessagesControllerComponents)
                                 (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val productForm: Form[CreateProductForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "supplierId" -> longNumber,
      "desc" -> nonEmptyText
    )(CreateProductForm.apply)(CreateProductForm.unapply)
  }

}

case class CreateProductForm(name: String, supplierId: Long, desc: String)