package service

import models.{Course, Student}

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
  def getStudent(document: String): Future[Student] = {
    studentRepository
      .getUserByDocument(document)
      .flatMap {
        case Some(student) =>
          Future.successful(student)
        case None =>
          Future.failed(OktanaAPIException("Student Not Found"))
      }
  }

  def getCourse(id: String): Future[Course] = {
    courseRepository
      .getCourseById(id)
      .flatMap {
        case Some(course) =>
          Future.successful(course)
        case None =>
          Future.failed(OktanaAPIException("Course Not Found"))
      }
  }
}
