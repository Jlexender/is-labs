import { createRouter, createWebHashHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHashHistory(),
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
    },
    {
      path: '/import',
      name: 'import',
      component: () => import('../views/ImportView.vue'),
    },
    {
      path: '/import-history',
      name: 'import-history',
      component: () => import('../views/ImportHistoryView.vue'),
    }
  ],
})

export default router
