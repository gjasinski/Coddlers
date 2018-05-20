import {Component, OnInit} from '@angular/core';
import {CourseService} from "../../services/course.service";
import {Course} from "../../models/course";
import {ActivatedRoute} from "@angular/router";
import {Assignment} from "../../models/assignment";
import {AssignmentService} from "../../services/assignment.service";

@Component({
  selector: 'cod-course-page',
  templateUrl: './course-page.component.html',
  styleUrls: ['./course-page.component.scss']
})
export class CoursePageComponent implements OnInit {
  private course: Course;
  private assignments: Assignment[] = [];

  constructor(private courseService: CourseService, private route: ActivatedRoute,
              private assignmentService: AssignmentService) {}

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

}
