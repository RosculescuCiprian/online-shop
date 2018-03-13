package models.catalogues

import play.api.libs.json.{Json, OFormat}

case class CatalogueItem(id: Long, sellerId: Long, productId: Long, price: Long, desc: String) {
  def patch(sellerId: Option[Long], productId: Option[Long], price: Option[Long], desc: Option[String]): CatalogueItem = {
    this.copy(
      sellerId = sellerId.getOrElse(this.sellerId),
      productId = productId.getOrElse(this.productId),
      price = price.getOrElse(this.price),
      desc = desc.getOrElse(this.desc)
    )
  }
}

object CatalogueItem {
  implicit val catalogueItemFormat: OFormat[CatalogueItem] = Json.format[CatalogueItem]
}
