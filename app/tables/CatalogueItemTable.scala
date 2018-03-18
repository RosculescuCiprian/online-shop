package tables

import models.catalogues.CatalogueItem

trait CatalogueItemTable extends CatalogueTable with ProductTable {

  this: SlickRepository =>

  import driver.api._

  protected class CatalogueItemRepresentation(tag: Tag) extends Table[CatalogueItem](tag, "catalogue_items") {
    def id = column[Long]("id")
    def productId = column[Long]("productId")
    def price = column[Long]("price")
    def desc = column[String]("desc")

    def cataloguesForeignKey = foreignKey("CATALOGUE_FK", id, cataloguesQueryManager)(_.id)
    def productForeignKey = foreignKey("PRODUCT_FK", productId, products)(_.id)

    def * = (id, productId, price, desc) <> ((CatalogueItem.apply _).tupled, CatalogueItem.unapply)
  }

  protected def catalogueItems = TableQuery[CatalogueItemRepresentation]
}
