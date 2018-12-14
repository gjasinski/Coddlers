package pl.coddlers.automation.tests.rest.course.version

import pl.coddlers.automation.CoddlersService
import pl.coddlers.automation.model.Course
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
class CourseVersionSpec extends Specification {

    @Shared coddlers = CoddlersService.asTeacher()
    @Shared coddlersStudent = CoddlersService.asStudent()
    @Shared Integer courseId

    def setupSpec(){
        def course = Course.sample()
        coddlers.createCourse(course).successful()
        courseId = coddlers.getCourses().getCourse(course.title, course.description).id
    }

    def "Should get course version as a Teacher"(){
        when:
            def resp = coddlers.getCourseVersion(courseId)

        then:
            assert resp.code() == 200

        when:
            def courseVersions = resp.asList()

        then:
            assert courseVersions.size() >= 1

        when:
            def courseVersion = courseVersions.last()

        then:
            assert courseVersion.versionNumber == 1
    }

    def "Should NOT get course version as Student"(){
        when:
            def resp = coddlersStudent.getCourseVersion(courseId)

        then:
           assert resp.code() == 403
    }

    def "Should add course version as a Teacher"(){
        given:
            def course = coddlers.getCourse(courseId).asObject()

        when:
            def resp = coddlers.createCourseVersion(course)

        then:
            assert resp.code() == 200

        when:
            def courseVersion = coddlers.getCourseVersion(courseId).asList()
                                    .toSorted { cv1, cv2 -> cv1.versionNumber <=> cv2.versionNumber}
                                    .last()

        then:
            assert courseVersion.versionNumber == 2
    }

    def "Should NOT add course version as a Student"(){
        given:
            def course = coddlers.getCourse(courseId).asObject()

        when:
            def resp = coddlersStudent.createCourseVersion(course)

        then:
            assert resp.code() == 403
    }
}