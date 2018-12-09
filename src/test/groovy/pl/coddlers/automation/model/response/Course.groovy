package pl.coddlers.automation.model.response

class Course {
    Integer id
    String title
    String description

    Course(Integer id, String title, String description) {
        this.id = id
        this.title = title
        this.description = description
    }
}
