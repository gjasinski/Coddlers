import { TestBed, inject } from '@angular/core/testing';

import { LoggedGuardService } from './logged-guard.service';

describe('LoggedGuardService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LoggedGuardService]
    });
  });

  it('should be created', inject([LoggedGuardService], (service: LoggedGuardService) => {
    expect(service).toBeTruthy();
  }));
});
