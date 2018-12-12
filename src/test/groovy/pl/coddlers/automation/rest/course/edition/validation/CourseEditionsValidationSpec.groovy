package pl.coddlers.automation.rest.course.edition.validation

import org.apache.commons.lang3.RandomStringUtils
import pl.coddlers.automation.CoddlersService
import pl.coddlers.automation.model.Course
import pl.coddlers.automation.model.CourseEdition
import pl.coddlers.automation.model.response.CourseVersion
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class CourseEditionsValidationSpec extends Specification {

    @Shared coddlers = CoddlersService.asTeacher()
    @Shared Integer courseId
    @Shared CourseVersion courseVersion

    def setupSpec() {
        def course = Course.sample()
        coddlers.createCourse(course).successful()
        courseId = coddlers.getCourses().getCourse(course.title, course.description).id
        courseVersion = coddlers.getCourseVersion(courseId).last()
    }

    @Unroll
    def "Should NOT add course edition with #description title"(){
        when:
            def courseEdition = CourseEdition.sample(courseVersion).withTitle(title)
            def resp = coddlers.createCourseEdition(courseVersion, courseEdition)

        then:
           assert resp.code() == 400

        where:
            title                                       | description
            null                                        | 'null'
            ''                                          | 'empty'
            'a'                                         | 'too short'
            RandomStringUtils.randomAlphanumeric(101)   | 'too long'
    }

    def "Should NOT add course edition with null startDate"(){
        given:
            def startDate = null

        when:
            def courseEdition = CourseEdition.sample(courseVersion).withStartDate(startDate)
            def resp = coddlers.createCourseEdition(courseVersion, courseEdition)

        then:
            assert resp.code() == 400
    }

    def "Should NOT add course edition with null courseVersion"(){
        given:
            def courseVersionNull = null

        when:
            def courseEdition = CourseEdition.sample(courseVersion).withCourseVersion(courseVersionNull)
            def resp = coddlers.createCourseEdition(courseVersion, courseEdition)

        then:
            assert resp.code() == 400
    }
}