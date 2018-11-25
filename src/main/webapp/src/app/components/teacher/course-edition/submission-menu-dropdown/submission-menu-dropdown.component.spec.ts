import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmissionMenuDropdownComponent } from './submission-menu-dropdown.component';

describe('SubmissionMenuDropdownComponent', () => {
  let component: SubmissionMenuDropdownComponent;
  let fixture: ComponentFixture<SubmissionMenuDropdownComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubmissionMenuDropdownComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubmissionMenuDropdownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
