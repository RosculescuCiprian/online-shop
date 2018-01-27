package models.products

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import tables.{ProductTable, SlickRepository}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class ProductRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends SlickRepository with ProductTable {

  override implicit protected final val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import dbConfig.profile.api._

  def createProduct(name: String, supplierId: Long, desc: String): Future[Either[String, Product]] = db.run {
    val creationQuery = (products.map(product => (product.name, product.supplierId, product.desc))
      returning products.map(_.id)
      into ((productTuple, productId) => Product(productId, productTuple._1, productTuple._2, productTuple._3))
      ) += (name, supplierId, desc)

    creationQuery.asTry
  } map {
    _ match {
      case Success(prod) => Right(prod)
      case Failure(e) => Left(e.getMessage)
    }
  }

  def getProductById(productId: Long): Future[Either[String, Option[Product]]] = db.run {
    products.filter(_.id === productId).result.headOption.asTry
  } map {
    _ match {
      case Success(product) => Right(product)
      case Failure(e) => Left(e.getMessage)
    }
  }

  def getProductForSupplier(supplierId: Long): Future[Either[String, Seq[Product]]] = db.run {
    products.filter(_.supplierId === supplierId).result.asTry
  } map {
    _ match {
      case Success(sequence) => Right(sequence)
      case Failure(e) => Left(e.getMessage)
    }
  }
}
