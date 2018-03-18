package models.catalogues

import play.api.libs.json.{Json, OFormat}

case class CatalogueInfo(catalogueId: Long, sellerId: Long, productId: Long, price: Long, desc: String)

object CatalogueInfo {
  implicit val catalogueInfoFormat: OFormat[CatalogueInfo] = Json.format[CatalogueInfo]

  def createCatalogueInfo(catalogue: Catalogue, catalogueItem: CatalogueItem):CatalogueInfo =
    CatalogueInfo(catalogue.catalogueId, catalogue.sellerId, catalogueItem.productId, catalogueItem.price, catalogueItem.desc)
}
