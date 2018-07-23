import {Routes} from "@angular/router";
import {CoursesComponent} from "./components/courses/courses.component";
import {CoursePageComponent} from "./components/teacher/course/course-page/course-page.component";
import {PageNotFoundComponent} from "./components/page-not-found/page-not-found.component";
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
import {EditionPageComponent} from "./components/teacher/edition/edition-page/edition-page.component";


export const ROUTES: Routes = [
  {
    path: '',
    redirectTo: 'courses',
    pathMatch: 'full'
  },
  {path: 'add-course', component: AddCoursePageComponent},
  {
    path: 'courses/:courseId',
    component: CoursePageComponent,
    children: [
      {path: 'add-lesson', component: AddLessonPageComponent},
      {
        path: 'lessons/:lessonId',
        component: LessonPageComponent,
        children: [
          {path: 'edit-lesson', component: EditLessonPageComponent},
          {path: 'add-task', component: AddTaskPageComponent}
        ]
      },
      {
        path: 'editions/:editionId',
        component: EditionPageComponent
      },
      {path: 'student/lessons/:lessonId', component: StudentLessonPageComponent}
    ]
  },
  {path: 'courses', component: CoursesComponent},
  {path: 'teacher/task/:taskId', component: TeacherTaskPageComponent},
  {path: 'student/task/:taskId', component: StudentTaskPageComponent},
  {path: 'edit-task/:taskId', component: EditTaskPageComponent},
  {path: 'edit-course/:courseId', component: EditCoursePageComponent},
  {path: '**', component: PageNotFoundComponent}
];
