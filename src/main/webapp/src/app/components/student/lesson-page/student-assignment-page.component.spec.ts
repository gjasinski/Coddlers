import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentLessonPageComponent } from './student-lesson-page.component';

describe('StudentLessonPageComponent', () => {
  let component: StudentLessonPageComponent;
  let fixture: ComponentFixture<StudentLessonPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StudentLessonPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudentLessonPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
