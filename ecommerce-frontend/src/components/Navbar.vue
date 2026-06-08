<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
    <div class="container">
      <router-link class="navbar-brand" to="/">
        <i class="bi bi-cart3"></i> 电商平台
      </router-link>

      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item">
            <router-link class="nav-link" to="/">首页</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/products">全部商品</router-link>
          </li>
        </ul>

        <div class="search-box mr-3 d-none d-md-flex">
          <div class="input-group input-group-sm">
            <input
              v-model="keyword"
              type="text"
              class="form-control"
              placeholder="搜索商品..."
              @keyup.enter="search"
            />
            <div class="input-group-append">
              <button class="btn btn-outline-light" @click="search">
                <i class="bi bi-search"></i>
              </button>
            </div>
          </div>
        </div>

        <ul class="navbar-nav">
          <li class="nav-item" v-if="!userStore.isLoggedIn">
            <router-link class="nav-link" to="/login">
              <i class="bi bi-person"></i> 登录
            </router-link>
          </li>
          <li class="nav-item" v-if="!userStore.isLoggedIn">
            <router-link class="nav-link" to="/register">注册</router-link>
          </li>

          <li class="nav-item" v-if="userStore.isLoggedIn">
            <router-link class="nav-link" to="/cart">
              <i class="bi bi-cart-fill"></i> 购物车
              <span v-if="cartStore.count > 0" class="badge badge-pill badge-danger">
                {{ cartStore.count }}
              </span>
            </router-link>
          </li>
          <li class="nav-item dropdown" v-if="userStore.isLoggedIn">
            <a class="nav-link dropdown-toggle" href="#" data-toggle="dropdown">
              <i class="bi bi-person-circle"></i> {{ userStore.username }}
            </a>
            <div class="dropdown-menu dropdown-menu-right">
              <router-link class="dropdown-item" to="/orders">
                <i class="bi bi-list-check"></i> 我的订单
              </router-link>
              <div class="dropdown-divider"></div>
              <a class="dropdown-item" href="#" @click.prevent="handleLogout">
                <i class="bi bi-box-arrow-right"></i> 退出登录
              </a>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { useCartStore } from '../stores/cart'

const router = useRouter()
const userStore = useUserStore()
const cartStore = useCartStore()

const keyword = ref('')

function search() {
  if (keyword.value.trim()) {
    router.push({ name: 'ProductList', query: { keyword: keyword.value.trim() } })
  }
}

function handleLogout() {
  userStore.logout()
  cartStore.setCount(0)
  router.push({ name: 'Home' })
}
</script>
