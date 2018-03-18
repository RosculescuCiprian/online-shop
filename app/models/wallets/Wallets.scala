package models.wallets

import play.api.libs.json.{Json, OFormat}

case class Wallets(id: Long, clientId: Long, sumInWallet: Long)

object Wallets {
  implicit val walletsFormat: OFormat[Wallets] = Json.format[Wallets]
}
