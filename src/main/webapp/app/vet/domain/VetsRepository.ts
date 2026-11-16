import type { VetsPage } from '@/vet/domain/Vet';

export interface VetsRepository {
  list(page: number): Promise<VetsPage>;
}
