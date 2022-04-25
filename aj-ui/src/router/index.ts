import Vue from 'vue'
import VueRouter, { RouteConfig } from 'vue-router'
// import Home from '../views/Home.vue'

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
  {
    path: '/',
    name: 'Home',
    component: () => import(/* webpackChunkName: "about" */ '../pages/index.vue')
  },
  {
    path: '/data-service/info',
    name: 'Home2',
    component: () => import(/* webpackChunkName: "about" */ '../pages/data-service-info.vue')
  },

  {
    path: '/factory-list-info',
    component: () => import('../pages/factory-list-info.vue')
  },
  {
    path: '/factory-form-info',
    component: () => import('../pages/factory-form-info.vue')
  },
  {
    path: '/form-designer',
    component: () => import('../components/form-designer/demo.vue')
  },
  {
    path: '/work-flow',
    component: () => import('../components/work-flow/index.vue')
  },
  {
    path: '/list',
    component: () => import('../pages/list-loader.vue')
  },
  {
    path: '/form',
    component: () => import('../pages/form-loader.vue')
  }
]

const router = new VueRouter({
  routes
})

export default router;