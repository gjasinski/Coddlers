package pl.coddlers.automation.model.response

class CourseWithCourseEdition {
    Course course
    CourseEdition courseEdition
    Integer allTasks
    Integer submittedTasks
    Integer gradedTasks

    CourseWithCourseEdition(Course course, CourseEdition courseEdition, Integer allTasks, Integer submittedTasks, Integer gradedTasks) {
        this.course = course
        this.courseEdition = courseEdition
        this.allTasks = allTasks
        this.submittedTasks = submittedTasks
        this.gradedTasks = gradedTasks
    }
}
