import {Component, OnInit} from '@angular/core';
import {Course} from "../../../../models/course";
import {switchMap} from "rxjs/operators";
import {forkJoin, Observable} from "rxjs";
import {Lesson} from "../../../../models/lesson";
import {CourseService} from "../../../../services/course.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Location} from "@angular/common";
import {LessonService} from "../../../../services/lesson.service";
import {CourseVersionService} from "../../../../services/course-version.service";
import {CourseEditionService} from "../../../../services/course-edition.service";
import {EventService} from "../../../../services/event.service";
import {CourseEdition} from "../../../../models/courseEdition";
import {SubscriptionManager} from "../../../../utils/SubscriptionManager";
import {SubmissionService} from "../../../../services/submission.service";
import {CourseEditionLesson} from "../../../../models/courseEditionLesson";
import {SubmissionStatusEnum} from "../../../../models/submissionStatusEnum";
import * as _ from "lodash";

@Component({
  selector: 'cod-student-course-edition-page',
  templateUrl: './student-course-edition-page.component.html',
  styleUrls: ['./student-course-edition-page.component.scss']
})
export class StudentCourseEditionPageComponent implements OnInit {
  course: Course;
  courseEdition: CourseEdition;
  lessons: Lesson[] = [];
  editionLessons: Map<Lesson, CourseEditionLesson> = new Map<Lesson, CourseEditionLesson>();
  submissionsStatus: Map<number, string> = new Map<number, string>();
  submissionsGrade: Map<number, number> = new Map<number, number>();

  submitted: number = 0;
  graded: number = 0;

  private subscriptionManager: SubscriptionManager = new SubscriptionManager();

  constructor(private courseService: CourseService,
              private route: ActivatedRoute,
              private _location: Location,
              private lessonService: LessonService,
              private courseVersionService: CourseVersionService,
              private courseEditionService: CourseEditionService,
              private submissionService: SubmissionService,
              private eventService: EventService,
              private router: Router) {
  }

  ngOnInit() {
    let paramsSub = this.route.paramMap.pipe(
      switchMap((params) => forkJoin(
        this.courseEditionService.getCourseEdition(+params.get('courseEditionId')),
        this.courseService.getCourseByCourseEditionId(+params.get('courseEditionId')),
        this.lessonService.getLessonsByCourseEditionId(+params.get('courseEditionId')))))
    .subscribe(([courseEdition, course, lessons]) => {
      this.courseEdition = courseEdition;
      this.course = course;
      this.lessons = lessons;
      this.getSubmissionData();
    });

    this.subscriptionManager.add(paramsSub);
  }

  getSubmissionData() {
    return _.forEach(this.lessons, lesson => {
      const lessonId = lesson.id;
      const subscription = forkJoin(
        this.submissionService.getSubmissionsForLesson(this.courseEdition.id, lessonId),
        this.courseEditionService.getCourseEditionLesson(this.courseEdition.id, lessonId))
      .subscribe(([submissions, courseEditionLesson]) => {
        if (submissions.length === 0) {
          this.submissionsStatus.set(lessonId, SubmissionStatusEnum.NOT_SUBMITTED.description);
          this.submissionsGrade.set(lessonId, -1);
        } else {
          this.submissionsStatus.set(lessonId, _.last(submissions).submissionStatusType);
          if (this.submissionsStatus.get(lessonId) === SubmissionStatusEnum.NOT_SUBMITTED.description) {
            this.submissionsGrade.set(lessonId, -1);
          } else {
            this.submissionsGrade.set(lessonId, _.last(submissions).points);
          }
        }

        this.submitted = _.size(_.filter(Array.from(this.submissionsStatus.values(),
          status => status !== SubmissionStatusEnum.NOT_SUBMITTED.description)));
        this.graded = _.size(_.filter(Array.from(this.submissionsGrade.values()),
          grade => grade >= 0));

        this.editionLessons.set(lesson, courseEditionLesson);
      });

      this.subscriptionManager.add(subscription);
    })
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribeAll();
  }
}
