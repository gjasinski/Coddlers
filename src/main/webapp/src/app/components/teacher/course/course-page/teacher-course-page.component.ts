import {Component, OnInit} from '@angular/core';

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


@Component({
  selector: 'cod-teacher-course-page',
  templateUrl: './teacher-course-page.component.html',
  styleUrls: ['./teacher-course-page.component.scss']
})
export class TeacherCoursePageComponent implements OnInit {
  private course: Course;
  private lessons: Lesson[] = [];
  private courseVersions: CourseVersion[] = [];
  private currentCourseVersion: CourseVersion;

  constructor(private courseService: CourseService,
              private route: ActivatedRoute,
              private _location: Location,
              private lessonService: LessonService,
              private router: Router,
              private courseVersionService: CourseVersionService) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(
      params => {
        let courseId: number = +params.get('courseId');
        this.courseService.getCourse(courseId).subscribe((course: Course) => {
          this.course = course;
        });

        this.courseVersionService.getCourseVersions(courseId).pipe(
          switchMap((courseVersions: CourseVersion[]) => {
            this.courseVersions = courseVersions;
            if (courseVersions.length > 0) {
              this.currentCourseVersion = courseVersions[0];
              return this.lessonService.getLessonsByCourseVersion(courseId, this.currentCourseVersion.versionNumber);
            } else {
              return throwError(`Cannot find any version of course with id ${courseId}`);
            }
          }),
          tap((lessons: Lesson[]) => {
            this.lessons = lessons;
          })
        ).subscribe();
      }
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

  back(e) {
    e.preventDefault();
    this.router.navigate(['../'], {relativeTo: this.route});
  }
}
