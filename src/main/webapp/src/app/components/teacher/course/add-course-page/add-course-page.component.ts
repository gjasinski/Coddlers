import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CourseService} from "../../../../services/course.service";
import {Course} from "../../../../models/course";
import {Location} from '@angular/common';

@Component({
  selector: 'cod-add-course-page',
  templateUrl: './add-course-page.component.html',
  styleUrls: ['./add-course-page.component.scss']
})
export class AddCoursePageComponent implements OnInit {
  private formGroup: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private courseService: CourseService,
              private _location: Location) {
  }

  ngOnInit(): void {
    this.formGroup = this.formBuilder.group({
      'title': ['', Validators.compose([Validators.required, Validators.minLength(3),
        Validators.maxLength(100)])],
      'description': ''
    });
  }

  addCourse(course): void {
    this.courseService.createCourse(new Course(
      null,
      course.title,
      course.description
      )
    ).subscribe(obj => {
      this._location.back();
    });
  }

  back(e) {
    e.preventDefault();
    this._location.back();
  }

}
