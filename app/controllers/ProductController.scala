package controllers

import javax.inject.Inject

import models.products.ProductRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

class ProductController @Inject()(repo: ProductRepository,
                                  cc: MessagesControllerComponents)
                                 (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val productForm: Form[CreateProductForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "supplierId" -> longNumber,
      "desc" -> nonEmptyText
    )(CreateProductForm.apply)(CreateProductForm.unapply)
  }

  val createProduct = Action.async { implicit request =>
    productForm.bindFromRequest().fold(
      errorForm => Future.successful(BadRequest(errorForm.errorsAsJson)),
      productData => repo.createProduct(productData.name, productData.supplierId, productData.desc) map {
        _ match {
          case Right(prod) => Ok(Json.obj("id" -> prod.id))
          case Left(err) => BadRequest(Json.obj("error" -> "there was a database error", "message" -> err))
        }
      }
    )
  }

  def getProductById(productId: Long) = Action.async {
    repo.getProductById(productId).map(_ match {
      case Right(productOption) => productOption.fold(NotFound(Json.obj("id" -> 0)))(product => Ok(Json.toJson(product)))
      case Left(databaseErrorMessage) => BadRequest(Json.obj("error" -> databaseErrorMessage))
    })
  }

  def getProductsBySupplierId(supplierId: Long) = Action.async {
    repo.getProductForSupplier(supplierId) map {
      _ match {
        case Right(products) => Ok(Json.toJson(products))
        case Left(str) => BadRequest(Json.obj("error" -> str))
      }
    }
  }

}

case class CreateProductForm(name: String, supplierId: Long, desc: String)