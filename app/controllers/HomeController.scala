package controllers

import javax.inject._
import models.{Course, Student}
import play.api.mvc._
import play.api.libs.json._
import commons.Implicits._
import models.OktanaException.OktanaAPIException
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

  def searchCourse(id: String): Action[AnyContent] = Action.async {
    OktanaService()
      .getCourse(id)
      .map(c => Ok(Json.toJson(c)))
      .recoverWith(ex => errorHandler(ex))
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
    case _ =>
      Future.successful(InternalServerError(s"An error occurred"))
  }

  def searchStudent(id: String): Action[AnyContent] = Action.async { _ =>
    OktanaService()
      .getStudent(id)
      .map(st => Ok(Json.toJson(st)))
      .recoverWith(ex => errorHandler(ex))
  }
}
