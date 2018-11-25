///<reference path="../../../../../../node_modules/rxjs/internal/Observable.d.ts"/>
import {Component, OnInit} from '@angular/core';
import {Lesson} from "../../../../models/lesson";
import {Task} from "../../../../models/task";
import {Course} from "../../../../models/course";
import {Observable} from "rxjs/internal/Observable";
import {map, switchMap} from "rxjs/operators";
import {ActivatedRoute, Router} from "@angular/router";
import {Location} from "@angular/common";
import {LessonService} from "../../../../services/lesson.service";
import {CourseService} from "../../../../services/course.service";
import {TaskService} from "../../../../services/task.service";
import {CourseEdition} from "../../../../models/courseEdition";
import {CourseEditionService} from "../../../../services/course-edition.service";
import {CourseEditionLesson} from "../../../../models/courseEditionLesson";
import {SubmissionService} from "../../../../services/submission.service";
import {Submission} from "../../../../models/submission";
import {StudentLessonRepositoryService} from "../../../../services/student-lesson-repository.service";
import {SubscriptionManager} from "../../../../utils/SubscriptionManager";
import {forkJoin} from "rxjs/index";
import {SubmissionStatus, SubmissionStatusEnum} from "../../../../models/submissionStatusEnum";

@Component({
  selector: 'cod-student-lesson-page',
  templateUrl: './student-lesson-page.component.html',
  styleUrls: ['./student-lesson-page.component.scss']
})
export class StudentLessonPageComponent implements OnInit {
  private subscriptionManager: SubscriptionManager = new SubscriptionManager();
  private courseEditionLesson: CourseEditionLesson;
  private lesson: Lesson;
  private courseEdition: CourseEdition;
  private course: Course;
  private tasks: Task[] = [];
  private tasksVisibility: boolean[] = new Array(this.tasks.length);
  private submissions: Submission[];
  private repoUrl: String;
  private courseEditionId: number = 0;

  constructor(private courseService: CourseService,
              private lessonService: LessonService,
              private route: ActivatedRoute,
              private router: Router,
              private _location: Location,
              private taskService: TaskService,
              private courseEditionService: CourseEditionService,
              private submissionService: SubmissionService,
              private studentLessonRepositoryService: StudentLessonRepositoryService) {
  }

  ngOnInit() {
    let paramParentMapSubscription = this.route.parent.paramMap.pipe(
      switchMap((params) => {
        this.courseEditionId = +params.get('courseEditionId');
        return forkJoin(
          this.courseService.getCourseByCourseEditionId(this.courseEditionId),
          this.courseEditionService.getCourseEdition(this.courseEditionId));
      })).subscribe(([course, courseEdition]) => {
      this.course = course;
      this.courseEdition = courseEdition;

      let paramMapSubscription = this.route.paramMap
        .pipe(switchMap((params) => forkJoin(
          this.getLessonAndTasksAndReturnSubmissions(params),
          this.courseEditionService.getCourseEditionLesson(this.courseEdition.id, +params.get('lessonId')),
          this.studentLessonRepositoryService.getStudentLessonRepositoryUrl(this.courseEditionId, +params.get('lessonId')),
        )))
        .subscribe(([submissions, courseEditionLesson, lessonRepositoryUrl]) => {
          this.courseEditionLesson = courseEditionLesson;
          if (lessonRepositoryUrl.length > 1) {
            this.repoUrl = "git clone " + lessonRepositoryUrl + " \"" + this.lesson.title + "\"";
          }
          else {
            this.repoUrl = "Your repository is not forked yet"
          }
        });
      this.subscriptionManager.add(paramMapSubscription);
    });

    this.subscriptionManager.add(paramParentMapSubscription);
  }

  private getLessonAndTasksAndReturnSubmissions(params): Observable<Submission[]> {
    return this.lessonService.getLesson(+params.get('lessonId'))
      .pipe(switchMap((lesson: Lesson) => {
        this.lesson = lesson;
        return this.getTasksAndReturnSubmissions(+params.get('courseEditionId'), lesson.id);
      }))
  }


  private getTasksAndReturnSubmissions(courseEditionId: number, lessonId: number): Observable<Submission[]> {
    return this.taskService.getTasks(lessonId)
      .pipe(
        switchMap((tasks: Task[]) => {
          this.tasks = tasks;
          this.tasks.sort((n1, n2) => n1.id - n2.id);
          return this.submissionService.getSubmissionsForLesson(courseEditionId, lessonId);
        }),
        map((submissions: Submission[]) => {
          this.submissions = submissions;
          this.submissions.sort((n1, n2) => n1.taskId - n2.taskId);
          return this.submissions;
        })
      );
  }

  back(e) {
    e.preventDefault();
    this._location.back();
  }

  navigateToCourseEdition() {
    this.router.navigate(["student", "course-editions", this.course.id]);
  }

  countPointsForLesson(): number {
    return this.tasks.reduce((sum, t) => sum + t.maxPoints, 0);
  }

  changeVisibilityForSubmissions(index: number) {
    this.tasksVisibility[index] = !this.tasksVisibility[index];
  }


  descriptionStatus(submissionStatus: SubmissionStatus): String {
    return SubmissionStatus.getEnumFromString(submissionStatus.toString()).toString();
  }

  isGraded(submission: number): boolean {
    return this.submissions[submission].submissionStatus.toString() === SubmissionStatusEnum.GRADED.toString();
  }
}
