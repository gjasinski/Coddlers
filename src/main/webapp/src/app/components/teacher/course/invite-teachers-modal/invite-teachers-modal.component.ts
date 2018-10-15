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

  autocompleteEmails = ['wojtek@gmail.com', 'wojtek1@gmail.com', 'wojtek@2gmail.com', 'wojtek@3gmail.com', 'wojtek@gmail.com3']

  private static hasAt(control: FormControl) {
    if (!control.value.toString().includes('@')) {
      return {
        isProperEmail: true
      };
    }

    return null;
  }

  private static localPartLength(control: FormControl) {
    const email = Email.parseMail(control.value.toString());
    if (email.local.length > 64 || email.local.length <= 0){
      return {
        invalidLengthLocal: true
      }
    }
    return null;
  }

  private static domainPartLength(control: FormControl) {
    const email = Email.parseMail(control.value.toString());
    if (email.domain.length > 255 || email.domain.length <= 0){
      return {
        invalidLengthDomain: true
      }
    }
    return null;
  }

  emailValidators = [InviteTeachersModalComponent.hasAt, InviteTeachersModalComponent.localPartLength,
    InviteTeachersModalComponent.domainPartLength];

  asyncErrorMessages = {
    isProperEmail: 'Please fill with proper email with @',
    invalidLengthLocal: 'Please fill with proper local part of email',
    invalidLengthDomain: 'Please fill with proper domain part of email'
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

  public parseEmail(value: string): Observable<Email> {
    return of(Email.parseMail(value));
  }
}

