package controllers

import javax.inject._
import models.Student
import play.api.mvc._
import play.api.libs.json._
import commons.Implicits._
import models.OktanaException.OktanaAPIException

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

  def createCourse: Action[AnyContent] = Action {
    Ok("createCourse")
  }

  def searchCourse(id: String): Action[AnyContent] = Action.async {
    service.OktanaService
      .getCourse(id)
      .map(c => Ok(Json.toJson(c)))
      .recoverWith(ex => errorHandler(ex))
  }

  def createStudent(student: Student): Action[AnyContent] = Action {
    Ok("student created")
  }

  def errorHandler(ex: Throwable): Future[Result] = ex match {
    case oktanaEx: OktanaAPIException =>
      Future.successful(
        InternalServerError(s"An error occurred : ${oktanaEx.message}")
      )
  }

  def searchStudent(id: String): Action[AnyContent] = Action.async { _ =>
    service.OktanaService
      .getStudent(id)
      .map(st => Ok(Json.toJson(st)))
      .recoverWith(ex => errorHandler(ex))
  }
}
