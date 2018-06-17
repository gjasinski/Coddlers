import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Location} from "@angular/common";
import {ActivatedRoute, Router} from "@angular/router";
import {AssignmentService} from "../../../services/assignment.service";
import {Assignment} from "../../../models/assignment";

@Component({
  selector: 'app-edit-assignment-page',
  templateUrl: './edit-assignment-page.component.html',
  styleUrls: ['./edit-assignment-page.component.scss']
})
export class EditAssignmentPageComponent implements OnInit {
  private formGroup: FormGroup;
  private assignment: Assignment;

  constructor(private formBuilder: FormBuilder,
              private assignmentService: AssignmentService,
              private _location: Location,
              private router: Router,
              private route: ActivatedRoute) {}

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      'title': ['', Validators.compose([Validators.required, Validators.minLength(3),
        Validators.maxLength(100)])],
      'description': '',
      'startDate': [null, Validators.required],
      'endDate': [null, Validators.required],
      'weight': [1, Validators.required]
    });

    this.route.parent.params.subscribe(params => {
      this.assignmentService.getAssignment(params.assignmentId)
        .subscribe((assignment: Assignment) => {
          this.assignment = assignment;
          this.setForm(assignment);
        })
    });
  }

  saveAssignment(assignment): void {
    this.assignmentService.updateAssignment(this.assignment.id,
      new Assignment(
        this.assignment.id,
        this.assignment.courseId,
        assignment.title,
        assignment.description,
        assignment.weight,
        new Date(assignment.startDate.year, assignment.startDate.month - 1, assignment.startDate.day),
        new Date(assignment.endDate.year, assignment.endDate.month - 1, assignment.endDate.day)
      )).subscribe(obj => {

      this.router.navigate(['/courses', this.assignment.courseId, 'assignments', this.assignment.id]);
    });
  }

  private setForm(assigment: Assignment) {
    console.log(assigment);

    this.formGroup.setValue({
      title: assigment.title,
      description: assigment.description,
      startDate: {
        day: assigment.startDate.getDate(),
        month: assigment.startDate.getMonth() + 1,
        year: assigment.startDate.getFullYear()
      },
      endDate: {
        day: assigment.dueDate.getDate(),
        month: assigment.dueDate.getMonth() + 1,
        year: assigment.dueDate.getFullYear()
      },
      weight: assigment.weight
    });
  }

  back(e): void {
    e.preventDefault();
    this._location.back();
  }
}
