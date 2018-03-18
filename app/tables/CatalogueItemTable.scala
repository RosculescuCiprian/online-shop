package tables

import models.catalogues.CatalogueItem

trait CatalogueItemTable extends CatalogueTable with ProductTable {

  this: SlickRepository =>

  import driver.api._

  protected class CatalogueItemRepresentation(tag: Tag) extends Table[CatalogueItem](tag, "catalogue_items") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def catalogueId = column[Long]("catalogueId")
    def productId = column[Long]("productId")
    def price = column[Long]("price")
    def desc = column[String]("desc")

    def cataloguesForeignKey = foreignKey("CATALOGUE_FK", catalogueId, cataloguesQueryManager)(_.id)
    def productForeignKey = foreignKey("PRODUCT_FK", productId, products)(_.id)

    def * = (id, catalogueId, productId, price, desc) <> ((CatalogueItem.apply _).tupled, CatalogueItem.unapply)
  }

  protected def catalogueItemQueryManager = TableQuery[CatalogueItemRepresentation]
}
