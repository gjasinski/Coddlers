package pl.coddlers.automation.tests.e2e

import pl.coddlers.automation.CoddlersService
import pl.coddlers.automation.Commons
import pl.coddlers.automation.auth.User
import pl.coddlers.automation.model.*
import spock.lang.Specification

import java.sql.Timestamp
import java.time.Instant

import static pl.coddlers.automation.Commons.*

class TeacherHappyPathSpec extends Specification{

    def "Happy path Teacher flow"(){
        given:
            def coddlers = new CoddlersService()
            def account = new Account('Jan', 'Kowalski', 'zaq1@WSX', "${UUID.randomUUID()}@coddlers.pl", ['ROLE_TEACHER'])
            def course = new Course('Programowanie obiektowe', 'Programowanie obiektowe w jezyku Java')

        when:'1 step: register'
            def registerResp = coddlers.register(account)
            assert registerResp.code() == 201

        and: '2 step: login'
            coddlers = new CoddlersService(new User(account.userMail, account.password)).withHeaders()

        and: '3 step: create course'
            def createCourseResp = coddlers.createCourse(course)
            assert createCourseResp.code() == 201

        and: '4 step: create lesson'
            def courseId = retrieveCourseId(createCourseResp.location())
            def courseVersion = coddlers.getCourseVersion(courseId).last()
            def courseVersionNumber = courseVersion.versionNumber
            def lesson = new Lesson(courseId, courseVersionNumber, 'Implementacja prostego programu', 3, 'Hello world', 10)

            def createLessonResp = coddlers.createLesson(courseId, courseVersionNumber, lesson)
            assert createLessonResp.code() == 201

        and: '5 step: create task'
            def lessonId = retrieveLessonId(createLessonResp.location())
            def task = new Task(lessonId, 'Stworzenie pliku tekstowego', 'Stworz plik zad1.java', 5, true)

            def createTaskResp = coddlers.createTask(lessonId, task)
            assert createTaskResp.code() == 201

        and: '6 step: create course edition'
            def courseEdition = new CourseEdition('Edycja 1', courseVersion, Timestamp.from(Instant.now()))

            def createCourseEditionResp = coddlers.createCourseEdition(courseVersion, courseEdition)
            assert createCourseEditionResp.code() == 201

        then: '7 step: send invitation'
            def courseEditionId = retrieveCourseEditionId(createCourseEditionResp.location())
            def invitationToken = coddlers.getInvitationLink(courseEditionId).parse().link as String

            def invite = new InvitationLink(invitationToken, 'gjasinski95@gmail.com')
            def invitationResp = coddlers.sendInvite(invite)
            assert invitationResp.code() == 200
    }
}