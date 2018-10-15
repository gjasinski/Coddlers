import {Component, OnDestroy, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Observable, of, Subscription} from "rxjs";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {TaskService} from "../../../../services/task.service";
import {ActivatedRoute, Router} from "@angular/router";
import {EventService} from "../../../../services/event.service";
import {Event} from "../../../../models/event";
import {Email} from "../../../../models/email";

@Component({
  selector: 'cod-invite-students-modal',
  templateUrl: './invite-students-modal.component.html',
  styleUrls: ['./invite-students-modal.component.scss']
})
export class InviteStudentsModalComponent implements OnInit, OnDestroy {
  private eventSubscription: Subscription;

  @ViewChild('content')
  private modalRef: TemplateRef<any>;
  private modalRefNgb: NgbModalRef;
  formGroup: FormGroup;

  emails: Email[] = [];

  autocompleteEmails = ['wojtek@gmail.com', 'wojtek1@gmail.com', 'wojtek@2gmail.com', 'wojtek@3gmail.com', 'wojtek@gmail.com3']

  private hasAt(control: FormControl) {
    if (!control.value.toString().includes('@')) {
      return {
        'hasAt': true,
        isProperEmail: true
      };
    }

    return null;
  }

  emailValidators = [this.hasAt];

  asyncErrorMessages = {
    isProperEmail: 'Please insert proper email'
  };

  constructor(private formBuilder: FormBuilder,
              private taskService: TaskService,
              private modalService: NgbModal,
              private route: ActivatedRoute,
              private eventService: EventService,
              private router: Router) {
  }

  ngOnInit() {

    this.eventSubscription = this.eventService.events.subscribe((event: Event) => {
      if (event.eventType === 'open-invite-students-modal') {
        this.open();
      }
    });

    this.router.onSameUrlNavigation = 'reload';
  }

  ngOnDestroy(): void {
    this.eventSubscription.unsubscribe();
  }

  open(): void {
    this.modalRefNgb = this.modalService.open(this.modalRef, {size: 'lg', backdrop: 'static'})
  }

  cancel(): void {
    this.modalRefNgb.dismiss();
  }

  sendInvitation(): void {
    if (this.emails.length == 0) return;

    this.emails.forEach( email => {
      console.log(`Sending email to ${email.full()}`);
    });
    this.modalRefNgb.dismiss();
  }
}
