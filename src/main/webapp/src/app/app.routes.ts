import {Routes} from "@angular/router";
import {CoursesComponent} from "./components/courses/courses.component";
import {CoursePageComponent} from "./components/course/course-page/course-page.component";
import {PageNotFoundComponent} from "./components/page-not-found/page-not-found.component";
import {AddCoursePageComponent} from "./components/course/add-course-page/add-course-page.component";
import {TeacherTaskPageComponent as TeacherTaskPageComponent} from "./components/teacher/task-page/teacher-task-page.component";
import {StudentTaskPageComponent as StudentTaskPageComponent} from "./components/student/task-page/student-task-page.component";
import {AddTaskPageComponent} from "./components/add-task-page/add-task-page.component";
import {EditTaskPageComponent} from "./components/edit-task-page/edit-task-page.component";
import {AddAssignmentPageComponent} from "./components/assignment/add-assignment-page/add-assignment-page.component";
import {AssignmentPageComponent} from "./components/assignment/assignment-page/assignment-page.component";
import {EditAssignmentPageComponent} from "./components/assignment/edit-assignment-page/edit-assignment-page.component";
import {EditCoursePageComponent} from "./components/course/edit-course-page/edit-course-page.component";
import {StudentAssignmentPageComponent} from "./components/student/assignment-page/student-assignment-page.component";


export const ROUTES: Routes = [
  { path: '',
    redirectTo: 'courses',
    pathMatch: 'full'
  },
  { path: 'add-course', component: AddCoursePageComponent },
  { path: 'courses/:courseId',
    component: CoursePageComponent,
    children: [
      { path: 'add-assignment', component: AddAssignmentPageComponent },
      { path: 'assignments/:assignmentId',
        component: AssignmentPageComponent,
        children: [
          { path: 'edit-assignment', component: EditAssignmentPageComponent },
          { path: 'add-task', component: AddTaskPageComponent }
        ]
      },
      { path: 'student/assignments/:assignmentId', component: StudentAssignmentPageComponent }
    ]
  },
  { path: 'courses', component: CoursesComponent },
  { path: 'teacher/task/:taskId', component: TeacherTaskPageComponent },
  { path: 'student/task/:taskId', component: StudentTaskPageComponent },
  { path: 'edit-task/:taskId', component: EditTaskPageComponent },
  { path: 'edit-course/:courseId', component: EditCoursePageComponent },
  { path: '**', component: PageNotFoundComponent }
];
