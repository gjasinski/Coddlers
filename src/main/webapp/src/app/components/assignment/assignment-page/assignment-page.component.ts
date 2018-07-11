import { Component, OnInit } from '@angular/core';
import {AssignmentService} from "../../../services/assignment.service";
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {Assignment} from "../../../models/assignment";
import {Course} from "../../../models/course";
import {CourseService} from "../../../services/course.service";
import {filter, flatMap, map, mergeMap, subscribeOn, switchMap, tap} from "rxjs/operators";
import {Observable} from "rxjs/internal/Observable";
import {Location} from "@angular/common";
import {Task} from "../../../models/task";
import {TaskService} from "../../../services/task.service";

@Component({
  selector: 'app-assignment-page',
  templateUrl: './assignment-page.component.html',
  styleUrls: ['./assignment-page.component.scss']
})
export class AssignmentPageComponent implements OnInit {
  private assignment: Assignment;
  private course: Course;
  private tasks: Task[] = [];

  constructor(private courseService: CourseService,
              private assignmentService: AssignmentService,
              private route: ActivatedRoute,
              private router: Router,
              private _location: Location,
              private taskService: TaskService) { }

  ngOnInit() {
    this.router.events.pipe(
      filter(e => (e instanceof NavigationEnd && e.url.split('/').length === 5)),
      switchMap(() => {
        return this.getAssignmentAndTasks();
      }),
    ).subscribe();

    this.getAssignmentAndTasks().subscribe();

    this.route.parent.params.subscribe(params => {
      this.courseService.getCourse(params.courseId).subscribe((course: Course) => {
        this.course = course;
      })
    });
  }

  private getAssignmentAndTasks(): Observable<any> {
    return this.getAssignment()
      .pipe(
        switchMap((assignment: Assignment) => {
          return this.getTasks(assignment.id);
        })
      );
  }

  private getAssignment(): Observable<Assignment> {
    return this.route.paramMap
      .pipe(
        switchMap((params) => {
          return this.assignmentService.getAssignment(+params.get('assignmentId'));
        }),
        map((assignment: Assignment) => {
          this.assignment = assignment;

          return this.assignment;
        })
      );
  }

  private getTasks(assignmentId: number): Observable<any> {
    return this.taskService.getTasks(assignmentId).pipe(
      tap((tasks: Task[]) => {
        this.tasks = tasks;
      })
    );
  }

  back(e) {
    e.preventDefault();
    this._location.back();
  }

}
