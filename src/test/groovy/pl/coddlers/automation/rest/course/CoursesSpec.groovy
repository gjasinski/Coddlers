package pl.coddlers.automation.rest.course

import pl.coddlers.automation.CoddlersService
import pl.coddlers.automation.Commons
import pl.coddlers.automation.model.Course
import pl.coddlers.automation.model.response.Course as RespCourse
import spock.lang.PendingFeature
import spock.lang.Shared
import spock.lang.Specification

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

    @PendingFeature
    def "Should NOT create duplicated course"(){
        given:
            def course = Course.sample()

        when: "Create 1st time"
            def resp = coddlers.createCourse(course)

        then:
            assert resp.code() == 201

        when: "Create 2nd time"
            resp = coddlers.createCourse(course)

        then:
            assert resp.code() == 400
    }

    def "Should get course by id"(){
        given:
            def course = Course.sample()
            coddlers.createCourse(course).successful()

            def courseId = coddlers.getCourses().asList()
                    .find { it -> it.title == course.title && it.description == course.description }
                    .id as Integer

        when:
            def resp = coddlers.getCourse(courseId)

        then:
            assert resp.code() == 200
    }

    def "Should update course as a teacher"(){
        given:
            def course = Course.sample()
            coddlers.createCourse(course).successful()

            def courseId = coddlers.getCourses().asList()
                .find { it -> it.title == course.title && it.description == course.description }
                .id as Integer

        when:
            def title = Commons.uniqueName('newTitle')
            def description = Commons.uniqueName('newDescription')
            def updatedCourse = new RespCourse(courseId, title, description)
            def resp = coddlers.updateCourse(updatedCourse)

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
            coddlers.createCourse(course).successful()

            def courseId = coddlers.getCourses().asList()
                    .find { it -> it.title == course.title && it.description == course.description }
                    .id as Integer

        when:
            def title = Commons.uniqueName('newTitle')
            def description = Commons.uniqueName('newDescription')
            def updatedCourse = new RespCourse(courseId, title, description)
            def resp = coddlersStudent.updateCourse(updatedCourse)

        then:
            assert resp.code() == 403
    }
}