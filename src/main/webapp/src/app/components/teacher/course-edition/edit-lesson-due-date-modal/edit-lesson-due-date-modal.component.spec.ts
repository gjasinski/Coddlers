import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditLessonDueDateModalComponent } from './edit-lesson-due-date-modal.component';

describe('EditLessonDueDateModalComponent', () => {
  let component: EditLessonDueDateModalComponent;
  let fixture: ComponentFixture<EditLessonDueDateModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditLessonDueDateModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditLessonDueDateModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
