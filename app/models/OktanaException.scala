package models
object OktanaException {
  case class OktanaAPIException(message: String) extends Exception
  case class S3UploadApiException(messages: String) extends Exception
}
