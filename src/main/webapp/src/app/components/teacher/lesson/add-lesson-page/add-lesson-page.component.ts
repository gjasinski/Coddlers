import {Component, OnInit} from '@angular/core';
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
  private courseVersionNumber: number;

  constructor(private formBuilder: FormBuilder,
              private lessonService: LessonService,
              private _location: Location,
              private router: Router,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.courseVersionNumber = +this.route.snapshot.queryParamMap.get('courseVersionNumber');

    this.formGroup = this.formBuilder.group({
      'title': ['', Validators.compose([Validators.required, Validators.minLength(3),
        Validators.maxLength(100)])],
      'description': '',
      'weight': [1, Validators.required],
      'timeDays': [1, Validators.required]
    });
  }

  addLesson(lesson): void {
    this.route.parent.params.subscribe(params => {
      this.lessonService.createLesson(new Lesson(
        null,
        lesson.title,
        lesson.description,
        lesson.weight,
        lesson.timeDays,
        params.courseId,
        this.courseVersionNumber
      )).subscribe(obj => {
        this.lessonService.getLessonsByCourseVersion(params.courseId, this.courseVersionNumber);
        this.router.navigate(['/teacher', 'courses', params.courseId]);
      });

    });
  }

  back(e): void {
    e.preventDefault();
    this._location.back();
  }
}
