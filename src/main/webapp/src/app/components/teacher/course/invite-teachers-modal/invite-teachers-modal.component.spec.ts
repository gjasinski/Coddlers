import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InviteTeachersModalComponent } from './invite-teachers-modal.component';

describe('InviteTeachersModalComponent', () => {
  let component: InviteTeachersModalComponent;
  let fixture: ComponentFixture<InviteTeachersModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InviteTeachersModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InviteTeachersModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
