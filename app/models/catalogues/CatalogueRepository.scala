package models.catalogues

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import tables.{CatalogueTable, SlickRepository}

import scala.concurrent.ExecutionContext

@Singleton
class CatalogueRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends SlickRepository with CatalogueTable {

  override implicit protected final val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import dbConfig.profile.api._

}
