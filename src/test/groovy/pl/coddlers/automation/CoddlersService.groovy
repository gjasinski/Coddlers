package pl.coddlers.automation

import com.google.gson.Gson
import groovy.transform.TupleConstructor
import pl.coddlers.automation.asserts.*
import pl.coddlers.automation.auth.Credentials
import pl.coddlers.automation.auth.User
import pl.coddlers.automation.model.Account
import pl.coddlers.automation.model.Course
import pl.coddlers.automation.model.CourseEdition
import pl.coddlers.automation.model.response.Course as RespCourse
import pl.coddlers.automation.model.response.CourseVersion

import static io.restassured.RestAssured.given

@TupleConstructor
class CoddlersService {

//    private static final CONTEXT = 'http://localhost:4200'
    private static final CONTEXT = 'http://coddlers.pl:8080'

    private static final ACCOUNT = '/api/account'
    private static final COURSES = '/api/courses'
    private static final COURSE_EDITIONS = '/api/editions'
    private static final COURSE_VERSION= '/api/course-versions'
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

    CoddlersService withHeaders(User user = this.user) {
        def bearerToken = this.authenticate(user).getToken()
        this.headers.put('Authorization', "Bearer ${bearerToken}")
        this.headers.put('Content-Type', 'application/json')
        this
    }

    def getAccount() {
        new AccountAssert(this.withToken()
                .get("${CONTEXT}${ACCOUNT}")
                .then()
                .extract()
                .response())
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

    def authenticate(User user = this.user) {
        new AuthAssert(given()
                .header('Content-Type', 'application/json')
                .body(new Gson().toJson(user))
                .post("${CONTEXT}${LOGIN}")
                .then()
                .extract()
                .response())
    }

    def getCourses() {
        new CoursesAssert(this.withToken()
                .get("${CONTEXT}${COURSES}")
                .then()
                .extract()
                .response())
    }

    def getCourse(Integer courseId) {
        new CourseAssert(this.withToken()
                .get("${CONTEXT}${COURSES}/${courseId}")
                .then()
                .extract()
                .response())
    }

    def createCourse(Course course = Course.sample()) {
        new Assert(this.withToken()
                .body(new Gson().toJson(course))
                .post("${CONTEXT}${COURSES}")
                .then()
                .extract()
                .response())
    }

    def updateCourse(RespCourse course) {
        new Assert(this.withToken()
                .body(new Gson().toJson(course))
                .put("${CONTEXT}${COURSES}")
                .then()
                .extract()
                .response())
    }

    def getCourseEditions(){
        new CourseEditionsAssert(this.withToken()
                .get("${CONTEXT}${COURSE_EDITIONS}")
                .then()
                .extract()
                .response())
    }

    def createCourseEdition(Integer courseId, CourseVersion courseVersion,
                            CourseEdition courseEdition = CourseEdition.sample(courseId, courseVersion)){
        new Assert(this.withToken()
                .body(courseEdition)
                .post("${CONTEXT}${COURSE_EDITIONS}")
                .then()
                .extract()
                .response()
        )
    }

    def getCourseVersion(Integer courseId){
        new CourseVersionsAssert(this.withToken()
                .get("${CONTEXT}${COURSE_VERSION}?courseId=${courseId}")
                .then()
                .extract()
                .response())
    }

    def createCourseVersion(RespCourse course){
        new CourseVersionAssert(this.withToken()
                .body(course)
                .post("${CONTEXT}${COURSE_VERSION}")
                .then()
                .extract()
                .response())
    }

    private def withToken() {
        given().headers(this.headers)
    }
}