package tables

import models.sellers.Seller

trait SellerTable {

  this:SlickRepository =>

  import driver.api._

  protected class Sellers(tag: Tag) extends Table[Seller](tag, "sellers") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def desc = column[String]("desc")

    def * = (id, name, desc) <> ((Seller.apply _).tupled, Seller.unapply)
  }


}
