import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Location} from "@angular/common";
import {ActivatedRoute, Router} from "@angular/router";
import {LessonService} from "../../../../services/lesson.service";
import {Lesson} from "../../../../models/lesson";
import {SubscriptionManager} from "../../../../utils/SubscriptionManager";

@Component({
  selector: 'cod-edit-lesson-page',
  templateUrl: './edit-lesson-page.component.html',
  styleUrls: ['./edit-lesson-page.component.scss']
})
export class EditLessonPageComponent implements OnInit, OnDestroy {
  private formGroup: FormGroup;
  private lesson: Lesson;
  private subscriptionManager: SubscriptionManager = new SubscriptionManager();

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
      'weight': [1, Validators.required],
      'timeDays': [1, Validators.required]
    });

    let routeParamsSub = this.route.parent.params.subscribe(params => {
      this.lessonService.getLesson(params.lessonId)
        .subscribe((lesson: Lesson) => {
          this.lesson = lesson;
          this.setForm(lesson);
        })
    });
    this.subscriptionManager.add(routeParamsSub);
  }

  saveLesson(lesson): void {
    this.lessonService.updateLesson(this.lesson.id,
      new Lesson(
        this.lesson.id,
        lesson.title,
        lesson.description,
        lesson.weight,
        lesson.timeDays,
        this.lesson.courseId,
        this.lesson.courseVersionNumber
      )).subscribe(() => {
      this.router.navigate(['/teacher', 'courses', this.lesson.courseId, 'lessons', this.lesson.id]);
    });
  }

  private setForm(lesson: Lesson) {
      this.formGroup.setValue({
      title: lesson.title,
      description: lesson.description,
      weight: lesson.weight,
      timeDays: lesson.timeInDays,
    });
  }

  back(e): void {
    e.preventDefault();
    this._location.back();
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribeAll();
  }
}
