import {Component, OnInit} from '@angular/core';
import {CourseService} from "../../services/course.service";
import {Course} from "../../models/course";
import {ActivatedRoute, Router} from "@angular/router";
import {Assignment} from "../../models/assignment";
import {AssignmentService} from "../../services/assignment.service";
import {Location} from "@angular/common";

@Component({
  selector: 'cod-course-page',
  templateUrl: './course-page.component.html',
  styleUrls: ['./course-page.component.scss',
    './../../app.component.scss']
})
export class CoursePageComponent implements OnInit {
  private course: Course;
  private assignments: Assignment[] = [];

  constructor(private courseService: CourseService,
              private route: ActivatedRoute,
              private _location: Location,
              private assignmentService: AssignmentService,
              private router: Router) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(
      params => {
        let courseId: number = +params.get('courseId');
        this.courseService.getCourse(courseId).subscribe((course: Course) => {
          this.course = course;
        });

        this.assignmentService.getAssignments(courseId).subscribe((assignments: Assignment[]) => {
          this.assignments = assignments;
        });
      }
    );
  }

  routeToEdit() {
    this.router.navigate(['edit-course/' + this.course.id]);
  }

  back(e) {
    e.preventDefault();
    this._location.back();
  }
}
