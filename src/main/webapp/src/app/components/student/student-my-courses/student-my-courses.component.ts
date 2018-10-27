import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {CourseEditionService} from "../../../services/course-edition.service";
import {CourseWithCourseEdition} from "../../../models/courseWithCourseEdition";
import {SubscriptionManager} from "../../../utils/SubscriptionManager";

@Component({
  selector: 'cod-student-my-courses',
  templateUrl: './student-my-courses.component.html',
  styleUrls: ['./student-my-courses.component.scss']
})
export class StudentMyCoursesComponent implements OnInit {
  private courses: CourseWithCourseEdition[];
  private subscriptionManager: SubscriptionManager = new SubscriptionManager();

  constructor(private courseEditionService: CourseEditionService,
              private route: ActivatedRoute,) {
  }

  ngOnInit(): void {
    this.subscriptionManager.add(this.courseEditionService.getCourses()
      .subscribe((courses: CourseWithCourseEdition[]) => this.courses = courses)
    );
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribeAll();
  }

}
