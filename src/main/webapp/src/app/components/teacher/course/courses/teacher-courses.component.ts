import {Component, OnInit} from '@angular/core';
import {CourseService} from "../../../../services/course.service";
import {Course} from "../../../../models/course";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'cod-teacher-courses',
  templateUrl: './teacher-courses.component.html',
  styleUrls: ['./teacher-courses.component.scss']
})
export class TeacherCoursesComponent implements OnInit {
  private courses: Course[];

  constructor(private courseService: CourseService,
              private route: ActivatedRoute,) {
  }

  ngOnInit(): void {
    this.courseService.getCourses().subscribe((courses: Course[]) => {
      this.courses = courses;
    });
  }

}
