import {Component, OnInit} from '@angular/core';

import {CourseService} from "../../../../services/course.service";
import {Course} from "../../../../models/course";
import {Lesson} from "../../../../models/lesson";
import {LessonService} from "../../../../services/lesson.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Location} from "@angular/common";


@Component({
  selector: 'cod-course-page',
  templateUrl: './course-page.component.html',
  styleUrls: ['./course-page.component.scss']
})
export class CoursePageComponent implements OnInit {
  private course: Course;
  private lessons: Lesson[] = [];

  constructor(private courseService: CourseService,
              private route: ActivatedRoute,
              private _location: Location,
              private lessonService: LessonService,
              private router: Router) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(
      params => {
        let courseId: number = +params.get('courseId');
        this.courseService.getCourse(courseId).subscribe((course: Course) => {
          this.course = course;
        });

        this.lessonService.getLessons(courseId).subscribe((lessons: Lesson[]) => {
          this.lessons = lessons;
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
