package models
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
