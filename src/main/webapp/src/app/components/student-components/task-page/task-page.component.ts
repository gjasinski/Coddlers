import {Component, OnInit} from '@angular/core';
import {TaskService} from "../../../services/task.service";
import {ActivatedRoute} from "@angular/router";
import {Task} from "../../../models/task";
import {Location} from "@angular/common";

@Component({
  selector: 'cod-task-page',
  templateUrl: './task-page.component.html',
  styleUrls: ['./task-page.component.scss',
    './../../../app.component.scss']
})
export class TaskPageComponent implements OnInit {
  private task: Task;
  private reviewExists: boolean = false;

  constructor(private taskService: TaskService,
              private route: ActivatedRoute,
              private _location: Location) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(
      params => {
        let taskId: number = +params.get('taskId');
        this.taskService.getTask(taskId).subscribe((task: Task) => {
          this.task = task;
          if (this.task.taskStatus == "graded" || this.task.taskStatus == "changes requested")
            this.reviewExists = true;
        });
      }
    );
  }

  back(e) {
    e.preventDefault();
    this._location.back();
  }

}
