import {Component, OnDestroy, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Subscription} from "rxjs/internal/Subscription";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventService} from "../../../../services/event.service";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'cod-edit-lesson-due-date-modal',
  templateUrl: './edit-lesson-due-date-modal.component.html',
  styleUrls: ['./edit-lesson-due-date-modal.component.scss']
})
export class EditLessonDueDateModalComponent implements OnInit, OnDestroy {
  private formGroup: FormGroup;
  private eventSubscription: Subscription;

  @ViewChild('content')
  private modalRef: TemplateRef<any>;

  private modalRefNgb: NgbModalRef;

  constructor(private modalService: NgbModal,
              private formBuilder: FormBuilder,
              private eventService: EventService) {
  }

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      'startDate': '',
      'endDate': '',
      'lessonLength': ''
    });

    this.eventSubscription = this.eventService.events.subscribe((str: string) => {
      if (str === 'open-edit-lesson-due-date-modal') {
        this.open();
      }
    });
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
}
