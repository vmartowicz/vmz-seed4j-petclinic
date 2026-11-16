import { provide } from '@/injections';
import { VETS_REPOSITORY } from '@/vet/application/VetsKeys';
import VetsListVue from '@/vet/infrastructure/primary/VetsListVue.vue';
import { flushPromises, mount } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import { james, linda, stubVetsRepository, vetsPage } from '../../domain/VetsFixture';

describe('Vets list', () => {
  it('should list vets with their specialties and paginate', async () => {
    const vets = stubVetsRepository();
    vets.list.mockResolvedValueOnce(vetsPage([james(), linda()], 0, 2));
    vets.list.mockResolvedValueOnce(vetsPage([linda()], 1, 2));
    vets.list.mockResolvedValueOnce(vetsPage([james(), linda()], 0, 2));
    provide(VETS_REPOSITORY, vets);

    const wrapper = mount(VetsListVue);
    await flushPromises();

    const rows = wrapper.findAll('[data-selector="vet"]');
    expect(rows[0]!.text()).toContain('James Carter');
    expect(rows[0]!.text()).toContain('none');
    expect(rows[1]!.text()).toContain('surgery, dentistry');

    await wrapper.find('[data-selector="next"]').trigger('click');
    await flushPromises();

    expect(vets.list).toHaveBeenLastCalledWith(1);
    expect(wrapper.find('[data-selector="page"]').text()).toBe('Page 2 / 2');

    await wrapper.find('[data-selector="previous"]').trigger('click');
    await flushPromises();

    expect(vets.list).toHaveBeenLastCalledWith(0);
    expect(wrapper.find('[data-selector="page"]').text()).toBe('Page 1 / 2');
  });

  it('should show error', async () => {
    const vets = stubVetsRepository();
    vets.list.mockRejectedValue(new Error('Network error'));
    provide(VETS_REPOSITORY, vets);

    const wrapper = mount(VetsListVue);
    await flushPromises();

    expect(wrapper.find('[data-selector="error"]').text()).toBe('Network error');
  });
});
