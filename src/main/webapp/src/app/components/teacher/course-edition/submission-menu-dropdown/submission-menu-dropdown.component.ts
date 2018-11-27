import {Component, Input, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Subscription} from "rxjs/internal/Subscription";
import {EventService} from "../../../../services/event.service";
import {Submission} from "../../../../models/submission";
import {SubmissionService} from "../../../../services/submission.service";
import {SubmissionStatusEnum} from "../../../../models/submissionStatusEnum";
import {NgbDropdown} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'cod-submission-menu-dropdown',
  templateUrl: './submission-menu-dropdown.component.html',
  styleUrls: ['./submission-menu-dropdown.component.scss']
})

export class SubmissionMenuDropdownComponent implements OnInit {
  @Input() submission: Submission;
  private grade: number;
  private gradeReason: string;
  private reopenReason: string;

  @ViewChild('content')
  private modalRef: TemplateRef<any>;

  constructor(private eventService: EventService, private submissionService: SubmissionService) {
  }

  ngOnInit() {
    this.grade = this.submission.points;
  }

  isGraded(): boolean {
    return this.submission.submissionStatusType.toString() == SubmissionStatusEnum.GRADED.toString();
  }

  gradeSubmission(myDrop: NgbDropdown) {
    this.submissionService.gradeSubmission(this.submission.id, this.gradeReason, this.grade)
      .subscribe(() => {
        myDrop.close();
        this.submission.points = this.grade;
        // TODO use this enum instead of reloading page
        // this.submission.submissionStatusType = SubmissionStatusEnum.GRADED;
        location.reload();
      });
  }

  reopenSubmission(myDrop: NgbDropdown) {
    this.submissionService.reopenSubmission(this.submission.id, this.reopenReason)
      .subscribe(() => {
        myDrop.close();
        // TODO use this enum instead of reloading page
        // this.submission.submissionStatusType = SubmissionStatusEnum.CHANGES_REQUESTED;
        location.reload();
      })
  }
}
