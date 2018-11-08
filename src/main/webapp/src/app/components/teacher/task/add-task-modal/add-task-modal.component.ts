import {Component, OnDestroy, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Subscription} from "rxjs/internal/Subscription";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventService} from "../../../../services/event.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Task} from "../../../../models/task";
import {TaskService} from "../../../../services/task.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Event} from "../../../../models/event";

@Component({
  selector: 'cod-add-task-modal',
  templateUrl: './add-task-modal.component.html',
  styleUrls: ['./add-task-modal.component.scss']
})
export class AddTaskModalComponent implements OnInit, OnDestroy {
  private eventSubscription: Subscription;

  @ViewChild('content')
  private modalRef: TemplateRef<any>;
  private modalRefNgb: NgbModalRef;
  formGroup: FormGroup;
  private lessonId: number;

  constructor(private formBuilder: FormBuilder,
              private taskService: TaskService,
              private modalService: NgbModal,
              private route: ActivatedRoute,
              private eventService: EventService,
              private router: Router) {
  }

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      'title': ['', Validators.compose([Validators.required, Validators.minLength(3),
        Validators.maxLength(100)])],
      'description': '',
      'maxPoints': '',
      'isCodeTask': true
    });

    this.eventSubscription = this.eventService.events.subscribe((event: Event) => {
      if (event.eventType === 'open-add-task-modal') {
        this.open();
        this.lessonId = event.eventData;
      }
    });

    this.router.onSameUrlNavigation = 'reload';
  }

  ngOnDestroy() {
    this.eventSubscription.unsubscribe();
  }

  open() {
    this.modalRefNgb = this.modalService.open(this.modalRef);
  }

  cancel(): void {
    this.modalRefNgb.dismiss();
    this.resetForm();
  }

  addTask(task) {
    this.taskService.createTask(new Task(null,
      this.lessonId,
      task.title,
      task.description,
      task.maxPoints,
      task.isCodeTask,
      task.branchNamePrefix)
    ).subscribe(() => {
      this.modalRefNgb.close('created');
      this.resetForm();
      this.router.navigate([this.router.url]);
    });
  }

  private resetForm(): void {
    this.formGroup.reset({
      'title': '',
      'description': '',
      'maxPoints': '',
      'isCodeTask': true
    });
  }
}
