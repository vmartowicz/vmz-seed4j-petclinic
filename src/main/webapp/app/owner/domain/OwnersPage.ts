import type { Owner } from '@/owner/domain/Owner';

export interface OwnersPage {
  owners: Owner[];
  currentPage: number;
  pageCount: number;
  totalElementsCount: number;
}

export type OwnersSearchOutcome = { kind: 'not-found' } | { kind: 'single'; owner: Owner } | { kind: 'list'; page: OwnersPage };

export const searchOutcome = (page: OwnersPage): OwnersSearchOutcome => {
  if (page.totalElementsCount === 0) {
    return { kind: 'not-found' };
  }

  if (page.totalElementsCount === 1) {
    return { kind: 'single', owner: page.owners[0]! };
  }

  return { kind: 'list', page };
};
