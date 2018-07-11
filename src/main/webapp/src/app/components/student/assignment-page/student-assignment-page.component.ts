import { Component, OnInit } from '@angular/core';
import {Assignment} from "../../../models/assignment";
import {Task} from "../../../models/task";
import {Course} from "../../../models/course";
import {Observable} from "rxjs/internal/Observable";
import {map, switchMap, tap} from "rxjs/operators";
import {ActivatedRoute, Router} from "@angular/router";
import {Location} from "@angular/common";
import {AssignmentService} from "../../../services/assignment.service";
import {CourseService} from "../../../services/course.service";
import {TaskService} from "../../../services/task.service";

@Component({
  selector: 'app-student-assignment-page',
  templateUrl: './student-assignment-page.component.html',
  styleUrls: ['./student-assignment-page.component.scss']
})
export class StudentAssignmentPageComponent implements OnInit {
  private assignment: Assignment;
  private course: Course;
  private tasks: Task[] = [];
  private repoUrl: string;

  constructor(private courseService: CourseService,
              private assignmentService: AssignmentService,
              private route: ActivatedRoute,
              private router: Router,
              private _location: Location,
              private taskService: TaskService) { }

  ngOnInit() {
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
          let repoName: string = this.assignment.title.toLowerCase().replace(' ', '-');
          this.repoUrl = `http://coddlers.pl:10080/student/${repoName}.git`;

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
