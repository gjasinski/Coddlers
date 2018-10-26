import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InviteStudentsModalComponent } from './invite-students-modal.component';

describe('InviteStudentsModalComponent', () => {
  let component: InviteStudentsModalComponent;
  let fixture: ComponentFixture<InviteStudentsModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InviteStudentsModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InviteStudentsModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
