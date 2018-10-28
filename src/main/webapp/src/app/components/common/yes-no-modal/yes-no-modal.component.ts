import {Component, EventEmitter, OnDestroy, OnInit, Output, TemplateRef, ViewChild} from '@angular/core';
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {Event} from "../../../models/event";
import {Subscription} from "rxjs/index";
import {EventService} from "../../../services/event.service";

@Component({
  selector: 'cod-yes-no-modal',
  templateUrl: './yes-no-modal.component.html',
  styleUrls: ['./yes-no-modal.component.scss']
})
export class YesNoModalComponent implements OnInit, OnDestroy {
  private eventSubscription: Subscription;
  @ViewChild('content')
  private modalRef: TemplateRef<any>;
  private modalRefNgb: NgbModalRef;
  @Output() confirmed = new EventEmitter<boolean>();

  constructor(private eventService: EventService,
              private modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.eventSubscription = this.eventService.events.subscribe((event: Event) => {
      if (event.eventType === 'open-yes-no-modal') {
        this.open();
      }
    });

  }

  ngOnDestroy(): void {
    this.eventSubscription.unsubscribe();
  }

  open(): void {
    this.modalRefNgb = this.modalService.open(this.modalRef, {size: 'sm', backdrop: 'static', centered: true})
  }

  cancel(): void {
    this.confirmed.emit(false);
    this.modalRefNgb.dismiss();
  }

  confirm(): void{
    this.confirmed.emit(true);
    this.modalRefNgb.dismiss();
  }
}
