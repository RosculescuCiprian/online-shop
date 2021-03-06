package models.catalogues

import play.api.libs.json.{Json, OFormat}

case class CatalogueItem(id: Long, catalogueId: Long, productId: Long, price: Long, desc: String) {
  def patch(productId: Option[Long], price: Option[Long], desc: Option[String]): CatalogueItem = {
    this.copy(
      productId = productId.getOrElse(this.productId),
      price = price.getOrElse(this.price),
      desc = desc.getOrElse(this.desc)
    )
  }
}

object CatalogueItem {
  implicit val catalogueItemFormat: OFormat[CatalogueItem] = Json.format[CatalogueItem]
}
