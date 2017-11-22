package models

import play.api.libs.json._

case class Client(id: Long, name: String, age: Int)

object Client {
  implicit val personFormat = Json.format[Client]
}
