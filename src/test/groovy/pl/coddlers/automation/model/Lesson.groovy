package pl.coddlers.automation.model

import org.apache.commons.lang3.RandomUtils
import pl.coddlers.automation.Commons

class Lesson {
    Integer courseId
    Integer courseVersionNumber
    String description
    Integer timeInDays
    String title
    Integer weight

    Lesson(Integer courseId, Integer courseVersionNumber, String description, Integer timeInDays, String title, Integer weight) {
        this.courseId = courseId
        this.courseVersionNumber = courseVersionNumber
        this.description = description
        this.timeInDays = timeInDays
        this.title = title
        this.weight = weight
    }

    static Lesson sample(Integer courseId, Integer courseVersionNumber,
                         String description = Commons.uniqueName('description'),
                         Integer timeInDays = RandomUtils.nextInt(1, 10),
                         String title = Commons.uniqueName('title'),
                         Integer weight = RandomUtils.nextInt(1, 100)) {
        new Lesson(courseId, courseVersionNumber, description, timeInDays, title, weight)
    }
}
