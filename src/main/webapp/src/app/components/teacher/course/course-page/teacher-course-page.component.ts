///<reference path="../../../../services/course-version.service.ts"/>
import {Component, OnDestroy, OnInit} from '@angular/core';

import {CourseService} from "../../../../services/course.service";
import {Course} from "../../../../models/course";
import {Lesson} from "../../../../models/lesson";
import {LessonService} from "../../../../services/lesson.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Location} from "@angular/common";
import {CourseVersionService} from "../../../../services/course-version.service";
import {switchMap, tap} from "rxjs/operators";
import {CourseVersion} from "../../../../models/courseVersion";
import {throwError} from "rxjs/internal/observable/throwError";
import {CourseEdition} from "../../../../models/courseEdition";
import {CourseEditionService} from "../../../../services/course-edition.service";
import {Event} from "../../../../models/event";
import {EventService} from "../../../../services/event.service";
import {Subscription} from "rxjs/internal/Subscription";
import {Observable} from "rxjs";
import {SubscriptionManager} from "../../../../utils/SubscriptionManager";


@Component({
  selector: 'cod-teacher-course-page',
  templateUrl: './teacher-course-page.component.html',
  styleUrls: ['./teacher-course-page.component.scss']
})
export class TeacherCoursePageComponent implements OnInit, OnDestroy {
  course: Course;
  lessons: Lesson[] = [];
  courseVersions: CourseVersion[] = [];
  courseEditions: CourseEdition[] = [];
  private currentCourseVersion: CourseVersion;
  currentCourseVersionNumber: number = 0;
  private subscriptionManager: SubscriptionManager = new SubscriptionManager();
  private courseEditionsSub: Subscription;
  private addVersionSubscribtion: Subscription;

  constructor(private courseService: CourseService,
              private route: ActivatedRoute,
              private _location: Location,
              private lessonService: LessonService,
              private router: Router,
              private courseVersionService: CourseVersionService,
              private courseEditionService: CourseEditionService,
              private eventService: EventService) {
  }

  ngOnInit(): void {
    let paramsSub = this.route.paramMap.subscribe(params => {
        let courseId: number = +params.get('courseId');
        let courseSub = this.courseService.getCourse(courseId).subscribe((course: Course) => {
          this.course = course;
        });
        this.subscriptionManager.add(courseSub);

        let courseVerSub = this.courseVersionService.getCourseVersions(courseId).pipe(
          switchMap((courseVersions: CourseVersion[]) => {
            this.courseVersions = courseVersions;
            if (courseVersions.length > 0) {
              this.currentCourseVersion = courseVersions[0];
              this.currentCourseVersionNumber = this.currentCourseVersion.versionNumber;
              return this.lessonService.getLessonsByCourseVersion(courseId, this.currentCourseVersion.versionNumber);
            } else {
              return throwError(`Cannot find any version of course with id ${courseId}`);
            }
          }),
          tap((lessons: Lesson[]) => {
            this.lessons = lessons;
          })
        ).subscribe(() =>  {
          this.courseEditionsSub = this.getCourseEditions().subscribe();
        });
        this.subscriptionManager.add(courseVerSub);
    });
    this.subscriptionManager.add(paramsSub);

    let eventSubscription = this.eventService.events.subscribe((event: Event) => {
      if (event.eventType === 'close-add-edition-modal') {
        this.courseEditionsSub.unsubscribe();
        this.courseEditionsSub = this.getCourseEditions().subscribe();
      }
    });
    this.subscriptionManager.add(eventSubscription);
  }

  private getCourseEditions(): Observable<any> {
    return this.courseEditionService.getEditionsByCourseVersion(this.currentCourseVersion.id)
      .pipe(
        tap(
        courseEditions => {
          this.courseEditions = courseEditions;
        })
      );
  }

  addLesson() {
    this.router.navigate(['add-lesson'], {
      queryParams: {
        courseVersionNumber: this.currentCourseVersion.versionNumber
      },
      relativeTo: this.route
    });
  }

  routeToEdit() {
    this.router.navigate(['/teacher', 'edit-course', this.course.id]);
  }

  changeVersion(version: CourseVersion) {
    this.currentCourseVersion = version;
    this.currentCourseVersionNumber = version.versionNumber;
    this.lessonService.getLessonsByCourseVersion(this.course.id, this.currentCourseVersion.versionNumber);
  }

  back(e) {
    e.preventDefault();
    this.router.navigate(['../'], {relativeTo: this.route});
  }

  addNewEdition() {
    this.eventService.emit(new Event('open-add-edition-modal', this.currentCourseVersion.id));
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribeAll();
    this.courseEditionsSub.unsubscribe();
  }

  addVersion() {
    this.addVersionSubscribtion = this.courseVersionService.createCourseVersion(this.course).subscribe((courseVersion: CourseVersion) => {
      this.courseVersions.push(courseVersion);
      this.courseVersions.sort((a, b) => a.versionNumber - b.versionNumber);
    },
      () =>{},
      () => this.addVersionSubscribtion.unsubscribe());
  }
}
