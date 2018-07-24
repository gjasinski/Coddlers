import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Location} from "@angular/common";
import {ActivatedRoute, Router} from "@angular/router";
import {LessonService} from "../../../../services/lesson.service";
import {Lesson} from "../../../../models/lesson";

@Component({
  selector: 'cod-edit-lesson-page',
  templateUrl: './edit-lesson-page.component.html',
  styleUrls: ['./edit-lesson-page.component.scss']
})
export class EditLessonPageComponent implements OnInit {
  private formGroup: FormGroup;
  private lesson: Lesson;

  constructor(private formBuilder: FormBuilder,
              private lessonService: LessonService,
              private _location: Location,
              private router: Router,
              private route: ActivatedRoute) {}

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      'title': ['', Validators.compose([Validators.required, Validators.minLength(3),
        Validators.maxLength(100)])],
      'description': '',
      'startDate': [null, Validators.required],
      'endDate': [null, Validators.required],
      'weight': [1, Validators.required]
    });

    this.route.parent.params.subscribe(params => {
      this.lessonService.getLesson(params.lessonId)
        .subscribe((lesson: Lesson) => {
          this.lesson = lesson;
          this.setForm(lesson);
        })
    });
  }

  saveLesson(lesson): void {
    this.lessonService.updateLesson(this.lesson.id,
      new Lesson(
        this.lesson.id,
        this.lesson.courseId,
        lesson.title,
        lesson.description,
        lesson.weight,
        new Date(lesson.startDate.year, lesson.startDate.month - 1, lesson.startDate.day),
        new Date(lesson.endDate.year, lesson.endDate.month - 1, lesson.endDate.day)
      )).subscribe(obj => {

      this.router.navigate(['/courses', this.lesson.courseId, 'lessons', this.lesson.id]);
    });
  }

  private setForm(assigment: Lesson) {
      this.formGroup.setValue({
      title: assigment.title,
      description: assigment.description,
      startDate: {
        day: assigment.startDate.getDate(),
        month: assigment.startDate.getMonth() + 1,
        year: assigment.startDate.getFullYear()
      },
      endDate: {
        day: assigment.dueDate.getDate(),
        month: assigment.dueDate.getMonth() + 1,
        year: assigment.dueDate.getFullYear()
      },
      weight: assigment.weight
    });
  }

  back(e): void {
    e.preventDefault();
    this._location.back();
  }
}
