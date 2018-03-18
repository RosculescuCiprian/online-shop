package tables

import models.catalogues.Catalogue

trait CatalogueTable extends SellerTable {

  this:SlickRepository =>

  import driver.api._

  protected class CatalogueTableRepresentation(tag: Tag) extends Table[Catalogue](tag, "catalogues") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def sellerId = column[Long]("sellerId")

    def sellerForeignKey = foreignKey("SELLER_FK", sellerId, sellersContainer)(_.id)

    def * = (id, sellerId) <> ((Catalogue.apply _).tupled, Catalogue.unapply)
  }

  def cataloguesQueryManager = TableQuery[CatalogueTableRepresentation]

}
