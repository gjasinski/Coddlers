///<reference path="../../../../../node_modules/rxjs/internal/Observable.d.ts"/>
import {Component, OnInit} from '@angular/core';
import {Lesson} from "../../../models/lesson";
import {Task} from "../../../models/task";
import {Course} from "../../../models/course";
import {Observable} from "rxjs/internal/Observable";
import {map, switchMap, tap} from "rxjs/operators";
import {ActivatedRoute, Router} from "@angular/router";
import {Location} from "@angular/common";
import {LessonService} from "../../../services/lesson.service";
import {CourseService} from "../../../services/course.service";
import {TaskService} from "../../../services/task.service";
import {CourseEdition} from "../../../models/courseEdition";
import {CourseEditionService} from "../../../services/course-edition.service";
import {CourseEditionLesson} from "../../../models/courseEditionLesson";
import {SubmissionService} from "../../../services/submission.service";
import {Submission} from "../../../models/submission";
import {SubmissionStatusType} from "../../../models/submissionStatusType";
import {StudentLessonRepositoryService} from "../../../services/student-lesson-repository.service";
import {SubscriptionManager} from "../../../utils/SubscriptionManager";

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
    this.subscriptionManager.add(this.getLesson().pipe(
      switchMap((lesson: Lesson) => this.getTasks(lesson.id))).subscribe(() => this.getRepositoryUrl()));
    this.subscriptionManager.add(this.route.paramMap.pipe(
      switchMap((params) => this.courseService.getCourseByCourseEditionId(+params.get('courseEditionId'))),
      map((course: Course) => this.course = course)
    ).subscribe());
    this.subscriptionManager.add(this.route.paramMap.pipe(
      switchMap((params) => this.courseEditionService.getCourseEdition(+params.get('courseEditionId'))),
      map((courseEdition: CourseEdition) => this.courseEdition = courseEdition)
    ).subscribe());
    this.subscriptionManager.add(this.route.paramMap.pipe(
      switchMap((params) => this.courseEditionService.getCourseEditionLesson(+params.get('courseEditionId'), +params.get('lessonId'))),
      map((courseEditionLesson: CourseEditionLesson) => this.courseEditionLesson = courseEditionLesson)
    ).subscribe());
  }

  private getRepositoryUrl(): void {
    this.subscriptionManager.add(this.route.paramMap.pipe(
      switchMap((params) => this.studentLessonRepositoryService.getLessonRepositoryUrl(+params.get('courseEditionId'), +params.get('lessonId'))),
      map((courseEditionLesson: String) => {
        let repoDirectory: string = this.lesson.title.toLowerCase().replace(new RegExp(' ', 'g'), '-');
        this.repoUrl = "git clone http://coddlers.pl:10080/" + courseEditionLesson + " " + repoDirectory;
        return this.repoUrl;
      })
    ).subscribe());
  }

  private getLesson(): Observable<Lesson> {
    return this.route.paramMap
      .pipe(
        switchMap((params) => this.lessonService.getLesson(+params.get('lessonId'))),
        map((lesson: Lesson) => {
          this.lesson = lesson;
          return this.lesson;
        })
      );
  }

  private getTasks(lessonId: number): Observable<any> {
    return this.taskService.getTasks(lessonId)
      .pipe(
        switchMap((tasks: Task[]) => {
          this.tasks = tasks;
          this.tasks.sort((n1, n2) => n1.id - n2.id);
          return this.submissionService.getSubmissionsForLesson(this.courseEdition.id, this.lesson.id);
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
    let sum = 0;
    this.tasks.forEach(task => sum += task.maxPoints);
    return sum;
  }

  changeVisibilityForSubmissions(index: number) {
    this.tasksVisibility[index] = !this.tasksVisibility[index];
  }


  descriptionStatus(submissionStatusType: SubmissionStatusType): String {
    if (submissionStatusType.toString() === "NOT_SUBMITTED") {
      return "Not submitted";
    }
    else if (submissionStatusType.toString() == "CHANGES_REQUESTED") {
      return "Changes requested";
    }
    else if (submissionStatusType.toString() == "GRADED") {
      return "Graded";
    }
    else if (submissionStatusType.toString() == "WAITING_FOR_REVIEW") {
      return "Waiting for review";
    }
    else {
      return "";
    }
  }
}
