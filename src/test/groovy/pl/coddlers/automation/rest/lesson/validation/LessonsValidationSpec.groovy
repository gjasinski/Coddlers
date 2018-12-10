package pl.coddlers.automation.rest.lesson.validation

import org.apache.commons.lang3.RandomStringUtils
import pl.coddlers.automation.CoddlersService
import pl.coddlers.automation.model.Course
import pl.coddlers.automation.model.Lesson
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class LessonsValidationSpec extends Specification {

    @Shared coddlers = CoddlersService.asTeacher()
    @Shared Integer courseId
    @Shared Integer courseVersionNumber

    def setupSpec(){
        def course = Course.sample()
        coddlers.createCourse(course).successful()
        courseId = coddlers.getCourses().getCourse(course.title, course.description).id
        courseVersionNumber = coddlers.getCourseVersion(courseId).last().versionNumber
    }

    @Unroll
    def "Should NOT add lesson with #description title"(){
        when:
            def lesson = new Lesson(courseId, courseVersionNumber, 'dummyDescription', 3, title, 3)
            def resp = coddlers.createLesson(courseId, courseVersionNumber, lesson)

        then:
            assert resp.code() == 400

        where:
            title                                       | description
            null                                        | 'null'
            ''                                          | 'empty'
            'a'                                         | 'too short'
            RandomStringUtils.randomAlphanumeric(101)   | 'too long'
    }

    def "Should NOT add lesson with too long description"(){
        given:
            def description = RandomStringUtils.randomAlphanumeric(256)

        when:
            def lesson = new Lesson(courseId, courseVersionNumber, description, 3, 'dummyTitle', 3)
            def resp = coddlers.createLesson(courseId, courseVersionNumber, lesson)

        then:
            assert resp.code() == 400
    }

    def "Should NOT add lesson with null weight"(){
        given:
            def weight = null

        when:
            def lesson = new Lesson(courseId, courseVersionNumber, 'dummyDescription', 3, 'dummyTitle', weight)
            def resp = coddlers.createLesson(courseId, courseVersionNumber, lesson)

        then:
            assert resp.code() == 400
    }

    def "Should NOT add lesson with null timeInDays"(){
        given:
            def timeInDays = null

        when:
            def lesson = new Lesson(courseId, courseVersionNumber, 'dummyDescription', timeInDays, 'dummyTitle', 3)
            def resp = coddlers.createLesson(courseId, courseVersionNumber, lesson)

        then:
            assert resp.code() == 400
    }

    def "Should NOT add lesson with null courseId"(){
        given:
            def courseIdNull = null

        when:
            def lesson = new Lesson(courseIdNull as Integer, courseVersionNumber, 'dummyDescription', 3, 'dummyTitle', 3)
            def resp = coddlers.createLesson(courseId, courseVersionNumber, lesson)

        then:
            assert resp.code() == 400
    }

    def "Should NOT add lesson with null Integer courseVersionNumber"(){
        given:
            def courseVersionNumberNull = null

        when:
            def lesson = new Lesson(courseId, courseVersionNumberNull as Integer, 'dummyDescription', 3, 'dummyTitle', 3)
            def resp = coddlers.createLesson(courseId, courseVersionNumber, lesson)

        then:
            assert resp.code() == 400
    }
}
