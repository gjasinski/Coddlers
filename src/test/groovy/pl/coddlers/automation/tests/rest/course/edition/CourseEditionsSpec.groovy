package pl.coddlers.automation.tests.rest.course.edition

import pl.coddlers.automation.CoddlersService
import pl.coddlers.automation.Commons
import pl.coddlers.automation.model.Course
import pl.coddlers.automation.model.CourseEdition
import pl.coddlers.automation.model.response.CourseVersion
import spock.lang.Shared
import spock.lang.Specification

import static pl.coddlers.automation.Commons.*

class CourseEditionsSpec extends Specification {

    @Shared coddlers = CoddlersService.asTeacher()
    @Shared coddlersStudent = CoddlersService.asStudent()
    @Shared Integer courseId
    @Shared CourseVersion courseVersion

    def setupSpec() {
        def course = Course.sample()
        coddlers.createCourse(course).successful()
        courseId = coddlers.getCourses().getCourse(course.title, course.description).id
        courseVersion = coddlers.getCourseVersion(courseId).last()
    }

    def "Should NOT get course editions as a Teacher"(){
        when:
            def resp = coddlers.getCourseEditions()

        then:
            assert resp.code() == 403
    }

    def "Should get course editions as a Student"(){
        when:
            def resp = coddlersStudent.getCourseEditions()

        then:
            assert resp.code() == 200

        when:
            def courseEditions = resp.asList()

        then:
            assert courseEditions.size() >= 0
    }

    def "Should create course edition as a Teacher"(){
        when:
            def resp = coddlers.createCourseEdition(courseVersion)
            courseVersion = coddlers.getCourseVersion(courseId).last()

        then:
            assert resp.code() == 201
    }

    def "Should NOT create course edition as a Student"(){
        when:
            def resp = coddlersStudent.createCourseEdition(courseVersion)

        then:
            assert resp.code() == 403
    }

    def "Should get invitation link as a Teacher"(){
        given:
            def courseEdition = CourseEdition.sample(courseVersion)
            def resp = coddlers.createCourseEdition(courseVersion, courseEdition)
            courseVersion = coddlers.getCourseVersion(courseId).last()
            def courseEditionId = retrieveCourseEditionId(resp.location())

        when:
            resp = coddlers.getInvitationLink(courseEditionId)

        then:
            assert resp.code() == 200

        when:
            def invitationLink = resp.parse().link as String

        then:
            assert invitationLink.contains(coddlers.makeInvitationLink())
            assert retrieveInvitationToken(invitationLink).length() >= 0
    }

    def "Should NOT get invitation link as a Student"(){
        given:
            def courseEdition = CourseEdition.sample(courseVersion)
            def resp = coddlers.createCourseEdition(courseVersion, courseEdition)
            courseVersion = coddlers.getCourseVersion(courseId).last()
            def courseEditionId = retrieveCourseEditionId(resp.location())

        when:
            resp = coddlersStudent.getInvitationLink(courseEditionId)

        then:
            assert resp.code() == 403
    }
}