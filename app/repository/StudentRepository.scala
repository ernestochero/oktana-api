package repository

import models.Student
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.Document
import scala.concurrent.{ExecutionContext, Future}
import models.OktanaException.OktanaAPIException
case class StudentRepository(collection: MongoCollection[Student])(
  implicit ec: ExecutionContext
) extends Repository[Student] {
  def getUserByDocument(document: String): Future[Option[Student]] = {
    collection
      .find(Document("document" -> document))
      .toFuture()
      .recoverWith {
        case ex =>
          Future.failed(OktanaAPIException(ex.getMessage))
      }
      .map(_.headOption)
  }
  def registerStudent(student: Student): Future[Student] =
    collection
      .insertOne(student)
      .toFuture()
      .recoverWith {
        case ex => Future.failed(OktanaAPIException(ex.getMessage))
      }
      .map(_ => student)

}
