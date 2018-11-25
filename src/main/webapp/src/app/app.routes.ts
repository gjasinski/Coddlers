import {Routes} from "@angular/router";
import {TeacherCoursesComponent} from "./components/teacher/course/courses/teacher-courses.component";
import {TeacherCoursePageComponent} from "./components/teacher/course/course-page/teacher-course-page.component";
import {PageNotFoundComponent} from "./components/common/page-not-found/page-not-found.component";
import {AddCoursePageComponent} from "./components/teacher/course/add-course-page/add-course-page.component";
import {AddLessonPageComponent} from "./components/teacher/lesson/add-lesson-page/add-lesson-page.component";
import {TeacherLessonPageComponent} from "./components/teacher/lesson/lesson-page/teacher-lesson-page.component";
import {EditLessonPageComponent} from "./components/teacher/lesson/edit-lesson-page/edit-lesson-page.component";
import {EditCoursePageComponent} from "./components/teacher/course/edit-course-page/edit-course-page.component";
import {LandingPageComponent} from "./components/common/landing-page/landing-page.component";
import {StudentDashboardComponent} from "./components/student/dashboard/student-dashboard.component";
import {TeacherDashboardComponent} from "./components/teacher/dashboard/teacher-dashboard.component";
import {LoggedGuardService} from "./auth/logged-guard.service";
import {UserRouteAccessService} from "./auth/user-route-access.service";
import {AccountTypesConstants} from "./constants/account-types.constants";
import {CourseEditionPageComponent} from "./components/teacher/course-edition/course-edition-page/course-edition-page.component";
import {InvitePageComponent} from "./components/common/invite-page/invite-page.component";
import {StudentCoursePageComponent} from "./components/student/course/course-page/student-course-page.component";
import {StudentCourseEditionPageComponent} from "./components/student/course-edition/course-edition-page/student-course-edition-page.component";
import {StudentLessonPageComponent} from "./components/student/lesson/lesson-page/student-lesson-page.component";
import {StudentMyCoursesComponent} from "./components/student/student-my-courses/student-my-courses.component";
import {SubmissionReviewPageComponent} from "./components/teacher/course-edition/submission-review-page/submission-review-page.component";


export const ROUTES: Routes = [
  {
    path: '',
    component: LandingPageComponent,
    canActivate: [LoggedGuardService]
  },
  // TODO make separate modules for this
  {
    path: 'student',
    data: {
      authorities: [AccountTypesConstants.ROLE_STUDENT],
    },
    canActivate: [UserRouteAccessService],
    children: [
      {path: '', redirectTo: 'dashboard', pathMatch: 'full'},
      {
        path: 'dashboard',
        component: StudentDashboardComponent
      },
      {
        path: 'course-editions',
        component: StudentCoursePageComponent,
        children: [
          {
            path: ':courseEditionId',
            component: StudentCourseEditionPageComponent,
            children: [
              {
                path: 'lessons/:lessonId',
                component: StudentLessonPageComponent
              }
            ]
          }
        ]
      }
    ]
  },
  {
    path: 'teacher',
    data: {
      authorities: [AccountTypesConstants.ROLE_TEACHER],
    },
    canActivate: [UserRouteAccessService],
    children: [
      {path: '', redirectTo: 'dashboard', pathMatch: 'full'},
      {
        path: 'dashboard',
        component: TeacherDashboardComponent
      },
      {
        path: 'courses',
        component: TeacherCoursesComponent,
        children: [
          {
            path: ':courseId',
            component: TeacherCoursePageComponent,
            children: [
              {path: 'add-lesson', component: AddLessonPageComponent},
              {
                path: 'lessons/:lessonId',
                component: TeacherLessonPageComponent,
                children: [
                  {path: 'edit-lesson', component: EditLessonPageComponent}
                ]
              },
              {
                path: 'editions/:editionId',
                component: CourseEditionPageComponent,
                children: [
                  {path: 'submission/:submissionId', component: SubmissionReviewPageComponent}
                ]
              }
            ]
          }
        ]
      },
      {
        path: 'add-course',
        component: AddCoursePageComponent
      },
      {
        path: 'edit-course/:courseId',
        component: EditCoursePageComponent,
      }
    ]
  },
  {
    path: 'invitations',
    component: InvitePageComponent
  },
  {path: '**', component: PageNotFoundComponent}
];
