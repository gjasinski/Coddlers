package pl.coddlers.automation.tests.rest.lesson

import pl.coddlers.automation.CoddlersService
import pl.coddlers.automation.model.Course
import pl.coddlers.automation.model.Lesson
import pl.coddlers.automation.model.response.Lesson as RespLesson
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static pl.coddlers.automation.Commons.retrieveLessonId

class LessonsSpec extends Specification {

    @Shared coddlers = CoddlersService.asTeacher()
    @Shared coddlersStudent = CoddlersService.asStudent()
    @Shared Integer courseId
    @Shared Integer courseVersionNumber

    def setupSpec(){
        def course = Course.sample()
        coddlers.createCourse(course).successful()
        courseId = coddlers.getCourses().getCourse(course.title, course.description).id
        courseVersionNumber = coddlers.getCourseVersion(courseId).last().versionNumber
    }

    def "Should create lesson as a Teacher"(){
        when:
            def resp = coddlers.createLesson(courseId, courseVersionNumber)

        then:
            assert resp.code() == 201

        when:
            def lessonId = retrieveLessonId(resp.location())

        then:
            assert lessonId >= 0
    }

    def "Should NOT create lesson as a Student"(){
        when:
            def resp = coddlersStudent.createLesson(courseId, courseVersionNumber)

        then:
            assert resp.code() == 403
    }

    @Unroll
    def "Should get lesson by id as a #who"(){
        when:
            def lesson = Lesson.sample(courseId, courseVersionNumber)
            def resp = coddlers.createLesson(courseId, courseVersionNumber, lesson).successful()
            def lessonId = retrieveLessonId(resp.location())

            def respLesson = service.getLesson(lessonId).asObject() as RespLesson

        then:
            assert respLesson.courseId == lesson.courseId
            assert respLesson.courseVersionNumber == lesson.courseVersionNumber
            assert respLesson.description == lesson.description
            assert respLesson.title == lesson.title
            assert respLesson.timeInDays == lesson.timeInDays
            assert respLesson.weight == lesson.weight

        where:
            service             | who
            coddlers            | 'Teacher'
            coddlersStudent     | 'Student'
    }

    def "Should update lesson as a Teacher"(){
        when: 'create lesson'
            def lesson = Lesson.sample(courseId, courseVersionNumber)
            def resp = coddlers.createLesson(courseId, courseVersionNumber, lesson)
            def lessonId = retrieveLessonId(resp.location())

        and: 'update lesson'
            def updatedLesson = new RespLesson(lessonId, courseId, courseVersionNumber,
                    'newDescription', 10, 'newTitle', 10)

            resp = coddlers.updateLesson(lessonId, updatedLesson)

        then:
            assert resp.code() == 200

        when:
            def respLesson = resp.asObject()

        then:
            assert respLesson.courseId == updatedLesson.courseId
            assert respLesson.courseVersionNumber == updatedLesson.courseVersionNumber
            assert respLesson.description == updatedLesson.description
            assert respLesson.title == updatedLesson.title
            assert respLesson.timeInDays == updatedLesson.timeInDays
            assert respLesson.weight == updatedLesson.weight
    }

    def "Should NOT update lesson as a Student"() {
        when: 'create lesson'
            def lesson = Lesson.sample(courseId, courseVersionNumber)
            def resp = coddlers.createLesson(courseId, courseVersionNumber, lesson).successful()
            def lessonId = retrieveLessonId(resp.location())

        and: 'update lesson'
            def updatedLesson = new RespLesson(lessonId, courseId, courseVersionNumber,
                    'newDescription', 10, 'newTitle', 10)

            resp = coddlersStudent.updateLesson(lessonId, updatedLesson)

        then:
            assert resp.code() == 403
    }
}
