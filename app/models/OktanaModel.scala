package models
import java.util.Date

import play.api.libs.json._

case class Student(document: String,
                   name: String,
                   lastName: String,
                   courses: List[String])
object Student {
  implicit val studentWrites: OWrites[Student] = Json.writes[Student]
  implicit val studentReads: Reads[Student] = Json.reads[Student]
}

case class Course(courseId: String, name: String)
object Course {
  implicit val courseWrites: OWrites[Course] = Json.writes[Course]
  implicit val courseReads: Reads[Course] = Json.reads[Course]
}

trait Member
case class EmailOnly(email: String, memberType: String) extends Member
object EmailOnly {
  implicit val emailOnlyWrites = Json.writes[EmailOnly]
  implicit val emailOnlyReads = Json.reads[EmailOnly]
}

case class EcommerceGuest(name: String,
                          email: Option[String],
                          dateOfBirth: Option[Date],
                          memberType: String)
    extends Member
object EcommerceGuest {
  implicit val ecommerceGuestWrites = Json.writes[EcommerceGuest]
  implicit val ecommerceGuestReads = Json.reads[EcommerceGuest]
}
