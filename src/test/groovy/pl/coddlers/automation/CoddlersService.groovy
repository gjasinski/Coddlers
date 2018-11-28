package pl.coddlers.automation

import com.google.gson.Gson
import groovy.transform.TupleConstructor
import pl.coddlers.automation.asserts.Assert
import pl.coddlers.automation.asserts.CourseAssert
import pl.coddlers.automation.asserts.CoursesAssert
import pl.coddlers.automation.auth.Credentials
import pl.coddlers.automation.auth.User
import pl.coddlers.automation.model.Account
import pl.coddlers.automation.model.Course

import static io.restassured.RestAssured.given

@TupleConstructor
class CoddlersService {

//    private static final CONTEXT = 'http://localhost:4200'
    private static final CONTEXT = 'http://coddlers.pl:8080'

    private static final COURSES = '/api/courses'
    private static final REGISTER = '/api/account/register'
    private static final LOGIN = '/api/auth'

    User user
    Map<String, String> headers = [:]


    static CoddlersService asStudent() {
        new CoddlersService(Credentials.student()).withHeaders()
    }

    static CoddlersService asTeacher() {
        new CoddlersService(Credentials.teacher()).withHeaders()
    }

    CoddlersService withHeaders() {
        def bearerToken = this.authenticate().parse().token as String
        this.headers.put('Authorization', "Bearer ${bearerToken}")
        this.headers.put('Content-Type', 'application/json')
        this
    }

    def register(Account account) {
        new Assert(given()
                .header('Content-Type', 'application/json')
                .body(new Gson().toJson(account))
                .post("${CONTEXT}${REGISTER}")
                .then()
                .extract()
                .response())
    }

    def authenticate() {
        new Assert(given()
                .header('Content-Type', 'application/json')
                .body(new Gson().toJson(this.user))
                .post("${CONTEXT}${LOGIN}")
                .then()
                .extract()
                .response())
    }

    def getCourses() {
        new CoursesAssert(this.withToken()
                .get("${CONTEXT}${COURSES}")
                .then().extract().response())
    }

    def getCourse(Integer courseId) {
        new CourseAssert(this.withToken()
                .get("${CONTEXT}${COURSES}/${courseId}")
                .then().extract().response())
    }

    def createCourse(Course course = Course.sample()) {
        new Assert(this.withToken()
                .body(new Gson().toJson(course))
                .post("${CONTEXT}${COURSES}")
                .then()
                .extract()
                .response())
    }

    def updateCourse(Course course) {
        new Assert(this.withToken()
                .body(new Gson().toJson(course))
                .put("${CONTEXT}${COURSES}")
                .then()
                .extract()
                .response())
    }


    private def withToken() {
        given().headers(this.headers)
    }
}