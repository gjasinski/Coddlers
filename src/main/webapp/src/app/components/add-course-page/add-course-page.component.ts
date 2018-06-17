import {Component, OnInit} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {CourseService} from "../../services/course.service";
import {Course} from "../../models/course";
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
              private _location: Location) {}

  ngOnInit(): void {
    this.formGroup = this.formBuilder.group({
      'title': ['', Validators.compose([Validators.required, Validators.minLength(3),
      Validators.maxLength(100)])],
      'description': '',
      'startDate': [null, Validators.required],
      'endDate': [null, Validators.required]
    });
  }

  addCourse(course): void {
    console.log(course);
    this.courseService.createCourse(new Course(
      null,
      course.title,
      course.description,
      new Date(course.startDate.year, course.startDate.month-1, course.startDate.day),
      new Date(course.endDate.year, course.endDate.month-1, course.endDate.day)
      )
    ).subscribe(obj => {
      this._location.back();
    });
  }

  back(e):void {
    e.preventDefault();
    this._location.back();
  }

}
