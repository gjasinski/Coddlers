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
              'title': this.course.title,
              'description': this.course.description,
              // TODO repair displaying date for course
              'startDate': this.datePipe.transform(this.course.startDate, 'yyyy-MM-dd'),
              'endDate': this.datePipe.transform(this.course.endDate, 'yyyy-MM-dd')
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

  saveCourse(course) {
    console.log(course);

    this.courseService.saveCourse(new Course(this.course.id, course.title, course.description,
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
