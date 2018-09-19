import {Component, OnDestroy, OnInit} from '@angular/core';
import {LessonService} from "../../../../services/lesson.service";
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {Lesson} from "../../../../models/lesson";
import {Course} from "../../../../models/course";
import {CourseService} from "../../../../services/course.service";
import {filter, map, switchMap, tap} from "rxjs/operators";
import {Observable} from "rxjs/internal/Observable";
import {Location} from "@angular/common";
import {Task} from "../../../../models/task";
import {TaskService} from "../../../../services/task.service";
import {Event} from "../../../../models/event";
import {EventService} from "../../../../services/event.service";
import {SubscriptionManager} from "../../../../utils/SubscriptionManager";

@Component({
  selector: 'cod-teacher-lesson-page',
  templateUrl: './teacher-lesson-page.component.html',
  styleUrls: ['./teacher-lesson-page.component.scss']
})
export class TeacherLessonPageComponent implements OnInit, OnDestroy {
  private lesson: Lesson;
  private course: Course;
  private tasks: Task[] = [];
  private tasksVisibility: boolean[];
  private subscriptionManager: SubscriptionManager = new SubscriptionManager();

  constructor(private courseService: CourseService,
              private lessonService: LessonService,
              private route: ActivatedRoute,
              private router: Router,
              private _location: Location,
              private taskService: TaskService,
              private eventService: EventService) {
  }

  ngOnInit() {
    let routerEventSub = this.router.events.pipe(
      filter(e => (e instanceof NavigationEnd && e.url.split('/').length === 6)),
      switchMap(() => this.getLessonsAndTasks())
    ).subscribe();
    this.subscriptionManager.add(routerEventSub);

    let getLessonsSub = this.getLessonsAndTasks().subscribe(() => {
      this.tasksVisibility = new Array(this.tasks.length);
    });
    this.subscriptionManager.add(getLessonsSub);

    let routeParamsSub = this.route.parent.params.subscribe(params =>
      this.courseService.getCourse(params.courseId)
        .subscribe((course: Course) => this.course = course));
    this.subscriptionManager.add(routeParamsSub);
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribeAll();
  }

  private getLessonsAndTasks(): Observable<any> {
    return this.getLesson()
      .pipe(
        switchMap((lesson: Lesson) => this.getTasks(lesson.id))
      );
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
    return this.taskService.getTasks(lessonId).pipe(
      tap((tasks: Task[]) => {
        this.tasks = tasks;
        this.tasks.sort((a, b) => a.id - b.id);
      })
    );
  }

  changeVisibilityForSubmissions(index: number) {
    this.tasksVisibility[index] = !this.tasksVisibility[index];
  }

  countPointsForLesson(): number {
    let sum = 0;
    this.tasks.forEach(task => sum += task.maxPoints);
    return sum;
  }

  addTask() {
    this.eventService.emit(new Event('open-add-task-modal', this.lesson.id));
  }

  navigateToCourse() {
    this.router.navigate(["teacher", "courses", this.course.id]);
  }

  editTask(task: Task) {
    this.eventService.emit(new Event('open-edit-task-modal', task.id));
  }
}
