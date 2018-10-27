import {Component, OnDestroy, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Subscription} from "rxjs";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormGroup} from "@angular/forms";
import {TaskService} from "../../../../services/task.service";
import {ActivatedRoute, Router} from "@angular/router";
import {EventService} from "../../../../services/event.service";
import {Event} from "../../../../models/event";
import {Email} from "../../../../models/email";
import {InvitationModalValidation} from "../../../../validators/invitation-modal-validation";
import {ValidationMessagesConstants} from "../../../../constants/validation-messages.constants";

@Component({
  selector: 'cod-invite-teachers-modal',
  templateUrl: './invite-teachers-modal.component.html',
  styleUrls: ['./invite-teachers-modal.component.scss']
})
export class InviteTeachersModalComponent implements OnInit, OnDestroy {
  private eventSubscription: Subscription;

  @ViewChild('content')
  private modalRef: TemplateRef<any>;
  private modalRefNgb: NgbModalRef;
  formGroup: FormGroup;

  emails: Email[] = [];
  autocompleteEmails = ['teacher@coddlers.pl'];
  emailValidatorsMessages = ValidationMessagesConstants.asyncErrorMessages;
  emailValidators = InvitationModalValidation.validators;

  constructor(private formBuilder: FormBuilder,
              private taskService: TaskService,
              private modalService: NgbModal,
              private route: ActivatedRoute,
              private eventService: EventService,
              private router: Router) {}

  ngOnInit() {
    this.eventSubscription = this.eventService.events.subscribe((event: Event) => {
      if (event.eventType === 'open-invite-teachers-modal') {
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

    this.emails.forEach(email => {
      console.log(`Sending email to ${email.raw}`);
    });

    this.modalRefNgb.dismiss();
  }
}
