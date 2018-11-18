import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmissionMenuModalComponent } from './submission-menu-modal.component';

describe('SubmissionMenuModalComponent', () => {
  let component: SubmissionMenuModalComponent;
  let fixture: ComponentFixture<SubmissionMenuModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubmissionMenuModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubmissionMenuModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
