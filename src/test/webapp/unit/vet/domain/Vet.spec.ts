import { specialtiesLabel } from '@/vet/domain/Vet';
import { describe, expect, it } from 'vitest';
import { james, linda } from './VetsFixture';

describe('Vet', () => {
  it('should list specialties', () => {
    expect(specialtiesLabel(linda())).toBe('surgery, dentistry');
  });

  it('should show none without specialty', () => {
    expect(specialtiesLabel(james())).toBe('none');
  });
});
