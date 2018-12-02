import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {CourseWithCourseEdition} from "../../../../models/courseWithCourseEdition";
import {SubscriptionManager} from "../../../../utils/SubscriptionManager";
import {CourseEditionService} from "../../../../services/course-edition.service";

@Component({
  selector: 'cod-student-course-page',
  templateUrl: './student-course-page.component.html',
  styleUrls: ['./student-course-page.component.scss']
})
export class StudentCoursePageComponent implements OnInit {
  courses: CourseWithCourseEdition[];
  courseEndDates: Map<CourseWithCourseEdition, Date> = new Map<CourseWithCourseEdition, Date>();
  private subscriptionManager: SubscriptionManager = new SubscriptionManager();

  constructor(private courseEditionService: CourseEditionService,
              private route: ActivatedRoute,
              private editionService: CourseEditionService) {
  }

  ngOnInit(): void {
    this.subscriptionManager.add(this.courseEditionService.getCourses()
      .subscribe((courses: CourseWithCourseEdition[]) => {
        this.courses = courses;
        this.courses.forEach(course => {
          this.editionService.getCourseEditionLessonList(course.courseEdition.id).subscribe(courseEditionLessonList => {
            if (courseEditionLessonList.length == 0)
              this.courseEndDates.set(course, course.courseEdition.startDate);
          else
              this.courseEndDates.set(course, courseEditionLessonList[courseEditionLessonList.length - 1].endDate);
          })
        })
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribeAll();
  }
}
