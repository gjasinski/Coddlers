import {Routes} from "@angular/router";
import {CoursesComponent} from "./components/courses/courses.component";
import {CoursePageComponent} from "./components/course/course-page/course-page.component";
import {PageNotFoundComponent} from "./components/page-not-found/page-not-found.component";
import {AddCoursePageComponent} from "./components/course/add-course-page/add-course-page.component";
import {TaskPageComponent as TeacherTaskPageComponent} from "./components/teacher-components/task-page/task-page.component";
import {TaskPageComponent as StudentTaskPageComponent} from "./components/student-components/task-page/task-page.component";
import {AddTaskPageComponent} from "./components/add-task-page/add-task-page.component";
import {EditTaskPageComponent} from "./components/edit-task-page/edit-task-page.component";
import {AddAssignmentPageComponent} from "./components/assignment/add-assignment-page/add-assignment-page.component";
import {AssignmentPageComponent} from "./components/assignment/assignment-page/assignment-page.component";
import {EditAssignmentPageComponent} from "./components/assignment/edit-assignment-page/edit-assignment-page.component";

export const ROUTES: Routes = [
  { path: '',
    redirectTo: 'courses',
    pathMatch: 'full'
  },
  { path: 'add-course', component: AddCoursePageComponent },
  // TODO this is probably correct path for task
  // { path: 'courses/:courseId/:taskId', component: TaskPageComponent },
  { path: 'courses/:courseId',
    component: CoursePageComponent,
    children: [
      { path: 'add-assignment', component: AddAssignmentPageComponent },
      { path: 'assignments/:assignmentId',
        component: AssignmentPageComponent,
        children: [
          { path: 'edit-assignment', component: EditAssignmentPageComponent }
        ]
      }
    ]
  },
  { path: 'courses', component: CoursesComponent },
  { path: 'teacher/task/:taskId', component: TeacherTaskPageComponent },
  { path: 'student/task/:taskId', component: StudentTaskPageComponent },
  { path: 'add-task', component: AddTaskPageComponent },
  { path: 'edit-task/:taskId', component: EditTaskPageComponent },
  { path: '**', component: PageNotFoundComponent }
];
