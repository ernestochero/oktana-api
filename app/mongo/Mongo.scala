package mongo

import com.typesafe.config.{Config, ConfigFactory}
import models.{Course, Student}
import org.bson.codecs.configuration.{CodecProvider, CodecRegistry}
import org.bson.codecs.configuration.CodecRegistries.{
  fromProviders,
  fromRegistries
}
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import org.mongodb.scala.bson.codecs.Macros
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY

object Mongo {
  lazy val studentCodecProvider: CodecProvider =
    Macros.createCodecProvider[Student]()
  lazy val courseCodecProvider: CodecProvider =
    Macros.createCodecProvider[Course]()
  lazy val config: Config = ConfigFactory.load()
  lazy val mongoClient: MongoClient = MongoClient(
    config.getString("mongo-conf.uri")
  )
  lazy val codecRegistry: CodecRegistry = fromRegistries(
    fromProviders(studentCodecProvider, courseCodecProvider),
    DEFAULT_CODEC_REGISTRY
  )
  lazy val database: MongoDatabase = mongoClient
    .getDatabase(config.getString("mongo-conf.database"))
    .withCodecRegistry(codecRegistry)

  lazy val studentCollection: MongoCollection[Student] =
    database.getCollection(config.getString("mongo-conf.students"))
  lazy val courseCollection: MongoCollection[Course] =
    database.getCollection(config.getString("mongo-conf.courses"))
}
