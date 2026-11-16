import type { Owner, OwnerToSave, Pet, PetToSave, PetType, Visit, VisitToCreate } from '@/owner/domain/Owner';
import type { OwnersPage } from '@/owner/domain/OwnersPage';

export interface OwnersRepository {
  search(lastName: string, page: number): Promise<OwnersPage>;
  get(ownerId: string): Promise<Owner>;
  register(owner: OwnerToSave): Promise<Owner>;
  update(ownerId: string, owner: OwnerToSave): Promise<Owner>;
  addPet(ownerId: string, pet: PetToSave): Promise<Pet>;
  updatePet(ownerId: string, petId: string, pet: PetToSave): Promise<Pet>;
  scheduleVisit(ownerId: string, petId: string, visit: VisitToCreate): Promise<Visit>;
  petTypes(): Promise<PetType[]>;
}
