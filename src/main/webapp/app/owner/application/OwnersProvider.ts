import { provide } from '@/injections';
import { OWNERS_REPOSITORY } from '@/owner/application/OwnersKeys';
import { RestOwnersRepository } from '@/owner/infrastructure/secondary/RestOwnersRepository';
import type { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp';

export const provideForOwners = (axios: AxiosHttp): void => {
  provide(OWNERS_REPOSITORY, new RestOwnersRepository(axios));
};
