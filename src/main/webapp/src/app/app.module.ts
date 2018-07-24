import {BrowserModule} from '@angular/platform-browser';
import {Injector, NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {RouterModule} from '@angular/router';
import {ROUTES} from "./app.routes";
import {CourseService} from "./services/course.service";
import {CoursesComponent} from "./components/common/courses/courses.component";
import {PageNotFoundComponent} from "./components/common/page-not-found/page-not-found.component";
import {CourseFilterPipe} from "./filters/course-filter.pipe";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NavbarComponent} from "./components/common/navbar/navbar.component";
import {CoursePageComponent} from "./components/teacher/course/course-page/course-page.component";
import {LessonService} from "./services/lesson.service";
import {AddCoursePageComponent} from "./components/teacher/course/add-course-page/add-course-page.component";
import {TeacherTaskPageComponent as TeacherTaskPageComponent} from "./components/teacher/task/task-page/teacher-task-page.component";
import {StudentTaskPageComponent as StudentTaskPageComponent} from "./components/student/task-page/student-task-page.component";
import {AddTaskPageComponent} from "./components/teacher/task/add-task-page/add-task-page.component";
import {TaskService} from "./services/task.service";
import {EditTaskPageComponent} from "./components/teacher/task/edit-task-page/edit-task-page.component";
import { AddLessonPageComponent } from './components/teacher/lesson/add-lesson-page/add-lesson-page.component';
import { LessonPageComponent } from './components/teacher/lesson/lesson-page/lesson-page.component';
import { EditLessonPageComponent } from './components/teacher/lesson/edit-lesson-page/edit-lesson-page.component';
import {EditCoursePageComponent} from "./components/teacher/course/edit-course-page/edit-course-page.component";
import {DatePipe} from "@angular/common";
import { StudentLessonPageComponent } from './components/student/lesson-page/student-lesson-page.component';
import { LandingPageComponent } from './components/common/landing-page/landing-page.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import {AuthenticationService} from "./auth/authentication.service";
import {AuthExpiredInterceptor} from "./auth/auth-expired.interceptor";
import {TokenInterceptor} from "./auth/token-interceptor";
import {AccountService} from "./services/account.service";
import { SignInModalComponent } from './components/common/sign-in-modal/sign-in-modal.component';
import { SignUpModalComponent } from './components/common/sign-up-modal/sign-up-modal.component';
import { StudentDashboardComponent } from './components/student/dashboard/student-dashboard.component';
import { TeacherDashboardComponent } from './components/teacher/dashboard/teacher-dashboard.component';

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
    AddLessonPageComponent,
    LessonPageComponent,
    EditLessonPageComponent,
    EditCoursePageComponent,
    StudentLessonPageComponent,
    LandingPageComponent,
    HasAnyAuthorityDirective,
    SignInModalComponent,
    SignUpModalComponent,
    StudentDashboardComponent,
    TeacherDashboardComponent
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
    ReactiveFormsModule,
    NgbModule.forRoot()
  ],
  providers: [
    CourseService,
    LessonService,
    TaskService,
    DatePipe,
    AuthenticationService,
    AccountService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthExpiredInterceptor,
      multi: true,
      deps: [
        Injector
      ]
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
