# oktana-api
## Overview
This is a challenge to create a REST API for managing students and their courses.
### Model Description 
#### Student
```scala
case class Student(document: String,
                   name: String,
                   lastName: String,
                   courses: List[String])
```
#### Course
```scala
case class Course(courseId: String, name: String)
```

#### OktanaResponse
```scala
sealed trait OktanaResponse {
  val responseCode: String
  val responseMessage: String
}
```

#### OktanaStudentSuccessResponse
```scala
case class OktanaStudentSuccessResponse(responseCode: String = "00",
                                        responseMessage: String,
                                        student: Student) extends OktanaResponse
```

#### OktanaCourseSuccessResponse
```scala
case class OktanaCourseSuccessResponse(responseCode: String = "01",
                                       responseMessage: String,
                                       course: Course) extends OktanaResponse
```
#### OktanaFailedResponse
```scala
case class OktanaFailedResponse(responseCode: String = "05",
                                responseMessage: String) extends OktanaResponse
```
#### note
we can inprove the success response type using generic types probably like this.
```scala
case class OktanaSuccessResponse[T](responseCode: String = "01",
                                       responseMessage: String,
                                       model: T) extends OktanaResponse
```

### Endoints
1\. Get a Student by the document.
```routes

GET    /api/student/:document                  controllers.HomeController.searchStudent(document)

```
example success response : 
```json
{
    "responseCode": "00",
    "responseMessage": "Success",
    "student": {
        "document": "1",
        "name": "Ernesto",
        "lastName": "Chero",
        "courses": [
            "1001",
            "1002"
        ]
    },
    "_type": "models.OktanaStudentSuccessResponse"
}
```

2\. Get a Course by courseId.
```routes

GET    /api/course/:courseId                   controllers.HomeController.searchCourse(courseId)

```
example success response : 
```json
{
    "responseCode": "01",
    "responseMessage": "Success",
    "course": {
        "courseId": "1001",
        "name": "course name 1001"
    },
    "_type": "models.OktanaCourseSuccessResponse"
}
```


3\. Create a course.
```routes

POST   /api/course               controllers.HomeController.createCourse

```
example success response :
```json
{
    "responseCode": "01",
    "responseMessage": "Course added successfully",
    "course": {
        "courseId": "1008",
        "name": "course name 1008"
    },
    "_type": "models.OktanaCourseSuccessResponse"
}
```


4\. Create a Student.
```routes

POST  /api/student               controllers.HomeController.createStudent

```

example success response :
```json
{
    "responseCode": "00",
    "responseMessage": "Student added successfully",
    "student": {
        "document": "3",
        "name": "John",
        "lastName": "Doe",
        "courses": [
            "1001",
            "1002"
        ]
    },
    "_type": "models.OktanaStudentSuccessResponse"
}
```

## Instructions to run project 
1\. clone project : `git@github.com:ernestochero/oktana-api.git`

2\. go into `docker` directory and execute `./run.sh` script to build `oktana-db` mongo container

3\. execute project : `sbt run` and go to `http://127.0.0.1:9000` or `http://localhost:9000` on your favorite browser in order to review that every works well.

4\. go into `postman` directory to download `oktana-api.postman_collection.json` and import it to postman. 
