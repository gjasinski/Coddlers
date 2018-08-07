import {Component, OnDestroy, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Subscription} from "rxjs/internal/Subscription";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventService} from "../../../../services/event.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Event} from "../../../../models/event";
import {Lesson} from "../../../../models/lesson";
import {LessonService} from "../../../../services/lesson.service";
import {filter, switchMap, tap} from "rxjs/operators";

@Component({
  selector: 'cod-edit-lesson-due-date-modal',
  templateUrl: './edit-lesson-due-date-modal.component.html',
  styleUrls: ['./edit-lesson-due-date-modal.component.scss']
})
export class EditLessonDueDateModalComponent implements OnInit, OnDestroy {
  private formGroup: FormGroup;
  private eventSubscription: Subscription;
  private lesson: Lesson;

  @ViewChild('content')
  private modalRef: TemplateRef<any>;

  private modalRefNgb: NgbModalRef;

  constructor(private modalService: NgbModal,
              private formBuilder: FormBuilder,
              private eventService: EventService,
              private lessonService: LessonService) {
  }

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      'startDate': '',
      'endDate': '',
      'lessonLength': ''
    });

    this.eventSubscription = this.eventService.events.pipe(
      filter((event: Event) => event.eventType === 'open-edit-lesson-due-date-modal'),
      switchMap((event: Event) => this.lessonService.getLesson(event.eventData)),
      tap((lesson: Lesson) => {
        this.lesson = lesson;
        return this.formGroup = this.formBuilder.group({
          'startDate': '',
          'endDate': '',
          'lessonLength': lesson.timeInDays
        });
      })
    ).subscribe(() => this.open());
  }

  ngOnDestroy() {
    this.eventSubscription.unsubscribe();
  }

  open() {
    this.modalRefNgb = this.modalService.open(this.modalRef);
    this.modalRefNgb.result.then((result) => {
      console.log(`closed ${result}`);
    }, (reason) => {
      console.log(`dismissed ${reason}`);
    });
  }

  saveLesson(lesson) {
    this.lessonService.updateLesson(this.lesson.id,
      new Lesson(
        this.lesson.id,
        this.lesson.title,
        this.lesson.description,
        this.lesson.weight,
        lesson.lessonLength,
        this.lesson.courseId,
        this.lesson.courseVersionNumber
      )).subscribe(() => this.modalRefNgb.close('updated'));
  }
}
