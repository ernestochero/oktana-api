package repository

import models.Course
import models.OktanaException.OktanaAPIException
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.Document

import scala.concurrent.{ExecutionContext, Future}

case class CourseRepository(collection: MongoCollection[Course])(
  implicit ec: ExecutionContext
) extends Repository[Course] {
  def getCourseById(courseId: String): Future[Option[Course]] = {
    collection
      .find(Document("courseId" -> courseId))
      .toFuture()
      .recoverWith {
        case ex => Future.failed(OktanaAPIException(ex.getMessage))
      }
      .map(_.headOption)
  }
  private def registerCourse(course: Course): Future[Course] =
    collection
      .insertOne(course)
      .toFuture()
      .recoverWith {
        case ex => Future.failed(OktanaAPIException(ex.getMessage))
      }
      .map(_ => course)
}
