package models

import play.api.libs.json._
sealed trait OktanaResponse {
  val responseCode: String
  val responseMessage: String
}
object OktanaResponse {
  implicit def oktanaFailedResponseWrites = Json.writes[OktanaFailedResponse]
  implicit def oktanaFailedResponseReads = Json.reads[OktanaFailedResponse]
  implicit def studentResponseWriter: OWrites[OktanaStudentSuccessResponse] =
    Json.writes[OktanaStudentSuccessResponse]
  implicit def studentResponseReads = Json.reads[OktanaStudentSuccessResponse]
  implicit def courseResponseWriter = Json.writes[OktanaCourseSuccessResponse]
  implicit def courseResponseReads = Json.reads[OktanaCourseSuccessResponse]
  implicit val format: OFormat[OktanaResponse] = Json.format[OktanaResponse]
}

case class OktanaStudentSuccessResponse(responseCode: String = "00",
                                        responseMessage: String,
                                        student: Student)
    extends OktanaResponse
case class OktanaCourseSuccessResponse(responseCode: String = "01",
                                       responseMessage: String,
                                       course: Course)
    extends OktanaResponse

case class OktanaFailedResponse(responseCode: String = "05",
                                responseMessage: String)
    extends OktanaResponse
