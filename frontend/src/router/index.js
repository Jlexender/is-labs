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
    }
  ],
})

export default router
