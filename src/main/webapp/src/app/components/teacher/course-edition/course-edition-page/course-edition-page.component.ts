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
import {switchMap, tap} from "rxjs/operators";
import {EventService} from "../../../../services/event.service";
import {SubscriptionManager} from "../../../../utils/SubscriptionManager";
import {forkJoin} from "rxjs";
import {CourseEditionLesson} from "../../../../models/courseEditionLesson";

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
  showTask: boolean[] = [];
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
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.getCourseInfo();
    this.getEditionData();
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
        this.showTask = [];

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
        this.editionEndDate = courseEditionLessonList[courseEditionLessonList.length-1].endDate;
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
      this.showTask.push(false);

      let submissionSub = this.submissionService.getSubmissions(task.id)
        .subscribe((submissions: Submission[]) => {
          this.submissionsMap.set(task, submissions);
        });

      this.subscriptionManager.add(submissionSub);
    });
  }

  swapShowLesson(index: number) {
    this.showLesson[index] = !this.showLesson[index]
  }

  changeVisibilityForSubmissions(index: number) {
    this.showTask[index] = !this.showTask[index];
  }

  navigateToCourse() {
    this.router.navigate(["teacher", "courses", this.course.id]);
  }

  navigateToLesson(lesson: Lesson) {
    this.router.navigate(["teacher", "courses", this.course.id, "lessons", lesson.id]);
  }

  openEditLessonDueDateModal(lesson: Lesson) {
    this.eventService.emit(new Event('open-edit-lesson-due-date-modal', lesson.id));
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribeAll();
  }
}
