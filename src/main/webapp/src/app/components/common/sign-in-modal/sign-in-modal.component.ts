import {Component, OnDestroy, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Subscription} from "rxjs/internal/Subscription";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventService} from "../../../services/event.service";
import {AuthenticationService} from "../../../auth/authentication.service";
import {Router} from "@angular/router";
import {throwError} from "rxjs/internal/observable/throwError";
import {Observable} from "rxjs/internal/Observable";
import {catchError} from "rxjs/operators";

@Component({
  selector: 'cod-sign-in-modal',
  templateUrl: './sign-in-modal.component.html',
  styleUrls: ['./sign-in-modal.component.scss']
})
export class SignInModalComponent implements OnInit, OnDestroy {
  private formGroup: FormGroup;
  private eventSubscription: Subscription;
  errorMsg: string = '';

  @ViewChild('content')
  private modalRef: TemplateRef<any>;

  private modalRefNgb: NgbModalRef;

  constructor(private modalService: NgbModal,
              private eventService: EventService,
              private formBuilder: FormBuilder,
              private authService: AuthenticationService,
              private router: Router) {}

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      'email': ['', Validators.compose([Validators.required, Validators.minLength(3),
        Validators.maxLength(50)])],
      'password': ['', Validators.compose([Validators.required, Validators.minLength(3),
        Validators.maxLength(50)])]
    });

    this.eventSubscription = this.eventService.events.subscribe((str: string) => {
      if (str === 'open-sign-in-modal') {
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

  signIn(form): void {
    this.authService.login(form.email, form.password)
      .pipe(
        catchError((err: any, caught: Observable<Response>) => {
          this.errorMsg = 'Failed to login';
          return throwError(err);
        })
      ).subscribe(
      (res) => {
        console.log('logged');
        this.modalRefNgb.close('logged');
        // login guard handles next action depending on user roles
        this.router.navigate(['']);
        this.formGroup.reset();
      }
    );
  }

}
