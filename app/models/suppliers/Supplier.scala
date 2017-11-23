package models.suppliers

import play.api.libs.json.Json

case class Supplier(id: Long, name: String, desc: String) {
  def patch(name: Option[String], desc: Option[String]): Supplier = {
    return this.copy(
      name = name.getOrElse(this.name),
      desc = desc.getOrElse(this.desc)
    )
  }
}

object Supplier {
  implicit val supplierFormat = Json.format[Supplier]
}
