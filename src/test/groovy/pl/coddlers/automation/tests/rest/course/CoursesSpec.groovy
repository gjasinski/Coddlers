package pl.coddlers.automation.tests.rest.course

import pl.coddlers.automation.CoddlersService
import pl.coddlers.automation.model.Course
import pl.coddlers.automation.model.response.Course as RespCourse
import spock.lang.Shared
import spock.lang.Specification

import static pl.coddlers.automation.Commons.retrieveCourseId
import static pl.coddlers.automation.Commons.uniqueName

class CoursesSpec extends Specification {

    @Shared coddlers = CoddlersService.asTeacher()
    @Shared coddlersStudent = CoddlersService.asStudent()

    def "Should get courses as a Teacher"(){
        when:
            def resp = coddlers.getCourses()
            def courses = resp.asList()

        then:
            assert resp.code() == 200
            assert courses.size() >= 0
    }

    def "Should get courses as a Student"(){
        when:
            def resp = coddlersStudent.getCourses()
            def courses = resp.asList()

        then:
            assert resp.code() == 200
            assert courses.size() >= 0
    }

    def "Should create course as a Teacher"(){
        given:
            def before = coddlers.getCourses().asList().size()

        when:
            def resp = coddlers.createCourse()

        then:
            assert resp.code() == 201

        when:
            def after = coddlers.getCourses().asList().size()

        then:
            assert before == after - 1
    }

    def "Should NOT create course as a Student"(){
        when:
            def resp = coddlersStudent.createCourse()

        then:
            assert resp.code() == 403
    }

    def "Should get course by id"(){
        given:
            def course = Course.sample()
            def resp = coddlers.createCourse(course).successful()

            def courseId = retrieveCourseId(resp.location())

        when:
            resp = coddlers.getCourse(courseId)

        then:
            assert resp.code() == 200
    }

    def "Should update course as a teacher"(){
        given:
            def course = Course.sample()
            def resp = coddlers.createCourse(course).successful()

            def courseId = retrieveCourseId(resp.location())

        when:
            def title = uniqueName('newTitle')
            def description = uniqueName('newDescription')
            def updatedCourse = new RespCourse(courseId, title, description)
            resp = coddlers.updateCourse(updatedCourse)

        then:
            assert resp.code() == 200

        and:
            def respCourse = coddlers.getCourse(courseId).asObject() as RespCourse
            assert respCourse != null
            assert respCourse.title == title
            assert respCourse.description == description
    }

    def "Should NOT update course a a student"(){
        given:
            def course = Course.sample()
            def resp = coddlers.createCourse(course).successful()

            def courseId = retrieveCourseId(resp.location())

        when:
            def title = uniqueName('newTitle')
            def description = uniqueName('newDescription')
            def updatedCourse = new RespCourse(courseId, title, description)
            resp = coddlersStudent.updateCourse(updatedCourse)

        then:
            assert resp.code() == 403
    }
}