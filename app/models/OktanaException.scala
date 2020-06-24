package models

import play.api.libs.json.{Json, OWrites, Reads}

object OktanaException {
  case class OktanaAPIException(message: String) extends Exception
  implicit val oktanaAPIExceptionWrites: OWrites[OktanaAPIException] =
    Json.writes[OktanaAPIException]
  implicit val oktanaAPIExceptionReads: Reads[OktanaAPIException] =
    Json.reads[OktanaAPIException]
}
