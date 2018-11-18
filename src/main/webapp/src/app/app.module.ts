import {BrowserModule} from '@angular/platform-browser';
import {Injector, NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {RouterModule} from '@angular/router';
import {ROUTES} from "./app.routes";
import {CourseService} from "./services/course.service";
import {TeacherCoursesComponent} from "./components/teacher/course/courses/teacher-courses.component";
import {PageNotFoundComponent} from "./components/common/page-not-found/page-not-found.component";
import {CourseFilterPipe} from "./filters/course-filter.pipe";
import {CourseWithCourseEditionFilterPipe} from "./filters/course-with-course-edition-filter.pipe";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NavbarComponent} from "./components/common/navbar/navbar.component";
import {TeacherCoursePageComponent} from "./components/teacher/course/course-page/teacher-course-page.component";
import {LessonService} from "./services/lesson.service";
import {AddCoursePageComponent} from "./components/teacher/course/add-course-page/add-course-page.component";
import {TaskService} from "./services/task.service";
import {AddLessonPageComponent} from './components/teacher/lesson/add-lesson-page/add-lesson-page.component';
import {TeacherLessonPageComponent} from './components/teacher/lesson/lesson-page/teacher-lesson-page.component';
import {EditLessonPageComponent} from './components/teacher/lesson/edit-lesson-page/edit-lesson-page.component';
import {EditCoursePageComponent} from "./components/teacher/course/edit-course-page/edit-course-page.component";
import {DatePipe} from "@angular/common";
import {StudentLessonPageComponent} from './components/student/lesson-page/student-lesson-page.component';
import {LandingPageComponent} from './components/common/landing-page/landing-page.component';
import {HasAnyAuthorityDirective} from './auth/has-any-authority.directive';
import {AuthenticationService} from "./auth/authentication.service";
import {AuthExpiredInterceptor} from "./auth/auth-expired.interceptor";
import {TokenInterceptor} from "./auth/token-interceptor";
import {AccountService} from "./services/account.service";
import {LoginModalComponent} from './components/common/login-modal/login-modal.component';
import {RegisterModalComponent} from './components/common/register-modal/register-modal.component';
import {StudentDashboardComponent} from './components/student/dashboard/student-dashboard.component';
import {TeacherDashboardComponent} from './components/teacher/dashboard/teacher-dashboard.component';
import {CourseEditionPageComponent} from './components/teacher/course-edition/course-edition-page/course-edition-page.component';
import {CourseEditionService} from "./services/course-edition.service";
import {SubmissionService} from "./services/submission.service";
import {StudentLessonRepositoryService} from "./services/student-lesson-repository.service";
import {EditLessonDueDateModalComponent} from "./components/teacher/course-edition/edit-lesson-due-date-modal/edit-lesson-due-date-modal.component";
import {AddTaskModalComponent} from "./components/teacher/task/add-task-modal/add-task-modal.component";
import {EditTaskModalComponent} from "./components/teacher/task/edit-task-modal/edit-task-modal.component";
import {AddEditionModalComponent} from './components/teacher/course-edition/add-edition-modal/add-edition-modal.component';
import {InvitePageComponent} from './components/common/invite-page/invite-page.component';
import {AfterAddToCourseModalComponent} from './components/common/after-add-to-course-modal/after-add-to-course-modal.component';
import {InviteStudentsModalComponent} from "./components/teacher/course-edition/invite-students-modal/invite-students-modal.component";
import {TagInputModule} from "ngx-chips";
import {ClipboardModule} from 'ngx-clipboard';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {InviteTeachersModalComponent} from './components/teacher/course/invite-teachers-modal/invite-teachers-modal.component';
import { StudentMyCoursesComponent } from './components/student/student-my-courses/student-my-courses.component';
import {YesNoModalComponent} from './components/common/yes-no-modal/yes-no-modal.component';
import {SubmissionMenuModalComponent } from './components/teacher/course-edition/submission-menu-modal/submission-menu-modal.component';
import {SubmissionReviewPageComponent} from "./components/teacher/course-edition/submission-review-page/submission-review-page.component";

@NgModule({
  declarations: [
    AppComponent,
    TeacherCoursesComponent,
    PageNotFoundComponent,
    CourseFilterPipe,
    CourseWithCourseEditionFilterPipe,
    NavbarComponent,
    TeacherCoursePageComponent,
    AddCoursePageComponent,
    AddTaskModalComponent,
    EditTaskModalComponent,
    AddLessonPageComponent,
    TeacherLessonPageComponent,
    EditLessonPageComponent,
    EditCoursePageComponent,
    StudentLessonPageComponent,
    CourseEditionPageComponent,
    LandingPageComponent,
    HasAnyAuthorityDirective,
    LoginModalComponent,
    RegisterModalComponent,
    YesNoModalComponent,
    EditLessonDueDateModalComponent,
    StudentDashboardComponent,
    TeacherDashboardComponent,
    AddEditionModalComponent,
    InvitePageComponent,
    AfterAddToCourseModalComponent,
    InviteStudentsModalComponent,
    InviteTeachersModalComponent,
    StudentMyCoursesComponent,
    SubmissionMenuModalComponent,
    SubmissionReviewPageComponent
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
    NgbModule.forRoot(),
    TagInputModule,
    ClipboardModule,
    BrowserAnimationsModule
  ],
  providers: [
    CourseService,
    LessonService,
    TaskService,
    CourseEditionService,
    SubmissionService,
    DatePipe,
    AuthenticationService,
    AccountService,
    StudentLessonRepositoryService,
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
