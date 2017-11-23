package tables

import models.suppliers.Supplier

trait SupplierTable {

  this:SlickRepository =>

  import driver.api._

  protected class Suppliers(tag: Tag) extends Table[Supplier](tag, "suppliers") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def desc = column[String]("desc")

    def * = (id, name, desc) <> ((Supplier.apply _).tupled, Supplier.unapply)
  }

  protected def suppliers = TableQuery[Suppliers]

}
