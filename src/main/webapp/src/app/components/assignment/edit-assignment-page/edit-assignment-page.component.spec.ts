import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditAssignmentPageComponent } from './edit-assignment-page.component';

describe('EditAssignmentPageComponent', () => {
  let component: EditAssignmentPageComponent;
  let fixture: ComponentFixture<EditAssignmentPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditAssignmentPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditAssignmentPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
