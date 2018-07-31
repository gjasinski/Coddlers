import {Component, OnInit} from '@angular/core';
import {Course} from "../../../../models/course";
import {Lesson} from "../../../../models/lesson";
import {CourseEdition} from "../../../../models/courseEdition";
import {Task} from "../../../../models/task";
import {CourseService} from "../../../../services/course.service";
import {ActivatedRoute, Router} from "@angular/router";
import {CourseEditionService} from "../../../../services/courseEdition.service";
import {LessonService} from "../../../../services/lesson.service";
import {TaskService} from "../../../../services/task.service";
import {Submission} from "../../../../models/submission";
import {SubmissionService} from "../../../../services/submission.service";
import {switchMap, tap} from "rxjs/operators";

@Component({
  selector: 'app-edition-page',
  templateUrl: './course-edition-page.component.html',
  styleUrls: ['./course-edition-page.component.scss']
})
export class CourseEditionPageComponent implements OnInit {
  private course: Course;
  private edition: CourseEdition;
  private showLesson: boolean[];
  private courseMap: Map<Lesson, Task[]> = new Map<Lesson, Task[]>();
  private submissionsMap: Map<Task, Submission[]> = new Map<Task, Submission[]>();
  private showSubmissionsMap: Map<Task, boolean> = new Map<Task, boolean>();

  constructor(private courseService: CourseService,
              private editionService: CourseEditionService,
              private lessonService: LessonService,
              private taskService: TaskService,
              private submissionService: SubmissionService,
              private router: Router,
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
        this.showSubmissionsMap.clear();
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
              this.showSubmissionsMap.set(task, false);
              this.submissionService.getSubmissions((task.id)).subscribe((submissions: Submission[]) => {
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
      tap((edition: CourseEdition) => this.edition = edition)
    ).subscribe();
  }

  swapShowLesson(index: number) {
    this.showLesson[index] = !this.showLesson[index]
  }

  shouldShowLesson(index: number): boolean {
    return this.showLesson[index]
  }

  getKeys(map) {
    return Array.from(map.keys());
  }

  changeVisibilityForSubmissions(task: Task) {
    this.showSubmissionsMap.set(task, !this.showSubmissionsMap.get(task));
  }

  shouldShowSubmissions(task: Task) {
    return this.showSubmissionsMap.get(task)
  }

  navigateToLesson(lesson: Lesson) {
    this.router.navigate(["courses", this.course.id, "lessons", lesson.id]);
  }
}
