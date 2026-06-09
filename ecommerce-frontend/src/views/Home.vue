<template>
  <div>
    <div class="jumbotron jumbotron-fluid bg-primary text-white text-center py-5 mb-4">
      <div class="container">
        <h1 class="display-4">欢迎来到电商平台</h1>
        <p class="lead">发现海量好物，享受便捷购物体验</p>
        <router-link to="/products" class="btn btn-light btn-lg">
          <i class="bi bi-shop"></i> 开始购物
        </router-link>
      </div>
    </div>

    <div class="container">
      <!-- 分类入口 -->
      <div class="row mb-4">
        <div class="col-12">
          <h5><i class="bi bi-grid"></i> 商品分类</h5>
        </div>
        <div v-for="cat in topCategories" :key="cat.id" class="col-6 col-md-3 mb-2">
          <router-link :to="{ name: 'ProductList', query: { category: cat.id } }"
            class="btn btn-outline-primary btn-block text-left">
            {{ cat.name }}
          </router-link>
        </div>
      </div>

      <hr />

      <!-- 热门商品 -->
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h3><i class="bi bi-star-fill text-warning"></i> 热门商品</h3>
        <router-link to="/products" class="btn btn-outline-primary btn-sm">
          查看全部 <i class="bi bi-arrow-right"></i>
        </router-link>
      </div>

      <div v-if="loading" class="text-center py-5">
        <div class="spinner-border text-primary" role="status">
          <span class="sr-only">加载中...</span>
        </div>
      </div>

      <div v-else>
        <div class="row">
          <ProductCard v-for="product in products" :key="product.id" :product="product" />
        </div>
        <div v-if="products.length === 0" class="text-center text-muted py-5">
          <i class="bi bi-inbox" style="font-size: 3rem;"></i>
          <p class="mt-2">暂无商品</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { getProducts } from '../api/product'
import { getCategoryList } from '../mock/products'
import { cancelAllPending } from '../utils/request'
import ProductCard from '../components/ProductCard.vue'

const products = ref([])
const loading = ref(true)
const categories = getCategoryList()
const topCategories = categories.filter(c => c.parentId === null)

let requestSeq = 0

onMounted(async () => {
  const seq = ++requestSeq
  try {
    const res = await getProducts({ page: 0, size: 8 })
    if (seq === requestSeq) {
      products.value = res.data.content || []
    }
  } catch (e) {
    if (e?.cancelled) return
    console.error('加载商品失败:', e)
  } finally {
    if (seq === requestSeq) loading.value = false
  }
})

onUnmounted(() => {
  cancelAllPending()
})
</script>
