import { TestBed, inject } from '@angular/core/testing';

import { UserRouteAccessService } from './user-route-access.service';

describe('UserRouteAccessService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UserRouteAccessService]
    });
  });

  it('should be created', inject([UserRouteAccessService], (service: UserRouteAccessService) => {
    expect(service).toBeTruthy();
  }));
});
