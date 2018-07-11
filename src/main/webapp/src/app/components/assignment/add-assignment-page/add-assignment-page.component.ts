import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AssignmentService} from "../../../services/assignment.service";
import {Location} from "@angular/common";
import {Assignment} from "../../../models/assignment";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-add-assignment-page',
  templateUrl: './add-assignment-page.component.html',
  styleUrls: ['./add-assignment-page.component.scss']
})
export class AddAssignmentPageComponent implements OnInit {
  private formGroup: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private assignmentService: AssignmentService,
              private _location: Location,
              private router: Router,
              private route: ActivatedRoute) { }

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      'title': ['', Validators.compose([Validators.required, Validators.minLength(3),
        Validators.maxLength(100)])],
      'description': '',
      'startDate': [null, Validators.required],
      'endDate': [null, Validators.required],
      'weight': [1, Validators.required]
    });
  }

  addAssignment(assignment): void {
    this.route.parent.params.subscribe(params => {
      this.assignmentService.createAssignment(new Assignment(
        null,
        params.courseId,
        assignment.title,
        assignment.description,
        assignment.weight,
        new Date(assignment.startDate.year, assignment.startDate.month - 1, assignment.startDate.day),
        new Date(assignment.endDate.year, assignment.endDate.month - 1, assignment.endDate.day)
      )).subscribe(obj => {
        this.assignmentService.getAssignments(params.courseId);
        this.router.navigate(['/courses', params.courseId]);
      });

    });
  }

  back(e): void {
    e.preventDefault();
    this._location.back();
  }
}
