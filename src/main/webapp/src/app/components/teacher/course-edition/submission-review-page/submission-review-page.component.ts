import {Component, OnInit} from '@angular/core';
import {Course} from "../../../../models/course";
import {CourseEdition} from "../../../../models/courseEdition";
import {CourseService} from "../../../../services/course.service";
import {ActivatedRoute, Router} from "@angular/router";
import {CourseEditionService} from "../../../../services/course-edition.service";
import {LessonService} from "../../../../services/lesson.service";
import {TaskService} from "../../../../services/task.service";
import {switchMap} from "rxjs/operators";
import {SubscriptionManager} from "../../../../utils/SubscriptionManager";
import {forkJoin} from "rxjs";
import {Location} from "@angular/common";

@Component({
  selector: 'app-edition-page',
  templateUrl: './submission-review-page.component.html',
  styleUrls: ['./submission-review-age.component.scss']
})
export class SubmissionReviewPageComponent implements OnInit {
  private subscriptionManager: SubscriptionManager = new SubscriptionManager();
  private courseEdition: CourseEdition;
  private course: Course;

  constructor(private courseService: CourseService,
              private lessonService: LessonService,
              private route: ActivatedRoute,
              private router: Router,
              private _location: Location,
              private taskService: TaskService,
              private courseEditionService: CourseEditionService) {
  }

  ngOnInit() {
    let paramMapSubscription = this.route.parent.paramMap
      .pipe(switchMap((params) => forkJoin(
        this.courseService.getCourseByCourseEditionId(+params.get('editionId')),
        this.courseEditionService.getCourseEdition(+params.get('editionId')),
      )))
      .subscribe(([course, courseEdition]) => {
        this.course = course;
        this.courseEdition = courseEdition;
      });
    this.subscriptionManager.add(paramMapSubscription);
  }

  back(e) {
    e.preventDefault();
    this._location.back();
  }

  navigateToCourseEdition() {
    this.router.navigate(["teacher", "courses", this.course.id, "editions", this.courseEdition.id]);
  }
}
