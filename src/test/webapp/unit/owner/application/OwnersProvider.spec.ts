import { inject } from '@/injections';
import { OWNERS_REPOSITORY } from '@/owner/application/OwnersKeys';
import { provideForOwners } from '@/owner/application/OwnersProvider';
import { RestOwnersRepository } from '@/owner/infrastructure/secondary/RestOwnersRepository';
import { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp';
import { describe, expect, it } from 'vitest';
import { stubAxiosInstance } from '../../shared/http/infrastructure/secondary/AxiosStub';

describe('Owners provider', () => {
  it('should provide rest owners repository', () => {
    provideForOwners(new AxiosHttp(stubAxiosInstance()));

    expect(inject(OWNERS_REPOSITORY)).toBeInstanceOf(RestOwnersRepository);
  });
});
