import { provide } from '@/injections';
import { OWNERS_REPOSITORY } from '@/owner/application/OwnersKeys';
import VisitFormVue from '@/owner/infrastructure/primary/VisitFormVue.vue';
import { routes } from '@/router';
import { flushPromises, mount, type VueWrapper } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import { createMemoryHistory, createRouter, type Router } from 'vue-router';
import { george, stubOwnersRepository } from '../../domain/OwnersFixture';

const memoryRouter = (): Router => createRouter({ history: createMemoryHistory(), routes });

const wrap = (router: Router): VueWrapper =>
  mount(VisitFormVue, { props: { ownerId: 'george-id', petId: 'leo-id' }, global: { plugins: [router] } });

describe('Visit form', () => {
  it('should schedule visit for the pet', async () => {
    const owners = stubOwnersRepository();
    owners.get.mockResolvedValue(george());
    owners.scheduleVisit.mockResolvedValue({ id: 'visit', date: '2030-01-01', description: 'Check-up' });
    provide(OWNERS_REPOSITORY, owners);
    const router = memoryRouter();
    const wrapper = wrap(router);
    await flushPromises();

    expect(wrapper.find('[data-selector="pet"]').text()).toContain('Leo');
    expect(wrapper.text()).toContain('Rabies shot');

    await wrapper.find('[data-selector="date"]').setValue('2030-01-01');
    await wrapper.find('[data-selector="description"]').setValue('Check-up');
    await wrapper.find('form').trigger('submit');
    await flushPromises();

    expect(owners.scheduleVisit).toHaveBeenCalledWith('george-id', 'leo-id', { date: '2030-01-01', description: 'Check-up' });
    expect(router.currentRoute.value.name).toBe('OwnerDetails');
  });

  it('should show visit date error', async () => {
    const owners = stubOwnersRepository();
    owners.get.mockResolvedValue(george());
    owners.scheduleVisit.mockRejectedValue(new Error('Visit date 2026-11-15 must be tomorrow at the earliest'));
    provide(OWNERS_REPOSITORY, owners);
    const wrapper = wrap(memoryRouter());
    await flushPromises();

    await wrapper.find('form').trigger('submit');
    await flushPromises();

    expect(wrapper.find('[data-selector="error"]').text()).toBe('Visit date 2026-11-15 must be tomorrow at the earliest');
  });

  it('should show loading error', async () => {
    const owners = stubOwnersRepository();
    owners.get.mockRejectedValue(new Error('Owner george-id is unknown'));
    provide(OWNERS_REPOSITORY, owners);

    const wrapper = wrap(memoryRouter());
    await flushPromises();

    expect(wrapper.find('[data-selector="error"]').text()).toBe('Owner george-id is unknown');
  });
});
