package models.catalogues

import play.api.libs.json.{Json, OFormat}

case class Catalogue(catalogueId: Long, sellerId: Long) {
  def patch(sellerId: Option[Long]): Catalogue = this.copy(sellerId = sellerId.getOrElse(this.sellerId))
}

object Catalogue {
  implicit val catalogueFormat: OFormat[Catalogue] = Json.format[Catalogue]
}