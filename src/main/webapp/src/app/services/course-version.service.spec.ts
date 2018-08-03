import { TestBed, inject } from '@angular/core/testing';

import { CourseVersionService } from './course-version.service';

describe('CourseVersionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CourseVersionService]
    });
  });

  it('should be created', inject([CourseVersionService], (service: CourseVersionService) => {
    expect(service).toBeTruthy();
  }));
});
