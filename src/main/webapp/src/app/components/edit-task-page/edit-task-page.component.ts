import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {TaskService} from "../../services/task.service";
import {Location} from '@angular/common';
import {Task} from "../../models/task";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'cod-edit-task-page',
  templateUrl: './edit-task-page.component.html',
  styleUrls: ['./edit-task-page.component.scss',
    './../../app.component.scss']
})
export class EditTaskPageComponent implements OnInit {
  private formGroup: FormGroup;
  private task: Task;

  constructor(private formBuilder: FormBuilder,
              private taskService: TaskService,
              private route: ActivatedRoute,
              private _location: Location) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(
      params => {
        let taskId: number = +params.get('taskId');
        this.taskService.getTask(taskId).subscribe((task: Task) => {
            this.task = task;

            this.formGroup.setValue({
              'title': this.task.title,
              'description': this.task.description,
              'weight': this.task.weight,
              'maxPoints': this.task.maxPoints
            });
          }
        );
      });

    this.formGroup = this.formBuilder.group({
      'title': ['', Validators.compose([Validators.required, Validators.minLength(3),
        Validators.maxLength(100)])],
      'description': '',
      'weight': '',
      'maxPoints': ''
    });
  }

  saveTask(task) {
    console.log(task);

    this.taskService.saveTask(new Task(this.task.id, this.task.assignmentId,
      task.title, task.description, task.weight, task.maxPoints, this.task.taskStatus.toUpperCase())
    ).subscribe(obj => {
      this._location.back();
    });
  }

  back(e)
    :
    void {
    e.preventDefault();
    this._location.back();
  }

}
