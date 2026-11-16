import { provide } from '@/injections';
import { OWNERS_REPOSITORY } from '@/owner/application/OwnersKeys';
import OwnerFormVue from '@/owner/infrastructure/primary/OwnerFormVue.vue';
import { routes } from '@/router';
import { flushPromises, mount, type VueWrapper } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import { createMemoryHistory, createRouter, type Router } from 'vue-router';
import { george, stubOwnersRepository } from '../../domain/OwnersFixture';

const memoryRouter = (): Router => createRouter({ history: createMemoryHistory(), routes });

const wrap = (router: Router, props: { ownerId?: string } = {}): VueWrapper =>
  mount(OwnerFormVue, { props, global: { plugins: [router] } });

const fill = async (wrapper: VueWrapper): Promise<void> => {
  await wrapper.find('[data-selector="first-name"]').setValue('George');
  await wrapper.find('[data-selector="last-name"]').setValue('Franklin');
  await wrapper.find('[data-selector="address"]').setValue('110 W. Liberty St.');
  await wrapper.find('[data-selector="city"]').setValue('Madison');
  await wrapper.find('[data-selector="telephone"]').setValue('6085551023');
};

describe('Owner form', () => {
  it('should register owner and show its details', async () => {
    const owners = stubOwnersRepository();
    owners.register.mockResolvedValue(george());
    owners.get.mockResolvedValue(george());
    provide(OWNERS_REPOSITORY, owners);
    const router = memoryRouter();
    const wrapper = wrap(router);

    await fill(wrapper);
    await wrapper.find('form').trigger('submit');
    await flushPromises();

    expect(owners.register).toHaveBeenCalledWith({
      firstName: 'George',
      lastName: 'Franklin',
      address: '110 W. Liberty St.',
      city: 'Madison',
      telephone: '6085551023',
    });
    expect(router.currentRoute.value.params).toEqual({ ownerId: 'george-id' });
  });

  it('should prefill and update owner', async () => {
    const owners = stubOwnersRepository();
    owners.get.mockResolvedValue(george());
    owners.update.mockResolvedValue(george());
    provide(OWNERS_REPOSITORY, owners);
    const wrapper = wrap(memoryRouter(), { ownerId: 'george-id' });
    await flushPromises();

    expect((wrapper.find('[data-selector="last-name"]').element as HTMLInputElement).value).toBe('Franklin');

    await wrapper.find('[data-selector="city"]').setValue('Monona');
    await wrapper.find('form').trigger('submit');
    await flushPromises();

    expect(owners.update).toHaveBeenCalledWith('george-id', expect.objectContaining({ city: 'Monona' }));
  });

  it('should show loading error', async () => {
    const owners = stubOwnersRepository();
    owners.get.mockRejectedValue(new Error('Owner george-id is unknown'));
    provide(OWNERS_REPOSITORY, owners);

    const wrapper = wrap(memoryRouter(), { ownerId: 'george-id' });
    await flushPromises();

    expect(wrapper.find('[data-selector="error"]').text()).toBe('Owner george-id is unknown');
  });

  it('should show saving error', async () => {
    const owners = stubOwnersRepository();
    owners.register.mockRejectedValue(new Error('Telephone "12" must have exactly 10 digits'));
    provide(OWNERS_REPOSITORY, owners);
    const wrapper = wrap(memoryRouter());

    await wrapper.find('form').trigger('submit');
    await flushPromises();

    expect(wrapper.find('[data-selector="error"]').text()).toBe('Telephone "12" must have exactly 10 digits');
  });
});
