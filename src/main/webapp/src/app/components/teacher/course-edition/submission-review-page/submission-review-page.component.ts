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

@Component({
  selector: 'app-edition-page',
  templateUrl: './submission-review-page.component.html',
  styleUrls: ['./submission-review-age.component.scss']
})
export class SubmissionReviewPageComponent implements OnInit {
  private subscriptionManager: SubscriptionManager = new SubscriptionManager();
  private courseEdition: CourseEdition;
  private course: Course;
  private submissionId: number;
  private filesContent: GitFileContent[] = [];
  private filesVisibility: boolean[] = new Array(this.filesContent.length);
  private submission: Submission;
  private fullName: string;
  private numberOfFiles :number;

  constructor(private courseService: CourseService,
              private lessonService: LessonService,
              private route: ActivatedRoute,
              private router: Router,
              private _location: Location,
              private taskService: TaskService,
              private courseEditionService: CourseEditionService,
              private submissionService: SubmissionService) {
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
      .pipe(switchMap((params) => this.submissionService.getSubmission(+params.get("submissionId"))))
      .subscribe((submission) =>{
        this.filesContent = submission.gitFileContents;
        this.submission = submission.submission;
        this.fullName = submission.fullName;
        this.numberOfFiles = this.filesContent.length
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

  changeVisibilityForFile(file: number){
    this.filesVisibility[file] = !this.filesVisibility[file];
  }


}
