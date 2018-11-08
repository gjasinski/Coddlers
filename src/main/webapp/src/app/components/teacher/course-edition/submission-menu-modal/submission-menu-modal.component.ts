import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Subscription} from "rxjs/internal/Subscription";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventService} from "../../../../services/event.service";
import {FormBuilder} from "@angular/forms";
import {Event} from "../../../../models/event";
import {Submission} from "../../../../models/submission";

@Component({
  selector: 'cod-submission-menu-modal',
  templateUrl: './submission-menu-modal.component.html',
  styleUrls: ['./submission-menu-modal.component.scss']
})
export class SubmissionMenuModalComponent implements OnInit {
  private eventSubscription: Subscription;
  private submission: Submission;
  public xCoordinate: string;

  @ViewChild('content')
  private modalRef: TemplateRef<any>;

  private modalRefNgb: NgbModalRef;

  constructor(private modalService: NgbModal,
              private formBuilder: FormBuilder,
              private eventService: EventService) {
  }

  ngOnInit() {
    this.eventSubscription = this.eventService.events.subscribe((event: Event) => {
      if (event.eventType === 'open-submission-menu-modal') {
        this.submission = event.eventData;
        this.open();
      }
    });
  }

  ngOnDestroy() {
    this.eventSubscription.unsubscribe();
  }

  open() {
    this.modalRefNgb = this.modalService.open(this.modalRef, {windowClass: 'my-modal', backdropClass: 'my-backdrop'});

    this.modalRefNgb.result.then((result) => {
      console.log(`closed ${result}`);
    }, (reason) => {
      console.log(`dismissed ${reason}`);
    });
  }

  isGraded(): boolean{
    return this.submission.submissionStatusType.nameWithUnderscores == "graded";
  }
}
