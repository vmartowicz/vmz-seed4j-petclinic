export type PetType = string;

export interface Visit {
  id: string;
  date: string;
  description: string;
}

export interface Pet {
  id: string;
  name: string;
  birthDate: string;
  type: PetType;
  visits: Visit[];
}

export interface Owner {
  id: string;
  firstName: string;
  lastName: string;
  address: string;
  city: string;
  telephone: string;
  pets: Pet[];
}

export interface OwnerToSave {
  firstName: string;
  lastName: string;
  address: string;
  city: string;
  telephone: string;
}

export interface PetToSave {
  name: string;
  birthDate: string;
  type: PetType;
}

export interface VisitToCreate {
  date: string;
  description: string;
}

export const emptyOwner = (): OwnerToSave => ({ firstName: '', lastName: '', address: '', city: '', telephone: '' });

export const ownerToSave = (owner: Owner): OwnerToSave => ({
  firstName: owner.firstName,
  lastName: owner.lastName,
  address: owner.address,
  city: owner.city,
  telephone: owner.telephone,
});

export const fullName = (owner: Owner): string => `${owner.firstName} ${owner.lastName}`;

export const findPet = (owner: Owner, petId: string): Pet | undefined => owner.pets.find(pet => pet.id === petId);
