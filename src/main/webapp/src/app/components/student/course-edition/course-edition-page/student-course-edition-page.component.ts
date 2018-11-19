import {Component, OnInit} from '@angular/core';
import {Course} from "../../../../models/course";
import {switchMap} from "rxjs/operators";
import {forkJoin} from "rxjs";
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
import {Submission} from "../../../../models/submission";
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
  submissionsStatus: Map<Lesson, string> = new Map<Lesson, string>();
  submissionsGrade: Map<Lesson, number> = new Map<Lesson, any>();

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

      this.lessons.forEach((lesson: Lesson) => {
        let submissionSub = this.submissionService.getSubmissionsForLesson(this.courseEdition.id, lesson.id)
        .subscribe((submissions: Submission[]) => {
          if (submissions.length === 0) {
            this.submissionsStatus.set(lesson, SubmissionStatusEnum.NOT_SUBMITTED.description);
            this.submissionsGrade.set(lesson, -1);
          } else {
            this.submissionsStatus.set(lesson, _.last(submissions).submissionStatus);
            if (this.submissionsStatus.get(lesson) === SubmissionStatusEnum.NOT_SUBMITTED.description) {
              this.submissionsGrade.set(lesson, -1);
            } else {
              this.submissionsGrade.set(lesson, _.last(submissions).points);
            }
          }

          this.submitted = _.size(_.filter(Array.from(this.submissionsStatus.values(),
            status => status !== SubmissionStatusEnum.NOT_SUBMITTED.description)));
          this.graded = _.size(_.filter(Array.from(this.submissionsGrade.values()),
            grade => grade >= 0));
        });

        let editionLessonSub = this.courseEditionService.getCourseEditionLesson(this.courseEdition.id, lesson.id)
        .subscribe((editionLessons: CourseEditionLesson) => {
          this.editionLessons.set(lesson, editionLessons);
        });

        this.subscriptionManager.add(editionLessonSub);
        this.subscriptionManager.add(submissionSub);
      });
    });

    this.subscriptionManager.add(paramsSub);
  }

  ngOnDestroy():
    void {
    this.subscriptionManager.unsubscribeAll();
  }
}
