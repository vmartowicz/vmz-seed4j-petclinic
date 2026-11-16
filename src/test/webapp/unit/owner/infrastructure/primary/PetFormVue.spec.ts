import { provide } from '@/injections';
import { OWNERS_REPOSITORY } from '@/owner/application/OwnersKeys';
import PetFormVue from '@/owner/infrastructure/primary/PetFormVue.vue';
import { routes } from '@/router';
import { flushPromises, mount, type VueWrapper } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import { createMemoryHistory, createRouter, type Router } from 'vue-router';
import { george, leo, stubOwnersRepository } from '../../domain/OwnersFixture';

const memoryRouter = (): Router => createRouter({ history: createMemoryHistory(), routes });

const wrap = (router: Router, props: { ownerId: string; petId?: string }): VueWrapper =>
  mount(PetFormVue, { props, global: { plugins: [router] } });

const loadedOwners = () => {
  const owners = stubOwnersRepository();
  owners.get.mockResolvedValue(george());
  owners.petTypes.mockResolvedValue(['cat', 'dog', 'lizard', 'snake', 'bird', 'hamster']);
  provide(OWNERS_REPOSITORY, owners);

  return owners;
};

describe('Pet form', () => {
  it('should add pet with a type from the pet types', async () => {
    const owners = loadedOwners();
    owners.addPet.mockResolvedValue(leo());
    const router = memoryRouter();
    const wrapper = wrap(router, { ownerId: 'george-id' });
    await flushPromises();

    expect(wrapper.find('[data-selector="owner-name"]').text()).toBe('Owner: George Franklin');
    expect(wrapper.findAll('[data-selector="type"] option')).toHaveLength(7);

    await wrapper.find('[data-selector="name"]').setValue('Basil');
    await wrapper.find('[data-selector="birth-date"]').setValue('2025-02-01');
    await wrapper.find('[data-selector="type"]').setValue('hamster');
    await wrapper.find('form').trigger('submit');
    await flushPromises();

    expect(owners.addPet).toHaveBeenCalledWith('george-id', { name: 'Basil', birthDate: '2025-02-01', type: 'hamster' });
    expect(router.currentRoute.value.name).toBe('OwnerDetails');
  });

  it('should prefill and update pet', async () => {
    const owners = loadedOwners();
    owners.updatePet.mockResolvedValue(leo());
    const wrapper = wrap(memoryRouter(), { ownerId: 'george-id', petId: 'leo-id' });
    await flushPromises();

    expect((wrapper.find('[data-selector="name"]').element as HTMLInputElement).value).toBe('Leo');

    await wrapper.find('form').trigger('submit');
    await flushPromises();

    expect(owners.updatePet).toHaveBeenCalledWith('george-id', 'leo-id', { name: 'Leo', birthDate: '2020-09-07', type: 'cat' });
  });

  it('should not prefill unknown pet', async () => {
    loadedOwners();
    const wrapper = wrap(memoryRouter(), { ownerId: 'george-id', petId: 'unknown' });
    await flushPromises();

    expect((wrapper.find('[data-selector="name"]').element as HTMLInputElement).value).toBe('');
  });

  it('should show duplicated name error', async () => {
    const owners = loadedOwners();
    owners.addPet.mockRejectedValue(new Error('The owner already has a pet named "leo"'));
    const wrapper = wrap(memoryRouter(), { ownerId: 'george-id' });
    await flushPromises();

    await wrapper.find('form').trigger('submit');
    await flushPromises();

    expect(wrapper.find('[data-selector="error"]').text()).toBe('The owner already has a pet named "leo"');
  });

  it('should show loading error', async () => {
    const owners = stubOwnersRepository();
    owners.get.mockRejectedValue(new Error('Owner george-id is unknown'));
    owners.petTypes.mockResolvedValue([]);
    provide(OWNERS_REPOSITORY, owners);

    const wrapper = wrap(memoryRouter(), { ownerId: 'george-id' });
    await flushPromises();

    expect(wrapper.find('[data-selector="error"]').text()).toBe('Owner george-id is unknown');
  });
});
