package models.catalogues

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import tables.{CatalogueItemTable, SlickRepository}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class CatalogueItemRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends SlickRepository with CatalogueItemTable {

  override implicit protected final val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import dbConfig.profile.api._

  def createCatalogueItemEntry(sellerId: Long, productId: Long, price: Long, desc: String): Future[Either[Throwable, CatalogueItem]] = db.run {
    val creationQuery = (catalogueItems.map(item => (item.sellerId, item.productId, item.price, item.desc))
      returning catalogueItems.map(_.id)
      into((itemElements, itemId) => CatalogueItem(itemId, itemElements._1, itemElements._2, itemElements._3, itemElements._4))
      ) += (sellerId, productId, price, desc)
    creationQuery.asTry
  } map {
    _ match {
      case Success(prod) => Right(prod)
      case Failure(error) => Left(error)
    }
  }

  def getCatalogueItemsBySellerId(sellerId: Long): Future[Seq[CatalogueItem]] = db.run {
    catalogueItems.filter(_.sellerId === sellerId).result
  }

  def getCatalogueById(catalogueId: Long): Future[Option[CatalogueItem]] = db.run {
    catalogueItems.filter(_.id === catalogueId).result.headOption
  }

  def getCataloguesByProductId(productId: Long): Future[Seq[CatalogueItem]] = db.run {
    catalogueItems.filter(_.productId === productId).result
  }

  def updateProductFromCatalogue(catalogueId: Long, productId: Long, price: Long, desc: String): Future[Either[Throwable, Int]] = db.run {
    val updateQuery = for {c <- catalogueItems if c.productId === productId if c.id === catalogueId} yield (c.price, c.desc)
    updateQuery.update(price, desc).asTry
  } map {
    _ match {
      case Success(number) => Right(number)
      case Failure(error) => Left(error)
    }
  }

  def changeProductForCatalogue(catalogueId: Long, sellerId: Long, productId: Long, price: Long, desc: String): Future[Either[Throwable, Int]] = db.run {
    val updateQuery = for{ catalogue <- catalogueItems if catalogue.id === catalogueId} yield (catalogue.sellerId, catalogue.productId, catalogue.price, catalogue.desc)
    updateQuery.update(sellerId, productId, price, desc).asTry
  } map {
    _ match {
      case Success(number) => Right(number)
      case Failure(error) => Left(error)
    }
  }

}
