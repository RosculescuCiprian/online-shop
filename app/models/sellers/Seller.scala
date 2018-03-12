package models.sellers

import play.api.libs.json.{Json, OFormat}

case class Seller(id: Long, name: String, desc: String) {
  def patch(name: Option[String] = None, desc: Option[String] = None): Seller =
    this.copy(name = name.getOrElse(this.name), desc = desc.getOrElse(this.desc))
}

object Seller {
  implicit val sellerFormat:OFormat[Seller] = Json.format[Seller]
}
