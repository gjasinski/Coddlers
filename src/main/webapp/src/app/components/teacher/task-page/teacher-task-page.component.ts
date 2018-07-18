import {Component, OnInit} from '@angular/core';
import {TaskService} from "../../../services/task.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Task} from "../../../models/task";
import {Location} from "@angular/common";

@Component({
  selector: 'cod-task-page',
  templateUrl: './teacher-task-page.component.html',
  styleUrls: ['./teacher-task-page.component.scss']
})
export class TeacherTaskPageComponent implements OnInit {
  private task: Task;

  constructor(private taskService: TaskService,
              private route: ActivatedRoute,
              private _location: Location,
              private router: Router) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(
      params => {
        let taskId: number = +params.get('taskId');
        this.taskService.getTask(taskId).subscribe((task: Task) => {
          this.task = task;
        });
      }
    );
  }

  back(e) {
    e.preventDefault();
    this._location.back();
  }

  routeToEdit() {
    this.router.navigate(['edit-task/' + this.task.id]);
  }

}