package pl.coddlers.automation.model

class CourseVersion {
    Integer id
    Integer versionNumber

    CourseVersion(Integer courseId, Integer versionNumber) {
        this.id = courseId
        this.versionNumber = versionNumber
    }

    static CourseVersion sample(Integer courseId, Integer versionNumber = 1){
        new CourseVersion(courseId, versionNumber)
    }
}
