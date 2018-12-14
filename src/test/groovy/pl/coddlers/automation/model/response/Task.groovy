package pl.coddlers.automation.model.response

class Task {
    Long id
    Long lessonId
    String title
    String description
    Integer maxPoints
    Boolean isCodeTask
    String branchNamePrefix

    Task(Long id, Long lessonId, String title, String description, Integer maxPoints, Boolean isCodeTask, String branchNamePrefix) {
        this.id = id
        this.lessonId = lessonId
        this.title = title
        this.description = description
        this.maxPoints = maxPoints
        this.isCodeTask = isCodeTask
        this.branchNamePrefix = branchNamePrefix
    }
}