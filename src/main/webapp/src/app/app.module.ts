import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { RouterModule } from '@angular/router';
import {ROUTES} from "./app.routes";
import {CourseService} from "./services/course.service";
import {CoursesComponent} from "./components/courses/courses.component";
import {PageNotFoundComponent} from "./components/page-not-found/page-not-found.component";
import {CourseFilterPipe} from "./filters/course-filter.pipe";
import {FormsModule} from "@angular/forms";
import {NavbarComponent} from "./components/navbar/navbar.component";
import {CoursePageComponent} from "./components/course-page/course-page.component";
import {AssignmentService} from "./services/assignment.service";


@NgModule({
  declarations: [
    AppComponent,
    CoursesComponent,
    PageNotFoundComponent,
    CourseFilterPipe,
    NavbarComponent,
    CoursePageComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    NgbModule.forRoot(),
    RouterModule.forRoot(
      ROUTES,
      {
        enableTracing: true, // debug
        useHash: false
      }
    ),
    FormsModule
  ],
  providers: [
    CourseService,
    AssignmentService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
