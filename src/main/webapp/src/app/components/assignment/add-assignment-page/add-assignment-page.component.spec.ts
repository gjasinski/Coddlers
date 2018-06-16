import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddAssignmentPageComponent } from './add-assignment-page.component';

describe('AddAssignmentPageComponent', () => {
  let component: AddAssignmentPageComponent;
  let fixture: ComponentFixture<AddAssignmentPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddAssignmentPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddAssignmentPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
