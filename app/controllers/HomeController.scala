package controllers

import javax.inject._
import models.{Course, EcommerceGuest, EmailOnly, Member, Student}
import play.api.mvc._
import play.api.libs.json._
import commons.Implicits._
import models.OktanaException.{OktanaAPIException, S3UploadApiException}
import service.OktanaService

import scala.concurrent.Future

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents)
    extends BaseController {

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def createCourse: Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson
    json.fold(
      Future.successful(InternalServerError("Impossible to parse body"))
    )(json => {
      OktanaService()
        .registerCourse(json.as[Course])
        .map(c => Ok(Json.toJson(c)))
        .recoverWith(ex => errorHandler(ex))
    })
  }

  def searchCourse(courseId: String): Action[AnyContent] = Action.async {
    OktanaService()
      .getCourse(courseId)
      .map(c => Ok(Json.toJson(c)))
      .recoverWith(ex => errorHandler(ex))
  }

  def evaluateMember(members: List[Option[Member]]): Option[Member] =
    members.find(_.isDefined).flatten

  def createMember: Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson
    json.fold(
      Future.successful(InternalServerError("Impossible to parse body"))
    )(json => {
      val emailOnly = json.asOpt[EmailOnly]
      val ecommerceGuest = json.asOpt[EcommerceGuest]
      val members = List(emailOnly, ecommerceGuest)
      val foundMember = evaluateMember(members)
      val memberF = foundMember.fold[Future[Result]](
        Future.failed(OktanaAPIException("Member not found"))
      ) {
        case em: EmailOnly =>
          println("this is email Only")
          //TODO: execute code when the object is EmailOnly
          uploadToS3(em).map(Ok(_))

        case ecm: EcommerceGuest =>
          println("this is ecommerce Guest")
          //TODO: execute code when the object is EcommerceGuest
          uploadToS3(ecm).map(Ok(_))
      }
      memberF
        .recoverWith(ex => errorHandler(ex))
    })
  }
  def uploadToS3(member: Member): Future[String] = {
    //call to s3 which might throw an exception
    Future.failed(S3UploadApiException("error from s3"))
  }

  def createStudent: Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson
    json.fold(
      Future.successful(InternalServerError("Impossible to parse body"))
    )(json => {
      OktanaService()
        .registerStudent(json.as[Student])
        .map(c => Ok(Json.toJson(c)))
        .recoverWith(ex => errorHandler(ex))
    })
  }

  def errorHandler(ex: Throwable): Future[Result] = ex match {
    case oktanaEx: OktanaAPIException =>
      Future.successful(
        InternalServerError(s"An error occurred : ${oktanaEx.message}")
      )
    case ex: S3UploadApiException =>
      Future.successful(InternalServerError(s"Error to upload from S3"))
    case _ =>
      Future.successful(InternalServerError(s"An error occurred"))
  }

  def searchStudent(document: String): Action[AnyContent] = Action.async { _ =>
    OktanaService()
      .getStudent(document)
      .map(st => Ok(Json.toJson(st)))
      .recoverWith(ex => errorHandler(ex))
  }
}
