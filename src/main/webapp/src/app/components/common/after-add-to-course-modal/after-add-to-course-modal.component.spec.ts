import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AfterAddToCourseModalComponent } from './after-add-to-course-modal.component';

describe('AfterAddToCourseModalComponent', () => {
  let component: AfterAddToCourseModalComponent;
  let fixture: ComponentFixture<AfterAddToCourseModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AfterAddToCourseModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AfterAddToCourseModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
