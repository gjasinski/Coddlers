import {Component, OnDestroy, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Subscription} from "rxjs/internal/Subscription";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventService} from "../../../../services/event.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Task} from "../../../../models/task";
import {Event} from "../../../../models/event";
import {TaskService} from "../../../../services/task.service";
import {ActivatedRoute, Router} from "@angular/router";
import {filter, switchMap, tap} from "rxjs/operators";

@Component({
  selector: 'cod-edit-task-modal',
  templateUrl: './edit-task-modal.component.html',
  styleUrls: ['./edit-task-modal.component.scss']
})
export class EditTaskModalComponent implements OnInit, OnDestroy {
  private eventSubscription: Subscription;

  @ViewChild('content')
  private modalRef: TemplateRef<any>;
  private modalRefNgb: NgbModalRef;
  private formGroup: FormGroup;
  private task: Task;

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

    this.eventSubscription = this.eventService.events.pipe(
      filter((event: Event) => event.eventType === 'open-edit-task-modal'),
      switchMap((event: Event) =>
        this.taskService.getTask(event.eventData)
      ),
      tap((task: Task) => {
          this.task = task;
          return this.formGroup.setValue({
            'title': task.title,
            'description': task.description,
            'maxPoints': task.maxPoints,
            'isCodeTask': task.isCodeTask
          })
        }
      )
    ).subscribe(() => this.open());

    this.router.onSameUrlNavigation = 'reload';
  }

  ngOnDestroy() {
    this.eventSubscription.unsubscribe();
  }

  open() {
    this.modalRefNgb = this.modalService.open(this.modalRef);
  }

  updateTask(task) {
    this.taskService.updateTask(new Task(this.task.id,
      this.task.lessonId,
      task.title,
      task.description,
      task.maxPoints,
      task.isCodeTask)
    ).subscribe(() => {
        this.modalRefNgb.close('updated');
        this.router.navigate([this.router.url]);
      }
    );
  }
}
