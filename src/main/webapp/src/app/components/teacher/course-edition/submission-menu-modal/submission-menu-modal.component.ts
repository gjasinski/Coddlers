import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Subscription} from "rxjs/internal/Subscription";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventService} from "../../../../services/event.service";
import {Event} from "../../../../models/event";
import {Submission} from "../../../../models/submission";
import {SubmissionService} from "../../../../services/submission.service";
import {SubmissionStatusEnum} from "../../../../models/submissionStatusEnum";

@Component({
  selector: 'cod-submission-menu-modal',
  templateUrl: './submission-menu-modal.component.html',
  styleUrls: ['./submission-menu-modal.component.scss']
})

export class SubmissionMenuModalComponent implements OnInit {
  private eventSubscription: Subscription;
  private submission: Submission;
  private showReopenReason: boolean;
  private showInputGrade: boolean;
  private showEditGrade: boolean;
  readonly defaultModalWidth: number = 160;
  readonly biggerModalWidth: number = 220;
  private grade: number;
  private gradeReason: string;
  private reopenReason: string;

  @ViewChild('content')
  private modalRef: TemplateRef<any>;

  private modalRefNgb: NgbModalRef;

  constructor(private modalService: NgbModal,
              private eventService: EventService,
              private submissionService: SubmissionService) {
  }

  ngOnInit() {
    this.eventSubscription = this.eventService.events.subscribe((event: Event) => {
      if (event.eventType === 'open-submission-menu-modal') {
        this.submission = event.eventData;
        this.open();
        this.showReopenReason = false;
        this.showEditGrade = false;
        this.showInputGrade = false;
        this.grade = this.submission.points;
      }
    });
  }

  ngOnDestroy() {
    this.eventSubscription.unsubscribe();
  }

  open() {
    this.modalRefNgb = this.modalService.open(this.modalRef, {
      windowClass: 'submission-menu-modal',
      backdropClass: 'submission-menu-backdrop'
    });
  }

  isGraded(): boolean {
    return this.submission.submissionStatus.toString() === SubmissionStatusEnum.GRADED.toDescription();
  }

  gradeSubmission() {
    this.submissionService.gradeSubmission(this.submission.id, this.gradeReason, this.grade)
      .subscribe(() => {
        this.modalRefNgb.close('graded');
        location.reload();
      });
  }

  reopenSubmission() {
    this.submissionService.reopenSubmission(this.submission.id, this.reopenReason)
      .subscribe(() => {
        this.modalRefNgb.close('reopened');
        location.reload();
      })
  }

  changeReopen(): void {
    this.showReopenReason = !this.showReopenReason;
    this.changeModalDialogWidth();
  }

  changeShowInputGrade(): void {
    this.showInputGrade = !this.showInputGrade;
    this.changeModalDialogWidth();
  }

  changeShowEditGrade(): void {
    this.showEditGrade = !this.showEditGrade;
    this.changeModalDialogWidth();
  }

  changeModalDialogWidth(): void {
    if (this.showEditGrade || this.showInputGrade || this.showReopenReason)
      document.getElementsByClassName("modal-dialog").item(0).setAttribute("style", "width: " + this.biggerModalWidth + "px;")
    else
      document.getElementsByClassName("modal-dialog").item(0).setAttribute("style", "width: " + this.defaultModalWidth + "px;")
  }
}
