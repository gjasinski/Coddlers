import {Component, OnInit} from '@angular/core';
import {Course} from "../../../../models/course";
import {switchMap, tap} from "rxjs/operators";
import {forkJoin, of} from "rxjs";
import {Lesson} from "../../../../models/lesson";
import {CourseService} from "../../../../services/course.service";
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {LessonService} from "../../../../services/lesson.service";
import {CourseVersionService} from "../../../../services/course-version.service";
import {CourseEditionService} from "../../../../services/course-edition.service";
import {CourseEdition} from "../../../../models/courseEdition";
import {SubscriptionManager} from "../../../../utils/SubscriptionManager";
import {SubmissionService} from "../../../../services/submission.service";
import {CourseEditionLesson} from "../../../../models/courseEditionLesson";
import {SubmissionStatusEnum} from "../../../../models/submissionStatusEnum";
import {Submission} from "../../../../models/submission";
import {TaskService} from "../../../../services/task.service";
import {Task} from "../../../../models/task";

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
  lessonGradeStatus: Map<number, string> = new Map<number, string>();
  lessonGrade: Map<number, number> = new Map<number, number>();
  courseEditionEndDate: Date = new Date();
  submitted: number = 0;
  graded: number = 0;
  progressSubmitted: number = 0;
  progressGraded: number = 0;

  private subscriptionManager: SubscriptionManager = new SubscriptionManager();

  constructor(private courseService: CourseService,
              private route: ActivatedRoute,
              private _location: Location,
              private lessonService: LessonService,
              private courseVersionService: CourseVersionService,
              private courseEditionService: CourseEditionService,
              private submissionService: SubmissionService,
              private taskService: TaskService) {
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

  // TODO move this logic to backend
  getSubmissionData() {
    let obs = [];
    this.lessons.forEach(lesson => {
      let fork = forkJoin(
        this.submissionService.getSubmissionsForLesson(this.courseEdition.id, lesson.id),
        this.courseEditionService.getCourseEditionLesson(this.courseEdition.id, lesson.id),
        of(lesson),
        this.taskService.getTasks(lesson.id)
      );
      obs.push(fork);
    });

    forkJoin(obs).pipe(
      tap(collection => {
        collection.forEach(([submissions, courseEditionLesson, lesson, tasks]: [Submission[], CourseEditionLesson, Lesson, Task[]]) => {
          this.submitted += (submissions.length != 0 && submissions.filter(s =>
            s.submissionStatusType.toString() == SubmissionStatusEnum.WAITING_FOR_REVIEW.toString() ||
            s.submissionStatusType.toString() == SubmissionStatusEnum.GRADED.toString()).length == submissions.length)
            ? 1 : 0;

          let isLessonGraded = (submissions.length != 0 && submissions.filter(s =>
            s.submissionStatusType.toString() == SubmissionStatusEnum.GRADED.toString()).length == submissions.length);
          this.graded += isLessonGraded ? 1 : 0;

          if (isLessonGraded) {
            this.lessonGradeStatus.set(lesson.id, 'GRADED');
            let a = submissions.map(s => s.points).reduce((a, b) => a + b, 0);
            let b = tasks.map(t => t.maxPoints).reduce((a, b) => a + b, 0);
            console.log(a, b);
            let grade = b === 0 ? 100 : (a / b) * 100;
            this.lessonGrade.set(lesson.id, grade);
          } else {
            this.lessonGradeStatus.set(lesson.id, 'NOT GRADED');
          }

          this.editionLessons.set(lesson, courseEditionLesson);

          if (courseEditionLesson.endDate > this.courseEditionEndDate) {
            this.courseEditionEndDate = courseEditionLesson.endDate;
          }
        });

        this.progressSubmitted = this.submitted * 100 / this.lessons.length;
        this.progressGraded = this.graded * 100 / this.lessons.length;
      })
    ).subscribe();
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribeAll();
  }
}
