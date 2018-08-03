import {Routes} from "@angular/router";
import {CoursesComponent} from "./components/common/courses/courses.component";
import {CoursePageComponent} from "./components/teacher/course/course-page/course-page.component";
import {PageNotFoundComponent} from "./components/common/page-not-found/page-not-found.component";
import {AddCoursePageComponent} from "./components/teacher/course/add-course-page/add-course-page.component";
import {TeacherTaskPageComponent as TeacherTaskPageComponent} from "./components/teacher/task/task-page/teacher-task-page.component";
import {StudentTaskPageComponent as StudentTaskPageComponent} from "./components/student/task-page/student-task-page.component";
import {AddTaskPageComponent} from "./components/teacher/task/add-task-page/add-task-page.component";
import {EditTaskPageComponent} from "./components/teacher/task/edit-task-page/edit-task-page.component";
import {AddLessonPageComponent} from "./components/teacher/lesson/add-lesson-page/add-lesson-page.component";
import {LessonPageComponent} from "./components/teacher/lesson/lesson-page/lesson-page.component";
import {EditLessonPageComponent} from "./components/teacher/lesson/edit-lesson-page/edit-lesson-page.component";
import {EditCoursePageComponent} from "./components/teacher/course/edit-course-page/edit-course-page.component";
import {StudentLessonPageComponent} from "./components/student/lesson-page/student-lesson-page.component";
import {LandingPageComponent} from "./components/common/landing-page/landing-page.component";
import {StudentDashboardComponent} from "./components/student/dashboard/student-dashboard.component";
import {TeacherDashboardComponent} from "./components/teacher/dashboard/teacher-dashboard.component";
import {LoggedGuardService} from "./auth/logged-guard.service";
import {UserRouteAccessService} from "./auth/user-route-access.service";
import {AccountTypesConstants} from "./constants/account-types.constants";
import {CourseEditionPageComponent} from "./components/teacher/course-edition/course-edition-page/course-edition-page.component";


export const ROUTES: Routes = [
  {
    path: '',
    component: LandingPageComponent,
    canActivate: [LoggedGuardService]
  },
  // TODO make separate modules for this
  { path: 'student',
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        component: StudentDashboardComponent,
        data: {
          authorities: [AccountTypesConstants.ROLE_STUDENT],
        },
        canActivate: [UserRouteAccessService]
      }
    ]
  },
  { path: 'teacher',
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        component: TeacherDashboardComponent,
        data: {
          authorities: [AccountTypesConstants.ROLE_TEACHER],
        },
        canActivate: [UserRouteAccessService]
      },
      {
        path: 'add-course',
        component: AddCoursePageComponent ,
        data: {
          authorities: [AccountTypesConstants.ROLE_TEACHER],
        },
        canActivate: [UserRouteAccessService]
      },
      {
        path: 'edit-course/:courseId',
        component: EditCoursePageComponent,
        data: {
          authorities: [AccountTypesConstants.ROLE_TEACHER],
        },
        canActivate: [UserRouteAccessService]
      }
    ]
  },
  // TODO refactor routes below
  { path: 'courses/:courseId',
    component: CoursePageComponent,
    children: [
      { path: 'add-lesson', component: AddLessonPageComponent },
      { path: 'lessons/:lessonId',
        component: LessonPageComponent,
        children: [
          { path: 'edit-lesson', component: EditLessonPageComponent },
          { path: 'add-task', component: AddTaskPageComponent }
        ]
      },
      { path: 'editions/:editionId',
        component: CourseEditionPageComponent,
        data: {
          authorities: [AccountTypesConstants.ROLE_TEACHER],
        },
        canActivate: [UserRouteAccessService]
      },
      { path: 'student/lessons/:lessonId', component: StudentLessonPageComponent }
    ]
  },
  { path: 'courses', component: CoursesComponent },
  { path: 'teacher/task/:taskId', component: TeacherTaskPageComponent },
  { path: 'student/task/:taskId', component: StudentTaskPageComponent },
  { path: 'edit-task/:taskId', component: EditTaskPageComponent },
  { path: '**', component: PageNotFoundComponent }
];
