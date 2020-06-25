package models
object OktanaException {
  case class OktanaAPIException(message: String) extends Exception
}
