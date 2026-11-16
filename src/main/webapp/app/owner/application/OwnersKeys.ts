import type { OwnersRepository } from '@/owner/domain/OwnersRepository';
import { key } from 'piqure';

export const OWNERS_REPOSITORY = key<OwnersRepository>('OwnersRepository');
