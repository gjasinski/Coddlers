import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CourseService} from "../../services/course.service";
import {Location} from '@angular/common';
import {Course} from "../../models/course";
import {ActivatedRoute} from "@angular/router";
import {DatePipe} from '@angular/common';

@Component({
  selector: 'cod-edit-task-page',
  templateUrl: './edit-course-page.component.html',
  styleUrls: ['./edit-course-page.component.scss',
    './../../app.component.scss']
})
export class EditCoursePageComponent implements OnInit {
  private formGroup: FormGroup;
  private course: Course;

  constructor(private formBuilder: FormBuilder,
              private courseService: CourseService,
              private route: ActivatedRoute,
              private _location: Location,
              private datePipe: DatePipe) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(
      params => {
        let courseId: number = +params.get('courseId');
        this.courseService.getCourse(courseId).subscribe((course: Course) => {
            this.course = course;

            this.formGroup.setValue({
              title: this.course.title,
              description: this.course.description,
              startDate: {
                day: this.course.startDate.getDate(),
                month: this.course.startDate.getMonth() + 1,
                year: this.course.startDate.getFullYear()
              },
              endDate: {
                day: this.course.endDate.getDate(),
                month: this.course.endDate.getMonth() + 1,
                year: this.course.endDate.getFullYear()
              },
            });
          }
        );
      });

    this.formGroup = this.formBuilder.group({
      'title': ['', Validators.compose([Validators.required, Validators.minLength(3),
        Validators.maxLength(100)])],
      'description': '',
      'startDate': [null, Validators.required],
      'endDate': [null, Validators.required]
    });
  }

  updateCourse(course) {
    this.courseService.updateCourse(new Course(this.course.id, course.title, course.description,
      new Date(course.startDate.year, course.startDate.month - 1, course.startDate.day),
      new Date(course.endDate.year, course.endDate.month - 1, course.endDate.day))
    ).subscribe(obj => {
      this._location.back();
    });
  }

  back(e) {
    e.preventDefault();
    this._location.back();
  }

}
