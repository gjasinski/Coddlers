import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import {StudentCourseEditionPageComponent} from "./student-course-edition-page.component";


describe('StudentCourseEditionPageComponent', () => {
  let component: StudentCourseEditionPageComponent;
  let fixture: ComponentFixture<StudentCourseEditionPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StudentCourseEditionPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudentCourseEditionPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
