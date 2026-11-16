import { emptyOwner, findPet, fullName, ownerToSave } from '@/owner/domain/Owner';
import { describe, expect, it } from 'vitest';
import { george, leo } from './OwnersFixture';

describe('Owner', () => {
  it('should build an empty owner to save', () => {
    expect(emptyOwner()).toEqual({ firstName: '', lastName: '', address: '', city: '', telephone: '' });
  });

  it('should get the owner to save from an owner', () => {
    expect(ownerToSave(george())).toEqual({
      firstName: 'George',
      lastName: 'Franklin',
      address: '110 W. Liberty St.',
      city: 'Madison',
      telephone: '6085551023',
    });
  });

  it('should get full name', () => {
    expect(fullName(george())).toBe('George Franklin');
  });

  it('should find a pet of the owner', () => {
    expect(findPet(george(), 'leo-id')).toEqual(leo());
  });

  it('should not find an unknown pet', () => {
    expect(findPet(george(), 'unknown')).toBeUndefined();
  });
});
