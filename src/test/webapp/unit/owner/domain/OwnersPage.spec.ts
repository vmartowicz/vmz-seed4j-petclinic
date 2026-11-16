import { searchOutcome } from '@/owner/domain/OwnersPage';
import { describe, expect, it } from 'vitest';
import { betty, george, ownersPage } from './OwnersFixture';

describe('Owners search outcome', () => {
  it('should be not found without owner', () => {
    expect(searchOutcome(ownersPage([]))).toEqual({ kind: 'not-found' });
  });

  it('should show the owner directly when a single owner matches', () => {
    expect(searchOutcome(ownersPage([george()]))).toEqual({ kind: 'single', owner: george() });
  });

  it('should list owners when several owners match', () => {
    const page = ownersPage([betty(), george()]);

    expect(searchOutcome(page)).toEqual({ kind: 'list', page });
  });

  it('should list owners on a second page holding a single owner', () => {
    const page = ownersPage([george()], 1, 2, 6);

    expect(searchOutcome(page)).toEqual({ kind: 'list', page });
  });
});
