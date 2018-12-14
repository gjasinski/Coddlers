package pl.coddlers.automation

import com.google.gson.Gson
import groovy.transform.TupleConstructor
import pl.coddlers.automation.asserts.*
import pl.coddlers.automation.auth.Credentials
import pl.coddlers.automation.auth.User
import pl.coddlers.automation.model.*
import pl.coddlers.automation.model.response.Course as RespCourse
import pl.coddlers.automation.model.response.CourseVersion
import pl.coddlers.automation.model.response.Lesson as RespLesson
import pl.coddlers.automation.model.response.Task as RespTask

import static io.restassured.RestAssured.given

@TupleConstructor
class CoddlersService {

//    private static final CONTEXT = 'http://localhost:4200'
    private static final CONTEXT = 'http://coddlers.pl:8080'

    private static final ACCOUNT = '/api/account'
    private static final COURSES = '/api/courses'
    private static final COURSE_EDITIONS = '/api/editions'
    private static final INVITATION_LINK = '/api/editions/invitation-link'
    private static final INVITATIONS = '/invitations/emails'
    private static final COURSE_VERSION = '/api/course-versions'
    private static final REGISTER = '/api/account/register'
    private static final LOGIN = '/api/auth'
    private static final LESSON = '/api/lessons'
    private static final TASK = '/api/tasks'

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

    def getCourseEditions() {
        new CourseEditionsAssert(this.withToken()
                .get("${CONTEXT}${COURSE_EDITIONS}")
                .then()
                .extract()
                .response())
    }

    def getCourseEdition(Integer courseEditionId) {
        new CourseEditionAssert(this.withToken()
                .get("${CONTEXT}${COURSE_EDITIONS}/${courseEditionId}")
                .then()
                .extract()
                .response())
    }

    def createCourseEdition(CourseVersion courseVersion,
                            CourseEdition courseEdition = CourseEdition.sample(courseVersion)) {
        new Assert(this.withToken()
                .body(courseEdition)
                .post("${CONTEXT}${COURSE_EDITIONS}")
                .then()
                .extract()
                .response()
        )
    }

    def getCourseVersion(Integer courseId) {
        new CourseVersionsAssert(this.withToken()
                .get("${CONTEXT}${COURSE_VERSION}?courseId=${courseId}")
                .then()
                .extract()
                .response())
    }

    def createCourseVersion(RespCourse course) {
        new CourseVersionAssert(this.withToken()
                .body(course)
                .post("${CONTEXT}${COURSE_VERSION}")
                .then()
                .extract()
                .response())
    }

    def createLesson(Integer courseId, Integer courseVersionNumber,
                     Lesson lesson = Lesson.sample(courseId, courseVersionNumber)) {
        new Assert(this.withToken()
                .body(lesson)
                .post("${CONTEXT}${LESSON}")
                .then()
                .extract()
                .response())
    }

    def getLesson(Integer lessonId) {
        new LessonAssert(this.withToken()
                .get("${CONTEXT}${LESSON}/${lessonId}")
                .then()
                .extract()
                .response())
    }

    def updateLesson(Integer lessonId, RespLesson lesson) {
        new LessonAssert(this.withToken()
                .body(lesson)
                .put("${CONTEXT}${LESSON}/${lessonId}")
                .then()
                .extract()
                .response())
    }

    def createTask(Integer lessonId, Task task = Task.sample(lessonId)) {
        new Assert(this.withToken()
                .body(task)
                .post("${CONTEXT}${TASK}")
                .then()
                .extract()
                .response())
    }

    def getTasks(Integer lessonId) {
        new TasksAssert(this.withToken()
                .get("${CONTEXT}${TASK}?lessonId=${lessonId}")
                .then()
                .extract()
                .response())
    }

    def getTask(Integer taskId) {
        new TaskAssert(this.withToken()
                .get("${CONTEXT}${TASK}/${taskId}")
                .then()
                .extract()
                .response())
    }

    def updateTask(Integer taskId, RespTask task) {
        new TaskAssert(this.withToken()
                .body(task)
                .put("${CONTEXT}${TASK}/${taskId}")
                .then()
                .extract()
                .response())
    }

    def getInvitationLink(Integer courseEditionId) {
        new Assert(this.withToken()
                .get("${CONTEXT}${INVITATION_LINK}?courseEditionId=${courseEditionId}")
                .then()
                .extract()
                .response())
    }

    def sendInvite(InvitationLink invite) {
        new Assert(this.withToken()
                .body(invite)
                .post("${CONTEXT}${COURSE_EDITIONS}${INVITATIONS}")
                .then()
                .extract()
                .response())
    }

    private def withToken() {
        given().headers(this.headers)
    }

    static def makeInvitationLink() {
        "${CONTEXT.replace('http://', 'http://www.')}/#/invitations?invitationToken="
    }
}