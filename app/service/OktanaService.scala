package service

import models._
import scala.concurrent.Future
import repository._
import commons.Implicits._
import mongo.Mongo.{courseCollection, studentCollection}
import OktanaMessages._
trait OktanaServiceOperation {
  def getStudent(document: String): Future[OktanaResponse]
  def getCourse(id: String): Future[OktanaResponse]
  def registerStudent(student: Student): Future[OktanaResponse]
  def registerCourse(course: Course): Future[OktanaResponse]
}
object OktanaService {
  private val oktanaService = new OktanaService()
  def apply(): OktanaService = oktanaService
}
class OktanaService extends OktanaServiceOperation {
  private val studentRepository: StudentRepository = StudentRepository(
    studentCollection
  )
  private val courseRepository: CourseRepository = CourseRepository(
    courseCollection
  )
  def getStudent(document: String): Future[OktanaResponse] = {
    studentRepository
      .getUserByDocument(document)
      .flatMap {
        case Some(student) =>
          Future.successful(
            OktanaStudentSuccessResponse(
              responseMessage = "Success",
              student = student
            )
          )
        case None =>
          Future.successful(
            OktanaFailedResponse(
              responseMessage = notFoundMessage(s"student $document")
            )
          )
      }
  }
  def registerStudent(student: Student): Future[OktanaResponse] =
    studentRepository
      .getUserByDocument(student.document)
      .flatMap {
        case Some(s) =>
          Future.successful(
            OktanaFailedResponse(
              responseMessage = alreadyExists(s"Student ${s.document}")
            )
          )
        case None =>
          studentRepository
            .registerStudent(student)
            .map(
              _ =>
                OktanaStudentSuccessResponse(
                  responseMessage = addedSuccessfullyMessage("Student"),
                  student = student
              )
            )
      }

  def getCourse(courseId: String): Future[OktanaResponse] = {
    courseRepository
      .getCourseById(courseId)
      .flatMap {
        case Some(course) =>
          Future.successful(
            OktanaCourseSuccessResponse(
              responseMessage = "Success",
              course = course
            )
          )
        case None =>
          Future.successful(
            OktanaFailedResponse(
              responseMessage = notFoundMessage(s"Course $courseId")
            )
          )
      }
  }

  def registerCourse(course: Course): Future[OktanaResponse] =
    courseRepository.getCourseById(course.courseId).flatMap {
      case Some(c) =>
        Future.successful(
          OktanaFailedResponse(
            responseMessage = alreadyExists(s"Course ${c.courseId}")
          )
        )
      case None =>
        courseRepository
          .registerCourse(course)
          .map(
            _ =>
              OktanaCourseSuccessResponse(
                responseMessage = addedSuccessfullyMessage("Course"),
                course = course
            )
          )
    }
}
