package models.sellers

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import tables.{SellerTable, SlickRepository}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SellerRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends SlickRepository with SellerTable {
  override implicit protected final val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import dbConfig.profile.api._

  def createSeller(name: String, desc:String): Future[Seller] = db.run {
    (sellersContainer.map(s => (s.name, s.desc))
      returning sellersContainer.map(_.id)
      into((nameDesc, id) => Seller(id, nameDesc._1, nameDesc._2)
      )) += (name, desc)
  }

  def getSellerById(id:Long): Future[Option[Seller]] = db.run {
    sellersContainer.filter(_.id === id).result.headOption
  }

  def getSellers(): Future[Seq[Seller]] = db.run {
    sellersContainer.result
  }

}
