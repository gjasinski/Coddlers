import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {RouterModule} from '@angular/router';
import {ROUTES} from "./app.routes";
import {CourseService} from "./services/course.service";
import {CoursesComponent} from "./components/courses/courses.component";
import {PageNotFoundComponent} from "./components/page-not-found/page-not-found.component";
import {CourseFilterPipe} from "./filters/course-filter.pipe";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NavbarComponent} from "./components/navbar/navbar.component";
import {CoursePageComponent} from "./components/course/course-page/course-page.component";
import {AssignmentService} from "./services/assignment.service";
import {AddCoursePageComponent} from "./components/course/add-course-page/add-course-page.component";
import {TeacherTaskPageComponent as TeacherTaskPageComponent} from "./components/teacher/task-page/teacher-task-page.component";
import {StudentTaskPageComponent as StudentTaskPageComponent} from "./components/student/task-page/student-task-page.component";
import {AddTaskPageComponent} from "./components/add-task-page/add-task-page.component";
import {TaskService} from "./services/task.service";
import {EditTaskPageComponent} from "./components/edit-task-page/edit-task-page.component";
import { AddAssignmentPageComponent } from './components/assignment/add-assignment-page/add-assignment-page.component';
import { AssignmentPageComponent } from './components/assignment/assignment-page/assignment-page.component';
import { EditAssignmentPageComponent } from './components/assignment/edit-assignment-page/edit-assignment-page.component';
import {EditCoursePageComponent} from "./components/course/edit-course-page/edit-course-page.component";
import {DatePipe} from "@angular/common";
import { StudentAssignmentPageComponent } from './components/student/assignment-page/student-assignment-page.component';

@NgModule({
  declarations: [
    AppComponent,
    CoursesComponent,
    PageNotFoundComponent,
    CourseFilterPipe,
    NavbarComponent,
    CoursePageComponent,
    AddCoursePageComponent,
    TeacherTaskPageComponent,
    StudentTaskPageComponent,
    AddTaskPageComponent,
    EditTaskPageComponent,
    AddAssignmentPageComponent,
    AssignmentPageComponent,
    EditAssignmentPageComponent,
    EditCoursePageComponent,
    StudentAssignmentPageComponent

  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    NgbModule.forRoot(),
    RouterModule.forRoot(
      ROUTES,
      {
        enableTracing: true, // debug
        useHash: true
      }
    ),
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    CourseService,
    AssignmentService,
    TaskService,
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
