import {Routes} from "@angular/router";
import {CoursesComponent} from "./components/courses/courses.component";
import {CoursePageComponent} from "./components/course-page/course-page.component";
import {PageNotFoundComponent} from "./components/page-not-found/page-not-found.component";
import {AddCoursePageComponent} from "./components/add-course-page/add-course-page.component";

export const ROUTES: Routes = [
  { path: '',
    redirectTo: 'courses',
    pathMatch: 'full'
  },
  { path: 'add-course', component: AddCoursePageComponent },
  { path: 'courses/:courseId', component: CoursePageComponent },
  { path: 'courses', component: CoursesComponent },
  { path: '**', component: PageNotFoundComponent }
];
