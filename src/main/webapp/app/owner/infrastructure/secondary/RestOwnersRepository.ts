import type { Owner, OwnerToSave, Pet, PetToSave, PetType, Visit, VisitToCreate } from '@/owner/domain/Owner';
import type { OwnersPage } from '@/owner/domain/OwnersPage';
import type { OwnersRepository } from '@/owner/domain/OwnersRepository';
import type { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp';
import { toError } from '@/shared/http/infrastructure/secondary/HttpErrors';

interface RestVisit {
  id: string;
  date: string;
  description: string;
}

interface RestPet {
  id: string;
  name: string;
  birthDate: string;
  type: string;
  visits: RestVisit[];
}

interface RestOwner {
  id: string;
  firstName: string;
  lastName: string;
  address: string;
  city: string;
  telephone: string;
  pets: RestPet[];
}

interface RestOwnersPage {
  owners: RestOwner[];
  currentPage: number;
  pageCount: number;
  totalElementsCount: number;
}

const toVisit = (visit: RestVisit): Visit => ({ id: visit.id, date: visit.date, description: visit.description });

const toPet = (pet: RestPet): Pet => ({
  id: pet.id,
  name: pet.name,
  birthDate: pet.birthDate,
  type: pet.type,
  visits: pet.visits.map(toVisit),
});

const toOwner = (owner: RestOwner): Owner => ({
  id: owner.id,
  firstName: owner.firstName,
  lastName: owner.lastName,
  address: owner.address,
  city: owner.city,
  telephone: owner.telephone,
  pets: owner.pets.map(toPet),
});

const toOwnersPage = (page: RestOwnersPage): OwnersPage => ({
  owners: page.owners.map(toOwner),
  currentPage: page.currentPage,
  pageCount: page.pageCount,
  totalElementsCount: page.totalElementsCount,
});

const OWNERS = '/api/owners';

export class RestOwnersRepository implements OwnersRepository {
  constructor(private readonly axios: AxiosHttp) {}

  search(lastName: string, page: number): Promise<OwnersPage> {
    return this.axios
      .get<RestOwnersPage>(OWNERS, { params: { lastName, page } })
      .then(response => toOwnersPage(response.data))
      .catch(error => Promise.reject(toError(error)));
  }

  get(ownerId: string): Promise<Owner> {
    return this.axios
      .get<RestOwner>(`${OWNERS}/${ownerId}`)
      .then(response => toOwner(response.data))
      .catch(error => Promise.reject(toError(error)));
  }

  register(owner: OwnerToSave): Promise<Owner> {
    return this.axios
      .post<RestOwner, OwnerToSave>(OWNERS, owner)
      .then(response => toOwner(response.data))
      .catch(error => Promise.reject(toError(error)));
  }

  update(ownerId: string, owner: OwnerToSave): Promise<Owner> {
    return this.axios
      .put<RestOwner, OwnerToSave>(`${OWNERS}/${ownerId}`, owner)
      .then(response => toOwner(response.data))
      .catch(error => Promise.reject(toError(error)));
  }

  addPet(ownerId: string, pet: PetToSave): Promise<Pet> {
    return this.axios
      .post<RestPet, PetToSave>(`${OWNERS}/${ownerId}/pets`, pet)
      .then(response => toPet(response.data))
      .catch(error => Promise.reject(toError(error)));
  }

  updatePet(ownerId: string, petId: string, pet: PetToSave): Promise<Pet> {
    return this.axios
      .put<RestPet, PetToSave>(`${OWNERS}/${ownerId}/pets/${petId}`, pet)
      .then(response => toPet(response.data))
      .catch(error => Promise.reject(toError(error)));
  }

  scheduleVisit(ownerId: string, petId: string, visit: VisitToCreate): Promise<Visit> {
    return this.axios
      .post<RestVisit, VisitToCreate>(`${OWNERS}/${ownerId}/pets/${petId}/visits`, visit)
      .then(response => toVisit(response.data))
      .catch(error => Promise.reject(toError(error)));
  }

  petTypes(): Promise<PetType[]> {
    return this.axios
      .get<string[]>('/api/pet-types')
      .then(response => response.data)
      .catch(error => Promise.reject(toError(error)));
  }
}
