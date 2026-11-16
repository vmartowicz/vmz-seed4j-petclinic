import { provide } from '@/injections';
import { OWNERS_REPOSITORY } from '@/owner/application/OwnersKeys';
import OwnerDetailsVue from '@/owner/infrastructure/primary/OwnerDetailsVue.vue';
import { routes } from '@/router';
import { flushPromises, mount, type VueWrapper } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import { createMemoryHistory, createRouter } from 'vue-router';
import { betty, george, stubOwnersRepository } from '../../domain/OwnersFixture';

const wrap = (): VueWrapper =>
  mount(OwnerDetailsVue, {
    props: { ownerId: 'george-id' },
    global: { plugins: [createRouter({ history: createMemoryHistory(), routes })] },
  });

describe('Owner details', () => {
  it('should show owner with pets and visits', async () => {
    const owners = stubOwnersRepository();
    owners.get.mockResolvedValue(george());
    provide(OWNERS_REPOSITORY, owners);

    const wrapper = wrap();
    await flushPromises();

    expect(owners.get).toHaveBeenCalledWith('george-id');
    expect(wrapper.find('[data-selector="owner-name"]').text()).toBe('George Franklin');
    expect(wrapper.find('[data-selector="pet-name"]').text()).toBe('Leo');
    expect(wrapper.find('[data-selector="visit"]').text()).toContain('Rabies shot');
  });

  it('should show owner without pets', async () => {
    const owners = stubOwnersRepository();
    owners.get.mockResolvedValue(betty());
    provide(OWNERS_REPOSITORY, owners);

    const wrapper = wrap();
    await flushPromises();

    expect(wrapper.text()).toContain('No pet yet.');
  });

  it('should show unknown owner error', async () => {
    const owners = stubOwnersRepository();
    owners.get.mockRejectedValue(new Error('Owner george-id is unknown'));
    provide(OWNERS_REPOSITORY, owners);

    const wrapper = wrap();
    await flushPromises();

    expect(wrapper.find('[data-selector="error"]').text()).toBe('Owner george-id is unknown');
  });
});
