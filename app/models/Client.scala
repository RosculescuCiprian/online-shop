package models

import play.api.libs.json._

case class Client(id: Long, email: String, age: Int) {
  def patch(name: Option[String] = None, age: Option[Int] = None): Client = {
    return this.copy(
      email = name.getOrElse(this.email),
      age = age.getOrElse(this.age)
    )
  }
}

object Client {
  implicit val clientFormat = Json.format[Client]
}
