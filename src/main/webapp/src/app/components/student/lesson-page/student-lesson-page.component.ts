///<reference path="../../../../../node_modules/rxjs/internal/Observable.d.ts"/>
import { Component, OnInit } from '@angular/core';
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

@Component({
  selector: 'cod-student-lesson-page',
  templateUrl: './student-lesson-page.component.html',
  styleUrls: ['./student-lesson-page.component.scss']
})
export class StudentLessonPageComponent implements OnInit {
  private lesson: Lesson;
  private courseEdition: CourseEdition;
  private course: Course;
  private tasks: Task[] = [];
  private repoUrl: string;

  constructor(private courseService: CourseService,
              private lessonService: LessonService,
              private route: ActivatedRoute,
              private router: Router,
              private _location: Location,
              private taskService: TaskService) { }

  ngOnInit() {
    let subscribtion = this.getLessonAndTasks().subscribe();
    let routeParamsSub = this.route.parent.params.subscribe(params => {
      //console.error(+params.get("courseEditionId"));
      //console.error(params.lessonId );
      //this.courseService.getCourseByCourseEditionId(params.get("courseEditionId")).subscribe(v => this.course = v);
    });

 //   this.route.parent.params.subscribe(params => {
     // console.error(params.courseId)});
/*    this.route.queryParamMap.subscribe(params => {
      console.error(params.get('lessonId'));
    });*/
    }

  private getLessonAndTasks(): Observable<any> {
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
          let repoName: string = this.lesson.title.toLowerCase().replace(' ', '-');
          // todo change repo url
          this.repoUrl = `http://coddlers.pl:10080/student/${repoName}.git`;

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
    this._location.back();
  }

  navigateToCourseEdition() {
    this.router.navigate(["student", "course-editions", this.course.id]);
  }

}
