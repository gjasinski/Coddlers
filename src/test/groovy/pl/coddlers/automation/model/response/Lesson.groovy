package pl.coddlers.automation.model.response

class Lesson {
    Integer id
    Integer courseId
    Integer courseVersionNumber
    String description
    Integer timeInDays
    String title
    Integer weight

    Lesson(Integer id, Integer courseId, Integer courseVersionNumber, String description, Integer timeInDays, String title, Integer weight) {
        this.id = id
        this.courseId = courseId
        this.courseVersionNumber = courseVersionNumber
        this.description = description
        this.timeInDays = timeInDays
        this.title = title
        this.weight = weight
    }
}
