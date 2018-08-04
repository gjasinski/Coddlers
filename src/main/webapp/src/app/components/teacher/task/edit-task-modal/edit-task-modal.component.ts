import {Component, OnDestroy, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Subscription} from "rxjs/internal/Subscription";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventService} from "../../../../services/event.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Task} from "../../../../models/task";
import {TaskService} from "../../../../services/task.service";
import {ActivatedRoute} from "@angular/router";

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
              private eventService: EventService) {
  }

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      'title': ['', Validators.compose([Validators.required, Validators.minLength(3),
        Validators.maxLength(100)])],
      'description': '',
      'maxPoints': '',
      'isCodeTask': true
    });

    this.eventSubscription = this.eventService.events.subscribe((str: string) => {
      if (str === 'open-edit-task-modal') {
        this.open();
      }
    });

    this.eventSubscription = this.eventService.eventsTask.subscribe((task: Task) => {
      this.task = task;

      this.formGroup.setValue({
        'title': this.task.title,
        'description': this.task.description,
        'maxPoints': this.task.maxPoints,
        'isCodeTask': this.task.isCodeTask
      });
    });
  }

  ngOnDestroy() {
    this.eventSubscription.unsubscribe();
  }

  open() {
    this.modalRefNgb = this.modalService.open(this.modalRef);
  }

  addTask(task) {
    this.taskService.updateTask(new Task(this.task.id,
      this.task.lessonId,
      task.title,
      task.description,
      task.maxPoints,
      task.isCodeTask)
    ).subscribe(() => {
      this.modalRefNgb.close('updated');
    });
  }
}
