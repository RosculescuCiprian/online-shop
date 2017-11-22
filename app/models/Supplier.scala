package models

import play.api.libs.json.Json

case class Supplier(id: Long, name: String, desc: String) {
  def patch(name: Option[String])
}

object Supplier {
  implicit val supplierFormat = Json.format[Supplier]
}
