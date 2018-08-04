import { Component, OnInit } from '@angular/core';
import {LessonService} from "../../../../services/lesson.service";
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {Lesson} from "../../../../models/lesson";
import {Course} from "../../../../models/course";
import {CourseService} from "../../../../services/course.service";
import {filter, flatMap, map, mergeMap, subscribeOn, switchMap, tap} from "rxjs/operators";
import {Observable} from "rxjs/internal/Observable";
import {Location} from "@angular/common";
import {Task} from "../../../../models/task";
import {TaskService} from "../../../../services/task.service";

@Component({
  selector: 'cod-lessons-page',
  templateUrl: './teacher-lesson-page.component.html',
  styleUrls: ['./teacher-lesson-page.component.scss']
})
export class TeacherLessonPageComponent implements OnInit {
  private lesson: Lesson;
  private course: Course;
  private tasks: Task[] = [];

  constructor(private courseService: CourseService,
              private lessonService: LessonService,
              private route: ActivatedRoute,
              private router: Router,
              private _location: Location,
              private taskService: TaskService) { }

  ngOnInit() {
    this.router.events.pipe(
      filter(e => (e instanceof NavigationEnd && e.url.split('/').length === 6)),
      switchMap(() => {
        return this.getLessonsAndTasks();
      }),
    ).subscribe();

    this.getLessonsAndTasks().subscribe();

    this.route.parent.params.subscribe(params => {
      this.courseService.getCourse(params.courseId).subscribe((course: Course) => {
        this.course = course;
      })
    });
  }

  private getLessonsAndTasks(): Observable<any> {
    return this.getLesson()
      .pipe(
        switchMap((lesson: Lesson) => {
          return this.getTasks(lesson.id);
        })
      );
  }

  private getLesson(): Observable<Lesson> {
    return this.route.paramMap
      .pipe(
        switchMap((params) => {
          return this.lessonService.getLesson(+params.get('lessonId'));
        }),
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
      })
    );
  }

  back(e) {
    e.preventDefault();
    this.router.navigate(['../../'], {relativeTo: this.route});
  }

}
