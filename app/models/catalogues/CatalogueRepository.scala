package models.catalogues

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import tables.{CatalogueItemTable, SlickRepository}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class CatalogueRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends SlickRepository with CatalogueItemTable {

  override implicit protected final val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import dbConfig.profile.api._


  def createCatalogue(sellerId: Long): Future[Either[Throwable, Catalogue]] = db.run {
    val creationQuery = (cataloguesQueryManager.map(catalogue => (catalogue.sellerId))
      returning cataloguesQueryManager.map(_.id)
      into((idSeller, idCatalogue) => Catalogue(idCatalogue, idSeller))) += sellerId
    creationQuery.asTry
  } map {
    _ match {
      case Success(catalogue) => Right(catalogue)
      case Failure(error) => Left(error)
    }
  }

  def createCatalogueItemEntry(catalogueId: Long, productId: Long, price: Long, desc: String): Future[Either[Throwable, CatalogueItem]] = db.run {
    val creationQuery = (catalogueItemQueryManager.map(item => (item.catalogueId, item.productId, item.price, item.desc))
      returning catalogueItemQueryManager.map(_.id)
      into((itemElements, id) => CatalogueItem(id, itemElements._1, itemElements._2, itemElements._3, itemElements._4))
      ) += (catalogueId, productId, price, desc)
    creationQuery.asTry
  } map {
    _ match {
      case Success(prod) => Right(prod)
      case Failure(error) => Left(error)
    }
  }

  def getCatalogueBySellerId(sellerId: Long): Future[Seq[Catalogue]] = db.run {
    cataloguesQueryManager.filter(_.sellerId === sellerId).result
  }

  def getCatalogueItemsByCatalogueId(catalogueId: Long): Future[Seq[CatalogueItem]] = db.run {
    catalogueItemQueryManager.filter(_.catalogueId === catalogueId).result
  }

  def getCataloguesByProductId(productId: Long): Future[Seq[CatalogueItem]] = db.run {
    catalogueItemQueryManager.filter(_.productId === productId).result
  }

  def updateProductFromCatalogue(catalogueId: Long, productId: Long, price: Long, desc: String): Future[Either[Throwable, Int]] = db.run {
    val updateQuery = for {c <- catalogueItemQueryManager if c.productId === productId if c.catalogueId === catalogueId} yield (c.price, c.desc)
    updateQuery.update(price, desc).asTry
  } map {
    _ match {
      case Success(number) => Right(number)
      case Failure(error) => Left(error)
    }
  }

}
