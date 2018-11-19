import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentDetailedResultsPageComponent } from './student-detailed-results-page.component';

describe('StudentDetailedResultsPageComponent', () => {
  let component: StudentDetailedResultsPageComponent;
  let fixture: ComponentFixture<StudentDetailedResultsPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StudentDetailedResultsPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudentDetailedResultsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
