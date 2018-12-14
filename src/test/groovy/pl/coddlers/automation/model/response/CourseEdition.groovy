package pl.coddlers.automation.model.response


import java.sql.Timestamp

class CourseEdition {
    Integer id
    String title
    String invitationToken
    CourseVersion courseVersion
    Timestamp startDate

    CourseEdition(Integer id, String title, String invitationToken, CourseVersion courseVersion, Timestamp startDate) {
        this.id = id
        this.title = title
        this.invitationToken = invitationToken
        this.courseVersion = courseVersion
        this.startDate = startDate
    }
}