import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditLessonPageComponent } from './edit-lesson-page.component';

describe('EditLessonPageComponent', () => {
  let component: EditLessonPageComponent;
  let fixture: ComponentFixture<EditLessonPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditLessonPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditLessonPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
