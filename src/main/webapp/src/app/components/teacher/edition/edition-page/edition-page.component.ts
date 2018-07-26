import {Component, OnInit} from '@angular/core';
import {Course} from "../../../../models/course";
import {Lesson} from "../../../../models/lesson";
import {Edition} from "../../../../models/edition";
import {Task} from "../../../../models/task";
import {CourseService} from "../../../../services/course.service";
import {ActivatedRoute} from "@angular/router";
import {EditionService} from "../../../../services/edition.service";
import {LessonService} from "../../../../services/lesson.service";
import {TaskService} from "../../../../services/task.service";
import {Submission} from "../../../../models/submission";
import {SubmissionService} from "../../../../services/submission.service";

@Component({
  selector: 'app-edition-page',
  templateUrl: './edition-page.component.html',
  styleUrls: ['./edition-page.component.scss']
})
export class EditionPageComponent implements OnInit {
  private course: Course;
  private edition: Edition;
  private showLesson: boolean[];
  private courseMap: Map<Lesson, Task[]> = new Map<Lesson, Task[]>();
  private submissionsMap: Map<Task, Submission[]> = new Map<Task, Submission[]>();
  private showSubmissions: Map<Task, boolean> = new Map<Task, boolean>();

  constructor(private courseService: CourseService,
              private editionService: EditionService,
              private lessonService: LessonService,
              private taskService: TaskService,
              private submissionService: SubmissionService,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.parent.params.subscribe(params => {
      this.courseService.getCourse(params.courseId).subscribe((course: Course) => {
        this.course = course;

        this.lessonService.getLessons(course.id).subscribe((lessons: Lesson[]) => {
          lessons.forEach(lesson => {
            this.taskService.getTasks(lesson.id).subscribe((tasks: Task[]) => {
              this.courseMap.set(lesson, tasks);
              tasks.forEach(task => {
                this.showSubmissions.set(task,false);
                this.submissionService.getSubmissions((task.id)).subscribe((submissions: Submission[]) => {
                  this.submissionsMap.set(task, submissions);
                })
              })
            })
          });

          this.showLesson = new Array(this.courseMap.size).fill(false);
        })
      });
      // TODO Don't know why it's not working. (params.editionId = undefined)
      // this.editionService.getEdition(params.editionId).subscribe((edition: Edition) => {
      //   this.edition = edition;
      // });
    });

    this.route.paramMap.subscribe(
      params => {
        let editionId: number = +params.get('editionId');
        this.editionService.getEdition(editionId).subscribe((edition: Edition) => {
          this.edition = edition;
        });
      }
    );
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
    this.showSubmissions.set(task, !this.showSubmissions.get(task));
  }

  shouldShowSubmissions(task: Task) {
    return this.showSubmissions.get(task)
  }
}
