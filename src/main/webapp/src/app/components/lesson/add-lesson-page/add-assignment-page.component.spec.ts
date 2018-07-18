import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddLessonPageComponent } from './add-lesson-page.component';

describe('AddLessonPageComponent', () => {
  let component: AddLessonPageComponent;
  let fixture: ComponentFixture<AddLessonPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddLessonPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddLessonPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
