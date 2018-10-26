import { Component, OnInit } from '@angular/core';
import {CourseService} from "../../../services/course.service";
import {ActivatedRoute} from "@angular/router";
import {Course} from "../../../models/course";
import {CourseEditionService} from "../../../services/course-edition.service";
import {CourseEdition} from "../../../models/courseEdition";
import {CourseWithCourseEdition} from "../../../models/courseWithCourseEdition";

@Component({
  selector: 'cod-student-my-courses',
  templateUrl: './student-my-courses.component.html',
  styleUrls: ['./student-my-courses.component.scss']
})
export class StudentMyCoursesComponent implements OnInit {
  private courses: CourseWithCourseEdition[];

  constructor(private courseEditionService: CourseEditionService,
              private route: ActivatedRoute,) {
  }

  ngOnInit(): void {
    this.courseEditionService.getCourses().subscribe((courses: CourseWithCourseEdition[]) => {
      this.courses = courses;
    });
  }
}
