import {Component, OnInit} from '@angular/core';
import {Course} from "../../../../models/course";
import {CourseEdition} from "../../../../models/courseEdition";
import {CourseService} from "../../../../services/course.service";
import {ActivatedRoute, Router} from "@angular/router";
import {CourseEditionService} from "../../../../services/course-edition.service";
import {LessonService} from "../../../../services/lesson.service";
import {TaskService} from "../../../../services/task.service";
import {switchMap} from "rxjs/operators";
import {SubscriptionManager} from "../../../../utils/SubscriptionManager";
import {forkJoin} from "rxjs";
import {Location} from "@angular/common";
import {SubmissionService} from "../../../../services/submission.service";
import {GitFileContent} from "../../../../models/gitFileContent";
import {Submission} from "../../../../models/submission";
import {Task} from '../../../../models/task';
import {SubmissionStatusEnum} from "../../../../models/submissionStatusEnum";
import {library} from "@fortawesome/fontawesome-svg-core";
import {faAngleDown, faAngleUp} from "@fortawesome/free-solid-svg-icons";


@Component({
  selector: 'app-edition-page',
  templateUrl: './submission-review-page.component.html',
  styleUrls: ['./submission-review-age.component.scss']
})
export class SubmissionReviewPageComponent implements OnInit {
  private subscriptionManager: SubscriptionManager = new SubscriptionManager();
  private course: Course;
  private submissionId: number;
  courseEdition: CourseEdition;
  filesContent: GitFileContent[] = [];
  filesVisibility: boolean[] = new Array(this.filesContent.length);
  submission: Submission;
  fullName: string;
  numberOfFiles: number;
  task: Task = null;
  gradePoints: number = 0;
  showWarning: boolean = false;
  warningMessage: string = "";
  disableRequestChanges = false;

  constructor(private courseService: CourseService,
              private lessonService: LessonService,
              private route: ActivatedRoute,
              private router: Router,
              private _location: Location,
              private taskService: TaskService,
              private courseEditionService: CourseEditionService,
              private submissionService: SubmissionService) {
    library.add(faAngleUp, faAngleDown);
  }

  ngOnInit() {
    let paramParentMapSubscription = this.route.parent.paramMap
      .pipe(switchMap((params) => forkJoin(
        this.courseService.getCourseByCourseEditionId(+params.get('editionId')),
        this.courseEditionService.getCourseEdition(+params.get('editionId')),
      )))
      .subscribe(([course, courseEdition]) => {
        this.course = course;
        this.courseEdition = courseEdition;
      });
    let paramMapSubscription = this.route.paramMap
      .pipe(switchMap((params) => {
          this.submissionId = +params.get("submissionId");
          return this.submissionService.getSubmission(this.submissionId);
        }),
        switchMap((submission) => {
          this.filesContent = submission.gitFileContents;
          this.submission = submission.submission;
          this.fullName = submission.fullName;
          this.numberOfFiles = this.filesContent.length;
          this.disableRequestChanges = this.submission.submissionStatusType.toString() !== SubmissionStatusEnum.WAITING_FOR_REVIEW.description;
          return this.taskService.getTask(this.submission.taskId);
        })
      )
      .subscribe((task) => {
        this.task = task;
      });
    this.subscriptionManager.add(paramMapSubscription);

    this.subscriptionManager.add(paramParentMapSubscription);

  }

  back(e) {
    e.preventDefault();
    this._location.back();
  }

  navigateToCourseEdition() {
    this.router.navigate(["teacher", "courses", this.course.id, "editions", this.courseEdition.id]);
  }

  changeVisibilityForFile(file: number) {
    this.filesVisibility[file] = !this.filesVisibility[file];
  }

  saveReview(comment, isComment, isGrade) {
    let subscription;
    if (isComment) {
      subscription = this.submissionService.createComment(this.submissionId, comment);
    }
    else if (isGrade) {
      subscription = this.submissionService.gradeSubmission(this.submissionId, comment, this.gradePoints);
    }
    else {
      subscription = this.submissionService.requestChangesForSubmission(this.submissionId, comment);
    }
    subscription = subscription.pipe(switchMap(() => this.submissionService.getSubmission(this.submissionId)))
      .subscribe(() => this.disableRequestChanges = this.submission.submissionStatusType.toString() !== SubmissionStatusEnum.WAITING_FOR_REVIEW.description);
    this.subscriptionManager.add(subscription);
  }

  validateGradePoints() {
    if (this.gradePoints > this.task.maxPoints) {
      this.warningMessage = "Maximum number of points: " + this.task.maxPoints;
      this.gradePoints = this.task.maxPoints;
      this.showWarning = true;
    }
    else {
      this.showWarning = false;
    }
    if (this.gradePoints < 0) {
      this.warningMessage = "You cannot set negative number of points";
      this.showWarning = true;
      this.gradePoints = 0;
    }

    if (this.gradePoints === null) {
      this.warningMessage = "Number of points must be number";
      this.showWarning = true;
      this.gradePoints = 0;
    }
  }

}
