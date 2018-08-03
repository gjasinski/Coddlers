import {Component, OnDestroy, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Subscription} from "rxjs/internal/Subscription";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventService} from "../../../../services/event.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Task} from "../../../../models/task";
import {TaskService} from "../../../../services/task.service";
import {ActivatedRoute} from "@angular/router";

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
  private formGroup: FormGroup;
  private lessonId: number;

  constructor(private formBuilder: FormBuilder,
              private taskService: TaskService,
              private modalService: NgbModal,
              private route: ActivatedRoute,
              private eventService: EventService) {
  }

  ngOnInit() {
    this.route.parent.params.subscribe(params => {
      this.lessonId = params.lessonId;
    });

    this.formGroup = this.formBuilder.group({
      'title': ['', Validators.compose([Validators.required, Validators.minLength(3),
        Validators.maxLength(100)])],
      'description': '',
      'maxPoints': '',
      'isCodeTask': true
    });

    this.eventSubscription = this.eventService.events.subscribe((str: string) => {
      if (str === 'open-add-task-modal') {
        this.open();
      }
    });
  }

  ngOnDestroy() {
    this.eventSubscription.unsubscribe();
  }

  open() {
    this.modalRefNgb = this.modalService.open(this.modalRef);
  }

  addTask(task) {
    this.taskService.createTask(new Task(null,
      this.lessonId,
      task.title,
      task.description,
      task.maxPoints,
      task.isCodeTask)
    ).subscribe(() => {
      this.modalRefNgb.close('created');
    });
  }
}
