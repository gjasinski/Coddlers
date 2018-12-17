import {Component, OnDestroy, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {EventService} from "../../../services/event.service";
import {Event} from "../../../models/event";
import {Subscription} from "rxjs";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'cod-after-add-to-course-modal',
  templateUrl: './after-add-to-course-modal.component.html',
  styleUrls: ['./after-add-to-course-modal.component.scss']
})
export class AfterAddToCourseModalComponent implements OnInit {
  private eventSubscription: Subscription;
  @ViewChild('content')
  private modalRef: TemplateRef<any>;
  private modalRefNgb: NgbModalRef;
  private headerMsg: string;
  private bodyMsg: string;

  constructor(private modalService: NgbModal,
              private eventService: EventService) {
  }

  ngOnInit() {
    this.eventSubscription = this.eventService.events.subscribe((event: Event) => {
      if (event.eventType === 'open-after-add-to-course-modal') {
        if (event.eventData) {
          this.headerMsg = "Welcome in new course";
          this.bodyMsg = "You have been successfully added to course!";
        } else {
          this.headerMsg = "Something went wrong :(";
          this.bodyMsg = "You haven not been added to any course. Some error occurred. Check your invitation link and check if you are not a member of this course."
        }

        this.open();
      }
    });
  }

  open() {
    this.modalRefNgb = this.modalService.open(this.modalRef);
    this.eventSubscription.unsubscribe();
  }

  close() {
    this.modalRefNgb.close();
  }
}
