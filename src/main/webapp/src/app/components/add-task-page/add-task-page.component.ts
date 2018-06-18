import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {TaskService} from "../../services/task.service";
import {Location} from '@angular/common';
import {Task} from "../../models/task";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'cod-add-task-page',
  templateUrl: './add-task-page.component.html',
  styleUrls: ['./add-task-page.component.scss']
})
export class AddTaskPageComponent implements OnInit {
  private formGroup: FormGroup;
  private assignmentId: number;

  constructor(private formBuilder: FormBuilder,
              private taskService: TaskService,
              private _location: Location,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.parent.params.subscribe(params => {
      this.assignmentId = params.assignmentId;
    });

    this.formGroup = this.formBuilder.group({
      'title': ['', Validators.compose([Validators.required, Validators.minLength(3),
        Validators.maxLength(100)])],
      'description': '',
      'maxPoints': ''
    });
  }

  addTask(task) {
    this.taskService.createTask(new Task(null,
      this.assignmentId,
      task.title,
      task.description,
      task.maxPoints,
      'NOT SUBMITTED')
    ).subscribe(() => {
      this._location.back();
    });
  }

  back(e): void {
    e.preventDefault();
    this._location.back();
  }

}
