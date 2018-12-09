package pl.coddlers.automation.rest.course

import org.apache.commons.lang3.RandomStringUtils
import pl.coddlers.automation.CoddlersService
import pl.coddlers.automation.model.Course
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class CoursesValidationSpec extends Specification {

    @Shared coddlers = CoddlersService.asTeacher()

    @Unroll
    def "Should NOT add course with #description title"(){
        when:
            def course = new Course(title, 'test')
            def resp = coddlers.createCourse(course)

        then:
            assert resp.code() == 400

        where:
            title                                       | description
            null                                        | 'null'
            ''                                          | 'empty'
            'a'                                         | 'too short'
            RandomStringUtils.randomAlphanumeric(101)   | 'too long'
    }

    def "Should NOT add course with too long description"(){
        given:
            def description = RandomStringUtils.randomAlphanumeric(256)

        when:
            def course = new Course('title', description)
            def resp = coddlers.createCourse(course)

        then:
            assert resp.code() == 400
    }

    @Unroll
    def "Should add course with #testCaseDescription description"(){
        when:
            def course = new Course('title', description)
            def resp = coddlers.createCourse(course)

        then:
            assert resp.code() == 201

        where:
            description | testCaseDescription
            null        | 'null'
            ''          | 'empty'
    }
}