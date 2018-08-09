import {Component, OnInit} from '@angular/core';
import {Course} from "../../../../models/course";
import {Lesson} from "../../../../models/lesson";
import {CourseEdition} from "../../../../models/courseEdition";
import {Task} from "../../../../models/task";
import {Event} from "../../../../models/event";
import {CourseService} from "../../../../services/course.service";
import {ActivatedRoute, Router} from "@angular/router";
import {CourseEditionService} from "../../../../services/course-edition.service";
import {LessonService} from "../../../../services/lesson.service";
import {TaskService} from "../../../../services/task.service";
import {Submission} from "../../../../models/submission";
import {SubmissionService} from "../../../../services/submission.service";
import {switchMap, tap} from "rxjs/operators";
import {EventService} from "../../../../services/event.service";

@Component({
  selector: 'app-edition-page',
  templateUrl: './course-edition-page.component.html',
  styleUrls: ['./course-edition-page.component.scss']
})
export class CourseEditionPageComponent implements OnInit {
  private course: Course;
  private courseEdition: CourseEdition;
  private showLesson: boolean[];
  private courseMap: Map<Lesson, Task[]> = new Map<Lesson, Task[]>();
  private submissionsMap: Map<Task, Submission[]> = new Map<Task, Submission[]>();
  private showTask: boolean[] = [];

  constructor(private courseService: CourseService,
              private editionService: CourseEditionService,
              private lessonService: LessonService,
              private taskService: TaskService,
              private submissionService: SubmissionService,
              private router: Router,
              public eventService: EventService,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.getCourseInfo();
    this.getEditionInfo();
  }

  getCourseInfo() {
    this.route.parent.params.pipe(
      switchMap(params => {
        this.submissionsMap.clear();
        this.courseMap.clear();
        this.showTask = [];
        return this.courseService.getCourse(params.courseId);
      }),
      switchMap((course: Course) => {
        this.course = course;
        return this.lessonService.getLessons(course.id);
      }),
      tap((lessons: Lesson[]) => {
        lessons.forEach(lesson => {
          this.taskService.getTasks(lesson.id).subscribe((tasks: Task[]) => {
            this.courseMap.set(lesson, tasks);
            this.showLesson = new Array(this.courseMap.size).fill(false);
            tasks.forEach(task => {
              this.showTask.push(false);
              this.submissionService.getSubmissions(task.id).subscribe((submissions: Submission[]) => {
                this.submissionsMap.set(task, submissions);
              })
            })
          })
        })
      })
    ).subscribe();
  }

  getEditionInfo() {
    this.route.params.pipe(
      switchMap(params => this.editionService.getCourseEdition(params.editionId)),
      tap((edition: CourseEdition) => this.courseEdition = edition)
    ).subscribe();
  }

  swapShowLesson(index: number) {
    this.showLesson[index] = !this.showLesson[index]
  }

  getKeys(map) {
    return Array.from(map.keys());
  }

  changeVisibilityForSubmissions(index: number) {
    this.showTask[index] = !this.showTask[index];
  }

  navigateToCourse() {
    this.router.navigate(["teacher", "courses", this.course.id]);
  }

  navigateToLesson(lesson: Lesson) {
    this.router.navigate(["teacher", "courses", this.course.id, "lessons", lesson.id]);
  }

  openEditLessonDueDateModal(lesson: Lesson) {
    this.eventService.emit(new Event('open-edit-lesson-due-date-modal', lesson.id));
  }
}
