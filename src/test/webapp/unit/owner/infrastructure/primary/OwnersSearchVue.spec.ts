import { provide } from '@/injections';
import { OWNERS_REPOSITORY } from '@/owner/application/OwnersKeys';
import OwnersSearchVue from '@/owner/infrastructure/primary/OwnersSearchVue.vue';
import { routes } from '@/router';
import { flushPromises, mount, type VueWrapper } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import { createMemoryHistory, createRouter, type Router } from 'vue-router';
import { betty, george, ownersPage, stubOwnersRepository } from '../../domain/OwnersFixture';

const wrap = (router: Router): VueWrapper => mount(OwnersSearchVue, { global: { plugins: [router] } });

const memoryRouter = (): Router => createRouter({ history: createMemoryHistory(), routes });

describe('Owners search', () => {
  it('should show not found message when no owner matches', async () => {
    const owners = stubOwnersRepository();
    owners.search.mockResolvedValue(ownersPage([]));
    provide(OWNERS_REPOSITORY, owners);
    const wrapper = wrap(memoryRouter());

    await wrapper.find('[data-selector="last-name"]').setValue('Unknown');
    await wrapper.find('form').trigger('submit');
    await flushPromises();

    expect(owners.search).toHaveBeenCalledWith('Unknown', 0);
    expect(wrapper.find('[data-selector="not-found"]').text()).toBe('Owner not found.');
    expect(wrapper.find('[data-selector="owners"]').exists()).toBe(false);
  });

  it('should show owner details directly when a single owner matches', async () => {
    const owners = stubOwnersRepository();
    owners.search.mockResolvedValue(ownersPage([george()]));
    owners.get.mockResolvedValue(george());
    provide(OWNERS_REPOSITORY, owners);
    const router = memoryRouter();
    const wrapper = wrap(router);

    await wrapper.find('form').trigger('submit');
    await flushPromises();

    expect(router.currentRoute.value.name).toBe('OwnerDetails');
    expect(router.currentRoute.value.params).toEqual({ ownerId: 'george-id' });
  });

  it('should list matching owners with their pets and paginate', async () => {
    const owners = stubOwnersRepository();
    owners.search.mockResolvedValueOnce(ownersPage([betty(), george()], 0, 2, 7));
    owners.search.mockResolvedValueOnce(ownersPage([betty(), george()], 1, 2, 7));
    owners.search.mockResolvedValueOnce(ownersPage([betty(), george()], 0, 2, 7));
    provide(OWNERS_REPOSITORY, owners);
    const wrapper = wrap(memoryRouter());

    await wrapper.find('form').trigger('submit');
    await flushPromises();

    const rows = wrapper.findAll('[data-selector="owner"]');
    expect(rows).toHaveLength(2);
    expect(rows[1]!.text()).toContain('George Franklin');
    expect(rows[1]!.text()).toContain('Leo');
    expect(wrapper.find('[data-selector="page"]').text()).toBe('Page 1 / 2');
    expect(wrapper.find('[data-selector="previous"]').attributes('disabled')).toBeDefined();

    await wrapper.find('[data-selector="next"]').trigger('click');
    await flushPromises();

    expect(owners.search).toHaveBeenLastCalledWith('', 1);
    expect(wrapper.find('[data-selector="page"]').text()).toBe('Page 2 / 2');
    expect(wrapper.find('[data-selector="next"]').attributes('disabled')).toBeDefined();

    await wrapper.find('[data-selector="previous"]').trigger('click');
    await flushPromises();

    expect(owners.search).toHaveBeenLastCalledWith('', 0);
    expect(wrapper.find('[data-selector="page"]').text()).toBe('Page 1 / 2');
  });

  it('should show search error', async () => {
    const owners = stubOwnersRepository();
    owners.search.mockRejectedValue(new Error('Network error'));
    provide(OWNERS_REPOSITORY, owners);
    const wrapper = wrap(memoryRouter());

    await wrapper.find('form').trigger('submit');
    await flushPromises();

    expect(wrapper.find('[data-selector="error"]').text()).toBe('Network error');
  });
});
