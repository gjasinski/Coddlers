import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentAssignmentPageComponent } from './student-assignment-page.component';

describe('StudentAssignmentPageComponent', () => {
  let component: StudentAssignmentPageComponent;
  let fixture: ComponentFixture<StudentAssignmentPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StudentAssignmentPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudentAssignmentPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
