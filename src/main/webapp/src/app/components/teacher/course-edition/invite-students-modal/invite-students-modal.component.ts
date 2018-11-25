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
import {CourseEditionService} from "../../../../services/course-edition.service";
import {InvitationLink} from "../../../../models/invitationLink";
import {InvitationRequest} from "../../../../models/invitationRequest";

@Component({
  selector: 'cod-invite-students-modal',
  templateUrl: './invite-students-modal.component.html',
  styleUrls: ['./invite-students-modal.component.scss']
})
export class InviteStudentsModalComponent implements OnInit, OnDestroy {
  public formGroup: FormGroup;
  public emails: Email[] = [];
  public autocompleteEmails = ['student@coddlers.pl'];
  public emailValidatorsMessages = ValidationMessagesConstants.asyncErrorMessages;
  public emailValidators = InvitationModalValidation.validators;
  public invitation: InvitationLink = new InvitationLink('');

  @ViewChild('content')
  private modalRef: TemplateRef<any>;
  private modalRefNgb: NgbModalRef;
  private courseEditionId: number;
  private eventSubscription: Subscription;

  constructor(private formBuilder: FormBuilder,
              private modalService: NgbModal,
              private route: ActivatedRoute,
              private router: Router,
              private courseEditionService: CourseEditionService,
              private eventService: EventService) {
  }

  ngOnInit() {
    this.eventSubscription = this.eventService.events.subscribe((event: Event) => {
      if (event.eventType === 'open-invite-students-modal') {
        this.courseEditionId = event.eventData;
        this.courseEditionService.getInvitationLink(this.courseEditionId).subscribe((invitationLink: InvitationLink) => {
          this.invitation = invitationLink;
        });
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
    this.courseEditionService.sendInvitation(invitationRequest).subscribe(() => {
      this.cancel();
    });
  }
}
