import { routes } from '@/router';
import { describe, expect, it } from 'vitest';
import { createMemoryHistory, createRouter } from 'vue-router';

describe('Clinic routes', () => {
  it.each([
    ['/owners', 'OwnersSearch'],
    ['/owners/new', 'OwnerCreation'],
    ['/owners/george-id', 'OwnerDetails'],
    ['/owners/george-id/edit', 'OwnerEdition'],
    ['/owners/george-id/pets/new', 'PetCreation'],
    ['/owners/george-id/pets/leo-id/edit', 'PetEdition'],
    ['/owners/george-id/pets/leo-id/visits/new', 'VisitCreation'],
    ['/vets', 'Vets'],
  ])('should route %s to %s', async (url, name) => {
    const router = createRouter({ history: createMemoryHistory(), routes });

    await router.push(url);

    expect(router.currentRoute.value.name).toBe(name);
  });
});
