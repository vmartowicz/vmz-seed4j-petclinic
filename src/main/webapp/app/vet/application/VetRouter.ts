import VetsListVue from '@/vet/infrastructure/primary/VetsListVue.vue';
import type { RouteRecordRaw } from 'vue-router';

export const vetRoutes = (): RouteRecordRaw[] => [{ path: '/vets', name: 'Vets', component: VetsListVue }];
