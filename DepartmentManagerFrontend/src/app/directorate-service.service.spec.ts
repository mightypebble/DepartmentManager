import { TestBed } from '@angular/core/testing';

import { DirectorateServiceService } from './directorate-service.service';

describe('DirectorateServiceService', () => {
  let service: DirectorateServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DirectorateServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
