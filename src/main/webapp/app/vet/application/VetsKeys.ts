import type { VetsRepository } from '@/vet/domain/VetsRepository';
import { key } from 'piqure';

export const VETS_REPOSITORY = key<VetsRepository>('VetsRepository');
