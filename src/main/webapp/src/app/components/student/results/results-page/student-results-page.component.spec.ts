import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentResultsPageComponent } from './student-results-page.component';

describe('StudentResultsPageComponent', () => {
  let component: StudentResultsPageComponent;
  let fixture: ComponentFixture<StudentResultsPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StudentResultsPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudentResultsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
