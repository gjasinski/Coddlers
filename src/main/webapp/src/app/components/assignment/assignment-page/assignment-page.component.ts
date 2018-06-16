import { Component, OnInit } from '@angular/core';
import {AssignmentService} from "../../../services/assignment.service";
import {ActivatedRoute} from "@angular/router";
import {Assignment} from "../../../models/assignment";
import {Course} from "../../../models/course";
import {CourseService} from "../../../services/course.service";

@Component({
  selector: 'app-assignment-page',
  templateUrl: './assignment-page.component.html',
  styleUrls: ['./assignment-page.component.scss']
})
export class AssignmentPageComponent implements OnInit {
  private assignment: Assignment;
  private course: Course;

  constructor(private courseService: CourseService,
              private assignmentService: AssignmentService,
              private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.paramMap.subscribe(
      params => {
        this.assignmentService.getAssignment(+params.get('assignmentId'))
          .subscribe((assignment: Assignment) => {
            this.assignment = assignment;
          })
      }
    );

    this.route.parent.params.subscribe(params => {
      this.courseService.getCourse(params.courseId).subscribe((course: Course) => {
        this.course = course;
      })
    })
  }

}
