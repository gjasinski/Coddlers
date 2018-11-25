import {Component, OnDestroy, OnInit} from '@angular/core';
import {Course} from "../../../../models/course";
import {Lesson} from "../../../../models/lesson";
import {CourseEdition} from "../../../../models/courseEdition";
import {Task} from "../../../../models/task";
import {Event} from "../../../../models/event";
import {CourseService} from "../../../../services/course.service";
import {ActivatedRoute, Router} from "@angular/router";
import {CourseEditionService} from "../../../../services/course-edition.service";
import {LessonService} from "../../../../services/lesson.service";
import {TaskService} from "../../../../services/task.service";
import {Submission} from "../../../../models/submission";
import {SubmissionService} from "../../../../services/submission.service";
import {filter, switchMap, tap} from "rxjs/operators";
import {EventService} from "../../../../services/event.service";
import {SubscriptionManager} from "../../../../utils/SubscriptionManager";
import {forkJoin} from "rxjs";
import {CourseEditionLesson} from "../../../../models/courseEditionLesson";
import {SubmissionStatus, SubmissionStatusEnum} from "../../../../models/submissionStatusEnum";
import {NgbDropdownConfig} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-edition-page',
  templateUrl: './course-edition-page.component.html',
  styleUrls: ['./course-edition-page.component.scss']
})
export class CourseEditionPageComponent implements OnInit, OnDestroy {
  course: Course;
  courseEdition: CourseEdition;
  showLesson: boolean[];
  courseMap: Map<Lesson, Task[]> = new Map<Lesson, Task[]>();
  submissionsMap: Map<Task, Submission[]> = new Map<Task, Submission[]>();
  lessonTimeMap: Map<Lesson, CourseEditionLesson> = new Map<Lesson, CourseEditionLesson>();
  showTask: Map<Task, boolean> = new Map<Task, boolean>();
  lessons = [];
  courseEditionLessonList: CourseEditionLesson[];
  editionEndDate: Date;

  private subscriptionManager: SubscriptionManager = new SubscriptionManager();

  constructor(private courseService: CourseService,
              private editionService: CourseEditionService,
              private lessonService: LessonService,
              private taskService: TaskService,
              private submissionService: SubmissionService,
              private router: Router,
              public eventService: EventService,
              private route: ActivatedRoute,
              config: NgbDropdownConfig) {
    config.autoClose = "outside";
  }

  ngOnInit() {
    this.getCourseInfo();
    this.getEditionData();
    this.watchUpdateEvents();
  }

  watchUpdateEvents() {
    let eventSub = this.eventService.events.pipe(
      filter((event: Event) =>
        event.eventType === 'edit-lesson-due-date-updated'
      ),
      tap(() => this.updateDates())
    ).subscribe();
    this.subscriptionManager.add(eventSub);
  }

  getCourseInfo() {
    let routeParamsSub = this.route.parent.params.pipe(
      switchMap(params => {
        return this.courseService.getCourse(params.courseId);
      }),
      tap((course: Course) => {
        this.course = course;
      })
    ).subscribe();
    this.subscriptionManager.add(routeParamsSub);
  }

  getEditionData() {
    let routeParamsSub = this.route.params.pipe(
      switchMap(params => {
        this.submissionsMap.clear();
        this.courseMap.clear();
        this.lessonTimeMap.clear();
        this.showTask.clear();

        return forkJoin(
          this.lessonService.getLessonsByCourseEditionId(params.editionId),
          this.editionService.getCourseEditionLessonList(params.editionId),
          this.editionService.getCourseEdition(params.editionId)
        );
      }),
      switchMap(([lessons, courseEditionLessonList, courseEdition]:
                   [Lesson[], CourseEditionLesson[], CourseEdition]) => {
        this.courseEdition = courseEdition;
        this.courseEditionLessonList = courseEditionLessonList;
        this.editionEndDate = courseEditionLessonList[courseEditionLessonList.length - 1].endDate;
        let getTasksObs = [];

        lessons.forEach(lesson => {
          this.fillLessonTimeMap(lesson, courseEditionLessonList);

          getTasksObs.push(
            this.getTasks(lesson)
          );
        });

        return forkJoin(getTasksObs).pipe(tap(() => {
          this.lessons = lessons;
        }));
      })
    ).subscribe();
    this.subscriptionManager.add(routeParamsSub);
  }

  updateDates() {
    let sub = this.editionService.getCourseEditionLessonList(this.courseEdition.id).pipe(
      tap((courseEditionLessonList: CourseEditionLesson[]) => {
        this.lessons.forEach(lesson => {
          this.fillLessonTimeMap(lesson, courseEditionLessonList);
        });
      })
    ).subscribe(() => sub.unsubscribe());
  }

  private fillLessonTimeMap(lesson: Lesson, courseEditionLessonList: CourseEditionLesson[]) {
    let foundItem = courseEditionLessonList.find((item: CourseEditionLesson) => item.lessonId == lesson.id);
    this.lessonTimeMap.set(lesson, foundItem);
  }

  getTasks(lesson: Lesson) {
    return this.taskService.getTasks(lesson.id)
      .pipe(
        tap((tasks: Task[]) => {
          this.courseMap.set(lesson, tasks);
          this.showLesson = new Array(this.courseMap.size).fill(false);

          this.getSubmissions(tasks);
        })
      );
  }

  getSubmissions(tasks: Task[]): void {
    tasks.forEach(task => {
      this.showTask.set(task, false);
      let submissionSub = this.submissionService.getSubmissions(task.id, this.courseEdition.id)
        .subscribe((submissions: Submission[]) => {
          this.submissionsMap.set(task, submissions);
        });

      this.subscriptionManager.add(submissionSub);
    });
  }

  swapShowLesson(index: number) {
    this.showLesson[index] = !this.showLesson[index]
  }

  changeVisibilityForSubmissions(task: Task) {
    this.showTask.set(task, !this.showTask.get(task));
  }

  navigateToCourse() {
    this.router.navigate(["teacher", "courses", this.course.id]);
  }

  navigateToLesson(lesson: Lesson) {
    this.router.navigate(["teacher", "courses", this.course.id, "lessons", lesson.id]);
  }

  openEditLessonDueDateModal(lesson: Lesson) {
    this.eventService.emit(new Event('open-edit-lesson-due-date-modal', {
      lessonId: lesson.id,
      editionId: this.courseEdition.id
    }));
  }

  forkLesson(lesson: Lesson) {
    this.lessonService.forkLessons(this.courseEdition.id, lesson.id).subscribe();
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribeAll();
  }

  inviteStudents(): void {
    this.eventService.emit(new Event('open-invite-students-modal', this.courseEdition.id));
  }

  isGraded(submission: Submission): boolean {
    return submission.submissionStatusType.toString() == SubmissionStatusEnum.GRADED.toString();
  }

  descriptionStatus(submission: Submission): string {
    return SubmissionStatus.getEnumFromString(submission.submissionStatusType.toString()).toString();
  }
}
