// 1. create user on dataBase
db.createUser(
    {
        user: "oktana_username",
        pwd: "oktana_password",
        roles: [
            {
                role: "readWrite",
                db: "oktana"
            }
        ]
    }
)

// 2. insert fake data to courses collection
var course_collection_name = "courses";
var courses_data_test = [
    {
        "courseId": "1001",
        "name" : "course name 1001"
    },
    {
        "courseId": "1002",
        "name" : "course name 1001"
    },
    {
        "courseId": "1003",
        "name" : "course name 1001"
    },
    {
        "courseId": "1004",
        "name" : "course name 1001"
    },
    {
        "courseId": "1005",
        "name" : "course name 1001"
    },
];
db.createCollection(course_collection_name);
courses_data_test.forEach(function (course) {
    db.getCollection(course_collection_name).insertOne(course);
    print("course " + course.courseId + " inserted");
});

// 3. insert fake data to student collection
var student_collection_name = "students";
var students_data_test = [
    {
        "document" : "1",
        "name" : "Ernesto",
        "lastName" : "Chero",
        "courses" : [
            "1001",
            "1002"
        ]
    },
    {
        "document" : "2",
        "name" : "Jose",
        "lastName" : "Diaz",
        "courses" : [
            "1001",
            "1005"
        ]
    }
];
db.createCollection(student_collection_name);
students_data_test.forEach(function (student) {
    db.getCollection(student_collection_name).insertOne(student);
})
