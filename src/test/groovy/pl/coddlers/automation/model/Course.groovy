package pl.coddlers.automation.model

import pl.coddlers.automation.Commons

class Course {
    String title
    String description

    Course(String title, String description) {
        this.title = title
        this.description = description
    }

    static Course sample(String title = Commons.uniqueName('Course'), String description = 'Sample description') {
        new Course(title, description)
    }
}
