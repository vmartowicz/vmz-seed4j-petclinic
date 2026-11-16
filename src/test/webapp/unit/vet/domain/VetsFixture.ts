import type { Vet, VetsPage } from '@/vet/domain/Vet';
import type { VetsRepository } from '@/vet/domain/VetsRepository';
import type { Mocked } from 'vitest';
import { vi } from 'vitest';

export const linda = (): Vet => ({ id: 'linda-id', firstName: 'Linda', lastName: 'Douglas', specialties: ['surgery', 'dentistry'] });

export const james = (): Vet => ({ id: 'james-id', firstName: 'James', lastName: 'Carter', specialties: [] });

export const vetsPage = (vets: Vet[], currentPage = 0, pageCount = 1): VetsPage => ({
  vets,
  currentPage,
  pageCount,
  totalElementsCount: vets.length,
});

export const stubVetsRepository = (): Mocked<VetsRepository> => ({ list: vi.fn() });
