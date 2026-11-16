import type { Owner, Pet } from '@/owner/domain/Owner';
import type { OwnersPage } from '@/owner/domain/OwnersPage';
import type { OwnersRepository } from '@/owner/domain/OwnersRepository';
import type { Mocked } from 'vitest';
import { vi } from 'vitest';

export const leo = (): Pet => ({
  id: 'leo-id',
  name: 'Leo',
  birthDate: '2020-09-07',
  type: 'cat',
  visits: [{ id: 'visit-id', date: '2026-01-10', description: 'Rabies shot' }],
});

export const george = (): Owner => ({
  id: 'george-id',
  firstName: 'George',
  lastName: 'Franklin',
  address: '110 W. Liberty St.',
  city: 'Madison',
  telephone: '6085551023',
  pets: [leo()],
});

export const betty = (): Owner => ({
  id: 'betty-id',
  firstName: 'Betty',
  lastName: 'Davis',
  address: '638 Cardinal Ave.',
  city: 'Sun Prairie',
  telephone: '6085551749',
  pets: [],
});

export const ownersPage = (owners: Owner[], currentPage = 0, pageCount = 1, totalElementsCount = owners.length): OwnersPage => ({
  owners,
  currentPage,
  pageCount,
  totalElementsCount,
});

export const stubOwnersRepository = (): Mocked<OwnersRepository> => ({
  search: vi.fn(),
  get: vi.fn(),
  register: vi.fn(),
  update: vi.fn(),
  addPet: vi.fn(),
  updatePet: vi.fn(),
  scheduleVisit: vi.fn(),
  petTypes: vi.fn(),
});
