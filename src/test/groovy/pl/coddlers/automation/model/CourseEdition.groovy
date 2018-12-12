package pl.coddlers.automation.model

import pl.coddlers.automation.Commons
import pl.coddlers.automation.model.response.CourseVersion

import java.sql.Timestamp
import java.time.Instant

class CourseEdition {
    String title
    String invitationToken
    CourseVersion courseVersion
    Timestamp startDate

    CourseEdition(String title, String invitationToken, CourseVersion courseVersion, Timestamp startDate) {
        this.title = title
        this.invitationToken = invitationToken
        this.courseVersion = courseVersion
        this.startDate = startDate
    }

    static CourseEdition sample(CourseVersion courseVersion,
                                String title = Commons.uniqueName('CourseEdition'),
                                String invitationToken = Commons.uniqueName('InvitationToken'),
                                Timestamp startDate = Timestamp.from(Instant.now())) {
        new CourseEdition(title, invitationToken, courseVersion, startDate)
    }

    CourseEdition withTitle(String title){
        this.title = title
        this
    }

    CourseEdition withInvitationToken(String invitationToken){
        this.invitationToken = invitationToken
        this
    }

    CourseEdition withCourseVersion(CourseVersion courseVersion){
        this.courseVersion = courseVersion
        this
    }

    CourseEdition withStartDate(Timestamp startDate){
        this.startDate = startDate
        this
    }
}