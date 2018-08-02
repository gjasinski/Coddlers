import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CourseService} from "../../../../services/course.service";
import {Location} from '@angular/common';
import {Course} from "../../../../models/course";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'cod-edit-course-page',
  templateUrl: './edit-course-page.component.html',
  styleUrls: ['./edit-course-page.component.scss']
})
export class EditCoursePageComponent implements OnInit {
  private formGroup: FormGroup;
  private course: Course;

  constructor(private formBuilder: FormBuilder,
              private courseService: CourseService,
              private route: ActivatedRoute,
              private _location: Location) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(
      params => {
        let courseId: number = +params.get('courseId');
        this.courseService.getCourse(courseId).subscribe((course: Course) => {
            this.course = course;

            this.formGroup.setValue({
              title: this.course.title,
              description: this.course.description
            });
          }
        );
      });

    this.formGroup = this.formBuilder.group({
      'title': ['', Validators.compose([Validators.required, Validators.minLength(3),
        Validators.maxLength(100)])],
      'description': ''
    });
  }

  updateCourse(course) {
    this.courseService.updateCourse(new Course(this.course.id, course.title, course.description)
    ).subscribe(obj => {
      this._location.back();
    });
  }

  back(e) {
    e.preventDefault();
    this._location.back();
  }

}
