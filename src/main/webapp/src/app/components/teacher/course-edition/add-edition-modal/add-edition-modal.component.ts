import {Component, Input, OnDestroy, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {Subscription} from "rxjs/internal/Subscription";
import {Event} from "../../../../models/event";
import {EventService} from "../../../../services/event.service";
import {CourseVersion} from "../../../../models/courseVersion";
import {CourseEditionService} from "../../../../services/course-edition.service";
import {CourseEdition} from "../../../../models/courseEdition";

@Component({
  selector: 'cod-add-edition-modal',
  templateUrl: './add-edition-modal.component.html',
  styleUrls: ['./add-edition-modal.component.scss']
})
export class AddEditionModalComponent implements OnInit, OnDestroy {
  @ViewChild('content')
  private modalRef: TemplateRef<any>;
  @Input()
  courseVersions: CourseVersion[];
  @Input()
  currentCourseVersion: CourseVersion;
  private eventSubscription: Subscription;
  private modalRefNgb: NgbModalRef;
  formGroup: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private modalService: NgbModal,
              private route: ActivatedRoute,
              private eventService: EventService,
              private router: Router,
              private courseEditionService: CourseEditionService) {
  }

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      'title': ['', Validators.compose([Validators.required, Validators.minLength(3),
        Validators.maxLength(255)])],
      'date': [this.getTodaysDate(), Validators.compose([Validators.required])]
    });
    this.eventSubscription = this.eventService.events.subscribe((event: Event) => {
      if (event.eventType === 'open-add-edition-modal') {
        this.open();
      }
    });
    this.router.onSameUrlNavigation = 'reload';

  }

  ngOnDestroy() {
    this.eventSubscription.unsubscribe();
  }

  open() {
    this.modalRefNgb = this.modalService.open(this.modalRef);
    this.formGroup.reset({
      title: '',
      date: this.getTodaysDate()
    });
  }

  private getTodaysDate(): any {
    let date = new Date;

    return {
      day: date.getUTCDate(),
      month: date.getUTCMonth() + 1,
      year: date.getUTCFullYear()
    };
  }

  changeVersion(version: CourseVersion) {
    this.currentCourseVersion = version;
  }

  addEdition(courseEdition) {
    let { day, month, year } = courseEdition.date;
    let c = new CourseEdition(null,
      courseEdition.title,
      this.currentCourseVersion,
     new Date(year, month - 1, day));
    this.courseEditionService.createCourseEdition(c)
      .subscribe(() => {
        this.modalRefNgb.close('created');
        this.formGroup.reset();
        this.router.navigate([this.router.url]);
        this.eventService.emit(new Event("close-add-edition-modal"))
      });
  }
}
