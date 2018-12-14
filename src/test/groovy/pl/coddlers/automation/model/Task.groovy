package pl.coddlers.automation.model

import org.apache.commons.lang3.RandomUtils
import pl.coddlers.automation.Commons

class Task {
    Long lessonId
    String title
    String description
    Integer maxPoints
    Boolean isCodeTask

    Task(Long lessonId, String title, String description, Integer maxPoints, Boolean isCodeTask) {
        this.lessonId = lessonId
        this.title = title
        this.description = description
        this.maxPoints = maxPoints
        this.isCodeTask = isCodeTask
    }

    static Task sample(Long lessonId,
                       String title = Commons.uniqueName('title', 15),
                       String description = Commons.uniqueName('description', 15),
                       Integer maxPoints = RandomUtils.nextInt(1, 100),
                       Boolean isCodeTask = RandomUtils.nextBoolean()) {
        new Task(lessonId, title, description, maxPoints, isCodeTask)
    }

    Task withTitle(String title){
        this.title = title
        this
    }

    Task withDescription(String description){
        this.description = description
        this
    }

    Task withMaxPoints(Integer maxPoints){
        this.maxPoints = maxPoints
        this
    }

    Task withIsCodeTask(Boolean isCodeTask){
        this.isCodeTask = isCodeTask
        this
    }
}