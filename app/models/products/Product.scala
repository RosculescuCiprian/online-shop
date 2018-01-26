package models.products

import play.api.libs.json.Json

case class Product(id: Long, name: String, supplierId: Long, desc: String) {
  def patch(name: Option[String], supplierId: Option[Long], desc: Option[String]): Product =
    this.copy(
      name = name.getOrElse(this.name),
      supplierId = supplierId.getOrElse(this.supplierId),
      desc = desc.getOrElse(this.desc)
    )
}

object Product {
  implicit val productFormat = Json.format[Product]
}
