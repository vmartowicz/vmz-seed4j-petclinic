import { inject } from '@/injections';
import { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp';
import { VETS_REPOSITORY } from '@/vet/application/VetsKeys';
import { provideForVets } from '@/vet/application/VetsProvider';
import { RestVetsRepository } from '@/vet/infrastructure/secondary/RestVetsRepository';
import { describe, expect, it } from 'vitest';
import { stubAxiosInstance } from '../../shared/http/infrastructure/secondary/AxiosStub';

describe('Vets provider', () => {
  it('should provide rest vets repository', () => {
    provideForVets(new AxiosHttp(stubAxiosInstance()));

    expect(inject(VETS_REPOSITORY)).toBeInstanceOf(RestVetsRepository);
  });
});
