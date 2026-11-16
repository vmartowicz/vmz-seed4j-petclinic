import OwnerDetailsVue from '@/owner/infrastructure/primary/OwnerDetailsVue.vue';
import OwnerFormVue from '@/owner/infrastructure/primary/OwnerFormVue.vue';
import OwnersSearchVue from '@/owner/infrastructure/primary/OwnersSearchVue.vue';
import PetFormVue from '@/owner/infrastructure/primary/PetFormVue.vue';
import VisitFormVue from '@/owner/infrastructure/primary/VisitFormVue.vue';
import type { RouteRecordRaw } from 'vue-router';

export const ownerRoutes = (): RouteRecordRaw[] => [
  { path: '/owners', name: 'OwnersSearch', component: OwnersSearchVue },
  { path: '/owners/new', name: 'OwnerCreation', component: OwnerFormVue },
  { path: '/owners/:ownerId', name: 'OwnerDetails', component: OwnerDetailsVue, props: true },
  { path: '/owners/:ownerId/edit', name: 'OwnerEdition', component: OwnerFormVue, props: true },
  { path: '/owners/:ownerId/pets/new', name: 'PetCreation', component: PetFormVue, props: true },
  { path: '/owners/:ownerId/pets/:petId/edit', name: 'PetEdition', component: PetFormVue, props: true },
  { path: '/owners/:ownerId/pets/:petId/visits/new', name: 'VisitCreation', component: VisitFormVue, props: true },
];
