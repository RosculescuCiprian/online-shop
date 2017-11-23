package models.suppliers

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import tables.{SlickRepository, SupplierTable}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SupplierRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends SlickRepository with SupplierTable {

  override implicit protected final val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import dbConfig.profile.api._

  def create(name:String, desc:String): Future[Supplier] = db.run {
    (suppliers.map(supplier => (supplier.name, supplier.desc))
      returning suppliers.map(_.id)
      into ((supplierDetailPair, id) => Supplier(id, supplierDetailPair._1, supplierDetailPair._2))
      ) += (name, desc)
  }

  def getAllSuppliers(): Future[Seq[Supplier]] = db.run {
    suppliers.result
  }

  def getSupplierById(id: Long): Future[Option[Supplier]] = db.run {
    suppliers.filter(_.id === id).result.headOption
  }

}
