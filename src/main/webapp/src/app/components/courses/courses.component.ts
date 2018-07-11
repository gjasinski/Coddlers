import {Component, OnInit} from '@angular/core';
import {CourseService} from "../../services/course.service";
import {Course} from "../../models/course";

@Component({
  selector: 'cod-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.scss']
})
export class CoursesComponent implements OnInit {
  private courses: Course[];

  constructor(private courseService: CourseService) {
  }

  ngOnInit(): void {
    this.courseService.getCourses().subscribe((courses: Course[]) => {
      this.courses = courses;
    });
  }

}
