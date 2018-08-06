import {Component, OnDestroy, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Subscription} from "rxjs/internal/Subscription";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventService} from "../../../services/event.service";
import {AccountService} from "../../../services/account.service";
import {PasswordValidation} from "../../../validators/password-validation";
import {User} from "../../../models/user";
import {AccountTypesConstants} from "../../../constants/account-types.constants";
import {Event} from "../../../models/event";

@Component({
  selector: 'cod-register-modal',
  templateUrl: './register-modal.component.html',
  styleUrls: ['./register-modal.component.scss']
})
export class RegisterModalComponent implements OnInit, OnDestroy {
  private formGroup: FormGroup;
  private eventSubscription: Subscription;

  @ViewChild('content')
  private modalRef: TemplateRef<any>;

  private modalRefNgb: NgbModalRef;

  constructor(private modalService: NgbModal,
              private eventService: EventService,
              private formBuilder: FormBuilder,
              private accountService: AccountService) {
  }

  ngOnInit(): void {
    this.formGroup = this.formBuilder.group({
      'userRole': ['student', Validators.compose([Validators.required])],
      'firstname': ['', Validators.compose([Validators.required, Validators.minLength(1)])],
      'lastname': ['', Validators.compose([Validators.required, Validators.minLength(1)])],
      'email': ['', Validators.compose([Validators.required, Validators.email, Validators.minLength(4),
        Validators.maxLength(50)])],
      'password': ['', Validators.compose([Validators.required, Validators.minLength(4),
        Validators.maxLength(50)])],
      'passwordRepeat': ['', Validators.compose([Validators.required, Validators.minLength(4),
        Validators.maxLength(50)])],
    }, {
      validator: PasswordValidation.MatchPassword
    });

    this.eventSubscription = this.eventService.events.subscribe((event: Event) => {
      if (event.eventType === 'open-register-modal') {
        this.open();
      }
    });
  }

  ngOnDestroy() {
    this.eventSubscription.unsubscribe();
  }

  open() {
    this.modalRefNgb = this.modalService.open(this.modalRef);
    // this.modalRefNgb.result.then((result) => {
    //   console.log(`closed ${result}`);
    // }, (reason) => {
    //   console.log(`dismissed ${reason}`);
    // });
  }

  register(form): void {
    console.log(form);

    let userRoles: string[] = [];
    if (form.userRole === 'student') {
      userRoles.push(AccountTypesConstants.ROLE_STUDENT);
    } else {
      userRoles.push(AccountTypesConstants.ROLE_TEACHER);
    }

    this.accountService.register(new User(form.email, form.password, form.firstname, form.lastname, userRoles)).subscribe(
      res => {
        console.log(res);
        this.modalRefNgb.close('registered');
        this.formGroup.reset();
      }, err => {
        console.error(err);
      }
    );
  }
}
