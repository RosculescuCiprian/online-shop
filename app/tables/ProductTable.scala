package tables

import models.products.Product

trait ProductTable extends SupplierTable {

  this: SlickRepository =>
  import driver.api._

  protected class ProductTableRepresentation(tag: Tag) extends Table[Product](tag, "products") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def supplierId = column[Long]("supplierId")
    def desc = column[String]("desc")

    def * = (id,name, supplierId, desc) <> ((Product.apply _).tupled, Product.unapply)
    def supplierForeignKey = foreignKey("SUP_FK", supplierId, suppliers)(_.id)
  }

  protected def products = TableQuery[ProductTableRepresentation]

}
