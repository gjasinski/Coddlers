import {Component, OnDestroy, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Subscription} from "rxjs";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormGroup} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {EventService} from "../../../../services/event.service";
import {Event} from "../../../../models/event";
import {Email} from "../../../../models/email";
import {InvitationModalValidation} from "../../../../validators/invitation-modal-validation";
import {ValidationMessagesConstants} from "../../../../constants/validation-messages.constants";
import {InvitationLink} from "../../../../models/invitationLink";
import {InvitationRequest} from "../../../../models/invitationRequest";
import {CourseService} from "../../../../services/course.service";

@Component({
  selector: 'cod-invite-teachers-modal',
  templateUrl: './invite-teachers-modal.component.html',
  styleUrls: ['./invite-teachers-modal.component.scss']
})
export class InviteTeachersModalComponent implements OnInit, OnDestroy {
  public formGroup: FormGroup;
  public emails: Email[] = [];
  public autocompleteEmails = ['teacher@coddlers.pl'];
  public emailValidatorsMessages = ValidationMessagesConstants.asyncErrorMessages;
  public emailValidators = InvitationModalValidation.validators;
  public invitation: InvitationLink = new InvitationLink('');

  @ViewChild('content')
  private modalRef: TemplateRef<any>;
  private modalRefNgb: NgbModalRef;
  private courseId: number;
  private eventSubscription: Subscription;

  constructor(private formBuilder: FormBuilder,
              private modalService: NgbModal,
              private route: ActivatedRoute,
              private router: Router,
              private courseService: CourseService,
              private eventService: EventService) {
  }

  ngOnInit() {
    this.eventSubscription = this.eventService.events.subscribe((event: Event) => {
      if (event.eventType === 'open-invite-teachers-modal') {
        this.courseId = event.eventData;
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
    const invitationRequest: InvitationRequest = new InvitationRequest(this.invitation.link, this.emails.map(email => email.raw));
  }
}
