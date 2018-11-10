///<reference path="../../../../../node_modules/rxjs/internal/Observable.d.ts"/>
import {Component, OnInit} from '@angular/core';
import {Lesson} from "../../../models/lesson";
import {Task} from "../../../models/task";
import {Course} from "../../../models/course";
import {Observable} from "rxjs/internal/Observable";
import {map, switchMap} from "rxjs/operators";
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
import {forkJoin} from "rxjs/index";
import {SubmissionStatus} from "../../../models/submissionStatusEnum";

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
    let paramMapSubscription = this.route.paramMap
      .pipe(switchMap((params) => forkJoin(
        this.getLessonAndTasksAndReturnSubmissions(params),
        this.courseService.getCourseByCourseEditionId(+params.get('courseEditionId')),
        this.courseEditionService.getCourseEdition(+params.get('courseEditionId')),
        this.courseEditionService.getCourseEditionLesson(+params.get('courseEditionId'), +params.get('lessonId')),
        this.studentLessonRepositoryService.getLessonRepositoryUrl(+params.get('courseEditionId'), +params.get('lessonId'))
      )))
      .subscribe(([submissions, course, courseEdition, courseEditionLesson, lessonRepositoryUrl]) => {
        this.course = course;
        this.courseEdition = courseEdition;
        this.courseEditionLesson = courseEditionLesson;
        this.createRepositoryUrl(lessonRepositoryUrl);
      });
    this.subscriptionManager.add(paramMapSubscription);
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

  private createRepositoryUrl(lessonRepositoryUrl: String): void {
    let repoDirectory: string = this.lesson.title.toLowerCase().replace(new RegExp(' ', 'g'), '-');
    this.repoUrl = "git clone http://coddlers.pl:10080/" + lessonRepositoryUrl + " " + repoDirectory;
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


  descriptionStatus(submissionStatusType: SubmissionStatusType): String {
    return SubmissionStatus.getEnumFromString(submissionStatusType.toString()).toDescription();
  }
}
