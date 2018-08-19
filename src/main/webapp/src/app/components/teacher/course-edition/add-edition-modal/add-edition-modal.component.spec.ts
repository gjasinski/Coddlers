import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddEditionModalComponent } from './add-edition-modal.component';

describe('AddEditionModalComponent', () => {
  let component: AddEditionModalComponent;
  let fixture: ComponentFixture<AddEditionModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddEditionModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddEditionModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
