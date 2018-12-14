package pl.coddlers.automation.model

import pl.coddlers.automation.Commons
import pl.coddlers.automation.model.response.CourseVersion

import java.sql.Timestamp
import java.time.Instant

class CourseEdition {
    String title
    CourseVersion courseVersion
    Timestamp startDate

    CourseEdition(String title, CourseVersion courseVersion, Timestamp startDate) {
        this.title = title
        this.courseVersion = courseVersion
        this.startDate = startDate
    }

    static CourseEdition sample(CourseVersion courseVersion,
                                String title = Commons.uniqueName('CourseEdition'),
                                Timestamp startDate = Timestamp.from(Instant.now())) {
        new CourseEdition(title, courseVersion, startDate)
    }

    CourseEdition withTitle(String title){
        this.title = title
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