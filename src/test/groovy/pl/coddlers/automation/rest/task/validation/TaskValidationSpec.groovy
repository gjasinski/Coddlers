package pl.coddlers.automation.rest.task.validation

import org.apache.commons.lang3.RandomStringUtils
import pl.coddlers.automation.CoddlersService
import pl.coddlers.automation.model.Course
import pl.coddlers.automation.model.Task
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class TaskValidationSpec extends Specification {

    @Shared coddlers = CoddlersService.asTeacher()
    @Shared Integer lessonId

    def setupSpec(){
        def course = Course.sample()
        coddlers.createCourse(course).successful()
        def courseId = coddlers.getCourses().getCourse(course.title, course.description).id
        def courseVersion = coddlers.getCourseVersion(courseId).last()

        def resp = coddlers.createLesson(courseId, courseVersion.versionNumber).successful()
        lessonId = retrieveLessonId(resp.location())
    }

    @Unroll
    def "Should NOT add task with #description title"(){
        when:
            def task = Task.sample(lessonId).withTitle(title)
            def resp = coddlers.createTask(lessonId, task)

        then:
            assert resp.code() == 400

        where:
            title                                       | description
            null                                        | 'null'
            ''                                          | 'empty'
            'a'                                         | 'too short'
            RandomStringUtils.randomAlphanumeric(101)   | 'too long'
    }

    def "Should NOT add task with too long description"(){
        given:
            def description = RandomStringUtils.randomAlphanumeric(256)

        when:
            def task = Task.sample(lessonId).withDescription(description)
            def resp = coddlers.createTask(lessonId, task)

        then:
            assert resp.code() == 400
    }

    def "Should NOT add task with null maxPoints"(){
        given:
            def maxPoints = null

        when:
            def task = Task.sample(lessonId).withMaxPoints(maxPoints)
            def resp = coddlers.createTask(lessonId, task)

        then:
           assert resp.code() == 400
    }

    def "Should NOT add task with null lessonId"(){
        given:
            def lessonIdNull = null

        when:
            def task = Task.sample(lessonIdNull)
            def resp = coddlers.createTask(lessonIdNull, task)

        then:
            assert resp.code() == 400
    }

    private retrieveLessonId(String location){
        (location =~ /lessons\/(\w*)/)[0][1] as Integer
    }
}