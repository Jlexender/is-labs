import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/special-ops',
      name: 'special-ops',
      component: () => import('../views/SpecialOpsView.vue'),
    }
  ],
})

export default router
