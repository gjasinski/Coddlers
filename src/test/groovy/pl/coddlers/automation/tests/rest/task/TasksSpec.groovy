package pl.coddlers.automation.tests.rest.task

import org.apache.commons.lang3.RandomUtils
import pl.coddlers.automation.CoddlersService
import pl.coddlers.automation.model.Course
import pl.coddlers.automation.model.Task
import pl.coddlers.automation.model.response.Task as RespTask
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static pl.coddlers.automation.Commons.retrieveLessonId
import static pl.coddlers.automation.Commons.retrieveTaskId

class TasksSpec extends Specification {

    @Shared coddlers = CoddlersService.asTeacher()
    @Shared coddlersStudent = CoddlersService.asStudent()
    @Shared Integer lessonId

    def setupSpec(){
        def course = Course.sample()
        coddlers.createCourse(course).successful()
        def courseId = coddlers.getCourses().getCourse(course.title, course.description).id
        def courseVersion = coddlers.getCourseVersion(courseId).last()

        def resp = coddlers.createLesson(courseId, courseVersion.versionNumber).successful()
        lessonId = retrieveLessonId(resp.location())
    }

    def "Should create task as a Teacher"(){
        when:
            def resp = coddlers.createTask(lessonId)

        then:
            assert resp.code() == 201

        when:
            def taskId = retrieveTaskId(resp.location())

        then:
            assert taskId >= 0
    }

    def "Should NOT create task as a Student"(){
        when:
            def resp = coddlersStudent.createTask(lessonId)

        then:
            assert resp.code() == 403
    }

    @Unroll
    def "Should create #description task"(){
        given:
            def task = Task.sample(lessonId).withIsCodeTask(isCodeTask)

        when:
            def resp = coddlers.createTask(lessonId, task)

        then:
            assert resp.code() == 201

        when:
            def taskId = retrieveTaskId(resp.location())

        then:
            assert taskId >= 0

        where:
            description     | isCodeTask
            'code'          | Boolean.TRUE
            'non-code'      | Boolean.FALSE
    }

    @Unroll
    def "Should get tasks as a #who"(){
        when:
            def resp = service.getTasks(lessonId)

        then:
            assert resp.code() == 200

        when:
            def tasks = resp.asList()

        then:
            assert tasks.size() >= 0

        where:
            service             | who
            coddlers            | 'Teacher'
            coddlersStudent     | 'Student'
    }

    @Unroll
    def "Should get task by id as a #who"(){
        when:
            def task = Task.sample(lessonId)
            def resp = coddlers.createTask(lessonId, task).successful()
            def taskId = retrieveTaskId(resp.location())
        
            def respTask = service.getTask(taskId).asObject() as RespTask

        then:
            assert respTask.isCodeTask == task.isCodeTask
            assert respTask.title == task.title
            assert respTask.description == task.description
            assert respTask.lessonId == task.lessonId
            assert respTask.maxPoints == task.maxPoints

        where:
            service             | who
            coddlers            | 'Teacher'
            coddlersStudent     | 'Student'
    }

    def "Should update task as a Teacher"(){
        when: 'create task'
            def task = Task.sample(lessonId)
            def resp = coddlers.createTask(lessonId, task).successful()
            def taskId = retrieveTaskId(resp.location()) as Integer

        and: 'update task'
            def updatedTask = new RespTask(taskId, lessonId, 'newTitle', 'newDescription',
            RandomUtils.nextInt(), RandomUtils.nextBoolean(), 'newBranchPrefix')

            resp = coddlers.updateTask(taskId, updatedTask)

        then:
            assert resp.code() == 200

        when: 'get updated task'
            def respTask = coddlers.getTask(taskId).asObject()

        then: 'check if task was properly updated'
            assert respTask.isCodeTask == updatedTask.isCodeTask
            assert respTask.title == updatedTask.title
            assert respTask.description == updatedTask.description
            assert respTask.lessonId == updatedTask.lessonId
            assert respTask.maxPoints == updatedTask.maxPoints
            assert respTask.branchNamePrefix == updatedTask.branchNamePrefix
    }

    def "Should NOT update task as a Student"() {
        when: 'create task'
            def task = Task.sample(lessonId)
            def resp = coddlers.createTask(lessonId, task).successful()
            def taskId = retrieveTaskId(resp.location()) as Integer

        and: 'update task'
            def updatedTask = new RespTask(taskId, lessonId, 'newTitle', 'newDescription',
                    RandomUtils.nextInt(), RandomUtils.nextBoolean(), 'newBranchPrefix')

            resp = coddlersStudent.updateTask(taskId, updatedTask)

        then:
            assert resp.code() == 403
    }
}