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
