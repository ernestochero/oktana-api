package service

import models.{
  Course,
  OktanaFailedResponse,
  OktanaCourseSuccessResponse,
  OktanaStudentSuccessResponse,
  OktanaResponse,
  Student
}

import scala.concurrent.Future
import repository._
import commons.Implicits._
import models.OktanaException.OktanaAPIException
import mongo.Mongo.{courseCollection, studentCollection}
object OktanaService {
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
            OktanaFailedResponse(responseMessage = "Student Not Found")
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
              responseMessage = s"Student ${s.document} already exist"
            )
          )
        case None =>
          studentRepository
            .registerStudent(student)
            .map(
              _ =>
                OktanaStudentSuccessResponse(
                  responseMessage = "Student added successfully",
                  student = student
              )
            )
      }

  def getCourse(id: String): Future[OktanaResponse] = {
    courseRepository
      .getCourseById(id)
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
            OktanaFailedResponse(responseMessage = "Course Not Found")
          )
      }
  }

  def registerCourse(course: Course): Future[OktanaResponse] =
    courseRepository.getCourseById(course.courseId).flatMap {
      case Some(c) =>
        Future.successful(
          OktanaFailedResponse(
            responseMessage = s"Course ${c.courseId} already exist"
          )
        )
      case None =>
        courseRepository
          .registerCourse(course)
          .map(
            _ =>
              OktanaCourseSuccessResponse(
                responseMessage = "Course added successfully",
                course = course
            )
          )
    }
}
