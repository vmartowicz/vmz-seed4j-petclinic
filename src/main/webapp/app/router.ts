import { homeRoutes } from '@/home/application/HomeRouter';
import { ownerRoutes } from '@/owner/application/OwnerRouter';
import { vetRoutes } from '@/vet/application/VetRouter';
import { createRouter, createWebHistory } from 'vue-router';

export const routes = [...homeRoutes(), ...ownerRoutes(), ...vetRoutes()];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
