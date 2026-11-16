import { provide } from '@/injections';
import type { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp';
import { VETS_REPOSITORY } from '@/vet/application/VetsKeys';
import { RestVetsRepository } from '@/vet/infrastructure/secondary/RestVetsRepository';

export const provideForVets = (axios: AxiosHttp): void => {
  provide(VETS_REPOSITORY, new RestVetsRepository(axios));
};
