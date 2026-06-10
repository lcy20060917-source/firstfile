import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', name: 'Home', component: () => import('../views/Home.vue') },
  { path: '/products', name: 'ProductList', component: () => import('../views/ProductList.vue') },
  { path: '/products/:id', name: 'ProductDetail', component: () => import('../views/ProductDetail.vue') },
  { path: '/login', name: 'Login', component: () => import('../views/Login.vue'), meta: { guest: true } },
  { path: '/register', name: 'Register', component: () => import('../views/Register.vue'), meta: { guest: true } },
  { path: '/cart', name: 'Cart', component: () => import('../views/Cart.vue'), meta: { requiresAuth: true } },
  { path: '/checkout', name: 'Checkout', component: () => import('../views/Checkout.vue'), meta: { requiresAuth: true } },
  { path: '/orders', name: 'Orders', component: () => import('../views/Orders.vue'), meta: { requiresAuth: true } },
  { path: '/orders/:id', name: 'OrderDetail', component: () => import('../views/OrderDetail.vue'), meta: { requiresAuth: true } },
  { path: '/order-success/:id', name: 'OrderSuccess', component: () => import('../views/OrderSuccess.vue'), meta: { requiresAuth: true } },
  // 404 通配路由 — 必须放最后
  { path: '/:pathMatch(.*)*', name: 'NotFound', component: () => import('../views/NotFound.vue') }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
  } else if (to.meta.guest && token) {
    next({ name: 'Home' })
  } else {
    next()
  }
})

export default router
