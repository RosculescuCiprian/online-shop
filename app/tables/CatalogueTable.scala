package tables

trait CatalogueTable extends ProductTable with SellerTable {

  this: SlickRepository =>

  import driver.api._


}
