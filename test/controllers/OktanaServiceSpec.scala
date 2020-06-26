package controllers
import models._
import service.OktanaService
import org.mockito.Mockito._
import OktanaMessages._
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
class OktanaServiceSpec extends HelperTest {
  behavior of "OktanaServiceSpec"
  "getCourse function" should
    "return OktanaCourseSuccessResponse type" in {
    val oktanaServiceMock = mock[OktanaService]
    val courseId = "1"
    val courseMock: Course = Course("1", "fake course")
    when(oktanaServiceMock.getCourse(courseId))
      .thenReturn(
        Future.successful(
          OktanaCourseSuccessResponse(
            responseMessage = addedSuccessfullyMessage("Course"),
            course = courseMock
          )
        )
      )
    val eventualResult: Future[OktanaResponse] =
      oktanaServiceMock.getCourse("1")

    val result = Await.result(eventualResult, 3.seconds)
    result.responseMessage mustBe "Course added successfully"
    result.responseCode mustBe "01"
    result.isInstanceOf[OktanaCourseSuccessResponse] mustBe true
  }

  "getStudent function" should
    "return OktanaStudentSuccessResponse type" in {
    val oktanaServiceMock = mock[OktanaService]
    val studentId = "1"
    val student = Student(
      document = "1",
      name = "Ernesto",
      lastName = "Rodriguez",
      courses = List("1001", "1002")
    )
    when(oktanaServiceMock.getStudent(studentId))
      .thenReturn(
        Future.successful(
          OktanaStudentSuccessResponse(
            responseMessage = addedSuccessfullyMessage("Student"),
            student = student
          )
        )
      )
    val eventualResult = oktanaServiceMock.getStudent(studentId)
    val result = Await.result(eventualResult, 3.seconds)

    result.isInstanceOf[OktanaStudentSuccessResponse] mustBe true
    result.responseCode mustBe "00"
    result.responseMessage mustBe "Student added successfully"
  }

  "getStudent function" should
    "return OktanaFailedResponse type when the student does not exist" in {
    val oktanaServiceMock = mock[OktanaService]
    val document = "1"
    when(oktanaServiceMock.getStudent(document))
      .thenReturn(
        Future.successful(
          OktanaFailedResponse(
            responseMessage = notFoundMessage(s"Student $document")
          )
        )
      )

    val eventualResult = oktanaServiceMock.getStudent(document)
    val result = Await.result(eventualResult, 3.seconds)
    result.isInstanceOf[OktanaFailedResponse] mustBe true
    result.responseCode mustBe "05"
    result.responseMessage mustBe "Student 1 not found"
  }

  "getCourse function" should
    "return OktanaFailedResponse type when the course does not exist" in {
    val oktanaServiceMock = mock[OktanaService]
    val courseId = "1"
    when(oktanaServiceMock.getCourse(courseId))
      .thenReturn(
        Future.successful(
          OktanaFailedResponse(
            responseMessage = notFoundMessage(s"Course $courseId")
          )
        )
      )

    val eventualResult = oktanaServiceMock.getCourse(courseId)
    val result = Await.result(eventualResult, 3.seconds)
    result.isInstanceOf[OktanaFailedResponse] mustBe true
    result.responseCode mustBe "05"
    result.responseMessage mustBe "Course 1 not found"
  }

  "registerCourse function" should
    "return OktanaFailedResponse type when the course already exist" in {
    val oktanaServiceMock = mock[OktanaService]
    val courseMock: Course = Course("1", "fake course")
    when(oktanaServiceMock.registerCourse(courseMock))
      .thenReturn(
        Future.successful(
          OktanaFailedResponse(
            responseMessage = alreadyExists(s"Course ${courseMock.courseId}")
          )
        )
      )
    val eventualResult = oktanaServiceMock.registerCourse(courseMock)
    val result = Await.result(eventualResult, 3.seconds)
    result.isInstanceOf[OktanaFailedResponse] mustBe true
    result.responseCode mustBe "05"
    result.responseMessage mustBe s"Course 1 already exist"
  }

  "registerStudent function" should
    "return OktanaFailedResponse type when the student already exist" in {
    val oktanaServiceMock = mock[OktanaService]
    val studentMock = Student(
      document = "1",
      name = "Ernesto",
      lastName = "Rodriguez",
      courses = List("1001", "1002")
    )
    when(oktanaServiceMock.registerStudent(studentMock))
      .thenReturn(
        Future.successful(
          OktanaFailedResponse(
            responseMessage = alreadyExists(s"Student ${studentMock.document}")
          )
        )
      )
    val eventualResult = oktanaServiceMock.registerStudent(studentMock)
    val result = Await.result(eventualResult, 3.seconds)
    result.isInstanceOf[OktanaFailedResponse] mustBe true
    result.responseCode mustBe "05"
    result.responseMessage mustBe s"Student 1 already exist"
  }

  "registerStudent function" should
    "return OktanaStudentSuccessResponse type when save the student without errors" in {
    val oktanaServiceMock = mock[OktanaService]
    val studentMock = Student(
      document = "1",
      name = "Ernesto",
      lastName = "Rodriguez",
      courses = List("1001", "1002")
    )
    when(oktanaServiceMock.registerStudent(studentMock))
      .thenReturn(
        Future.successful(
          OktanaStudentSuccessResponse(
            responseMessage = addedSuccessfullyMessage("Student"),
            student = studentMock
          )
        )
      )
    val eventualResult = oktanaServiceMock.registerStudent(studentMock)
    val result = Await.result(eventualResult, 3.seconds)
    result.isInstanceOf[OktanaStudentSuccessResponse] mustBe true
    result.responseCode mustBe "00"
    result.responseMessage mustBe s"Student added successfully"
  }

  "registerCourse function" should
    "return OktanaCourseSuccessResponse type when save the course without errors" in {
    val oktanaServiceMock = mock[OktanaService]
    val courseMock: Course = Course("1", "fake course")
    when(oktanaServiceMock.registerCourse(courseMock))
      .thenReturn(
        Future.successful(
          OktanaCourseSuccessResponse(
            responseMessage = addedSuccessfullyMessage("Course"),
            course = courseMock
          )
        )
      )
    val eventualResult = oktanaServiceMock.registerCourse(courseMock)
    val result = Await.result(eventualResult, 3.seconds)
    result.isInstanceOf[OktanaCourseSuccessResponse] mustBe true
    result.responseCode mustBe "01"
    result.responseMessage mustBe s"Course added successfully"
  }

}
