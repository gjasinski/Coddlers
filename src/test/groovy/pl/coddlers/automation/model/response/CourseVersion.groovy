package pl.coddlers.automation.model.response

class CourseVersion {
    Integer id
    Integer versionNumber

    CourseVersion(Integer id, Integer versionNumber) {
        this.id = id
        this.versionNumber = versionNumber
    }
}
