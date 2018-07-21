import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {LessonService} from "../../../../services/lesson.service";
import {Location} from "@angular/common";
import {Lesson} from "../../../../models/lesson";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'cod-add-lesson-page',
  templateUrl: './add-lesson-page.component.html',
  styleUrls: ['./add-lesson-page.component.scss']
})
export class AddLessonPageComponent implements OnInit {
  private formGroup: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private lessonService: LessonService,
              private _location: Location,
              private router: Router,
              private route: ActivatedRoute) { }

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      'title': ['', Validators.compose([Validators.required, Validators.minLength(3),
        Validators.maxLength(100)])],
      'description': '',
      'startDate': [null, Validators.required],
      'endDate': [null, Validators.required],
      'weight': [1, Validators.required]
    });
  }

  addLesson(lesson): void {
    this.route.parent.params.subscribe(params => {
      this.lessonService.createLesson(new Lesson(
        null,
        params.courseId,
        lesson.title,
        lesson.description,
        lesson.weight,
        new Date(lesson.startDate.year, lesson.startDate.month - 1, lesson.startDate.day),
        new Date(lesson.endDate.year, lesson.endDate.month - 1, lesson.endDate.day)
      )).subscribe(obj => {
        this.lessonService.getLessons(params.courseId);
        this.router.navigate(['/courses', params.courseId]);
      });

    });
  }

  back(e): void {
    e.preventDefault();
    this._location.back();
  }
}
