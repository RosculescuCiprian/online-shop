package tables

import models.catalogues.CatalogueItem

trait CatalogueTable extends ProductTable with SellerTable {

  this: SlickRepository =>

  import driver.api._

  protected class CatalogueItemRepresentation(tag: Tag) extends Table[CatalogueItem](tag, "catalogue_items") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def sellerId = column[Long]("sellerId")
    def productId = column[Long]("productId")
    def price = column[Long]("price")
    def desc = column[String]("desc")

    def sellerForeignKey = foreignKey("SELLER_FK", sellerId, sellersContainer)(_.id)
    def productForeignKey = foreignKey("PRODUCT_FK", productId, products)(_.id)

    def * = (id, sellerId, productId, price, desc) <> ((CatalogueItem.apply _).tupled, CatalogueItem.unapply)
  }

  protected def catalogueItems = TableQuery[CatalogueItemRepresentation]
}
