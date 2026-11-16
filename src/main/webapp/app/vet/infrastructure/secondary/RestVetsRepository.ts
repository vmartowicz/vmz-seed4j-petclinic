import type { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp';
import { toError } from '@/shared/http/infrastructure/secondary/HttpErrors';
import type { Vet, VetsPage } from '@/vet/domain/Vet';
import type { VetsRepository } from '@/vet/domain/VetsRepository';

interface RestVet {
  id: string;
  firstName: string;
  lastName: string;
  specialties: string[];
}

interface RestVetsPage {
  vets: RestVet[];
  currentPage: number;
  pageCount: number;
  totalElementsCount: number;
}

const toVet = (vet: RestVet): Vet => ({ id: vet.id, firstName: vet.firstName, lastName: vet.lastName, specialties: vet.specialties });

export class RestVetsRepository implements VetsRepository {
  constructor(private readonly axios: AxiosHttp) {}

  list(page: number): Promise<VetsPage> {
    return this.axios
      .get<RestVetsPage>('/api/vets', { params: { page } })
      .then(response => ({
        vets: response.data.vets.map(toVet),
        currentPage: response.data.currentPage,
        pageCount: response.data.pageCount,
        totalElementsCount: response.data.totalElementsCount,
      }))
      .catch(error => Promise.reject(toError(error)));
  }
}
