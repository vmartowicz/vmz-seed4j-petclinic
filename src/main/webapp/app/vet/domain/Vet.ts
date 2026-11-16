export interface Vet {
  id: string;
  firstName: string;
  lastName: string;
  specialties: string[];
}

export interface VetsPage {
  vets: Vet[];
  currentPage: number;
  pageCount: number;
  totalElementsCount: number;
}

export const specialtiesLabel = (vet: Vet): string => (vet.specialties.length === 0 ? 'none' : vet.specialties.join(', '));
