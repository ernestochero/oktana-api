package repository

import org.mongodb.scala.MongoCollection
trait Repository[T] {
  def collection: MongoCollection[T]
}
