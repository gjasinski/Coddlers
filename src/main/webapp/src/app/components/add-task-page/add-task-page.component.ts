import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {TaskService} from "../../services/task.service";
import {Location} from '@angular/common';
import {Task} from "../../models/task";

@Component({
  selector: 'cod-add-task-page',
  templateUrl: './add-task-page.component.html',
  styleUrls: ['./add-task-page.component.scss',
    './../../app.component.scss']
})
export class AddTaskPageComponent implements OnInit {
  private formGroup: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private taskService: TaskService,
              private _location: Location) {
  }

  ngOnInit(): void {
    this.formGroup = this.formBuilder.group({
      // TODO delete this line while connecting with assignments
      'assignmentId': '',
      'title': ['', Validators.compose([Validators.required, Validators.minLength(3),
        Validators.maxLength(100)])],
      'description': '',
      'weight': '',
      'maxPoints': ''
    });
  }

  addTask(task): void {
    console.log(task);
    this.taskService.createTask(new Task(null, task.assignmentId,
      task.title, task.description, task.weight, task.maxPoints, "NOT SUBMITTED")
    ).subscribe(obj => {
      this._location.back();
    });
  }

  back(e): void {
    e.preventDefault();
    this._location.back();
  }

}
