import {Component, OnDestroy, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Subscription} from "rxjs/internal/Subscription";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventService} from "../../../../services/event.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Event} from "../../../../models/event";
import {Lesson} from "../../../../models/lesson";
import {LessonService} from "../../../../services/lesson.service";
import {filter, switchMap, tap} from "rxjs/operators";
import {forkJoin} from "rxjs/internal/observable/forkJoin";
import {CourseEditionService} from "../../../../services/course-edition.service";
import {CourseEditionLesson} from "../../../../models/courseEditionLesson";

@Component({
  selector: 'cod-edit-lesson-due-date-modal',
  templateUrl: './edit-lesson-due-date-modal.component.html',
  styleUrls: ['./edit-lesson-due-date-modal.component.scss']
})
export class EditLessonDueDateModalComponent implements OnInit, OnDestroy {
  private formGroup: FormGroup;
  private eventSubscription: Subscription;
  private lesson: Lesson;
  private courseEditionLesson: CourseEditionLesson;

  @ViewChild('content')
  private modalRef: TemplateRef<any>;

  private modalRefNgb: NgbModalRef;

  constructor(private modalService: NgbModal,
              private formBuilder: FormBuilder,
              private eventService: EventService,
              private lessonService: LessonService,
              private courseEditionService: CourseEditionService) {
  }

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      'startDate': '',
      'endDate': '',
      'lessonLength': ''
    });

    this.eventSubscription = this.eventService.events.pipe(
      filter((event: Event) => event.eventType === 'open-edit-lesson-due-date-modal'),
      switchMap((event: Event) => forkJoin([
          this.lessonService.getLesson(event.eventData.lessonId),
          this.courseEditionService.getCourseEditionLesson(event.eventData.editionId, event.eventData.lessonId)
        ])
      ),
      tap(([lesson, courseEditionLesson]: [Lesson, CourseEditionLesson]) => {
        this.lesson = lesson;
        this.courseEditionLesson = courseEditionLesson;

        this.formGroup = this.formBuilder.group({
          'startDate': this.getDateObj(courseEditionLesson.startDate),
          'endDate': this.getDateObj(courseEditionLesson.endDate),
          'lessonLength': this.lesson.timeInDays
        });
      })
    ).subscribe(() => this.open());
  }

  private getDateObj(date: Date): any {
    return {
      year: date.getFullYear(),
      month: date.getUTCMonth() + 1,
      day: date.getUTCDate()
    }
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
    let sub = this.courseEditionService.updateCourseEditionLesson(this.courseEditionLesson.id,
      new CourseEditionLesson(
        this.courseEditionLesson.id,
        this.convertToDate(lesson.startDate),
        this.convertToDate(lesson.endDate),
        this.courseEditionLesson.courseEditionId,
        this.courseEditionLesson.lessonId
      ))
      .subscribe(() => {
        this.modalRefNgb.close('updated');
        this.eventService.emit(new Event('edit-lesson-due-date-updated'));
        sub.unsubscribe();
      });
  }

  private convertToDate(dateObj: any): Date {
    return new Date(dateObj.year, dateObj.month - 1, dateObj.day, 1);
  }
}
