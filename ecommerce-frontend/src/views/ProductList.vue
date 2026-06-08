<template>
  <div class="container py-4">
    <div class="row">
      <div class="col-12">
        <div class="search-box mb-4">
          <div class="input-group">
            <input
              v-model="keyword"
              type="text"
              class="form-control form-control-lg"
              placeholder="搜索您想要的商品..."
              @keyup.enter="doSearch"
            />
            <div class="input-group-append">
              <button class="btn btn-primary" @click="doSearch">
                <i class="bi bi-search"></i> 搜索
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="d-flex justify-content-between align-items-center mb-3">
      <h5 v-if="keyword">搜索 "{{ keyword }}" 的结果 ({{ total }} 件)</h5>
      <h5 v-else>全部商品 ({{ total }} 件)</h5>
    </div>

    <div v-if="loading" class="loading-spinner">
      <div class="spinner-border text-primary"></div>
    </div>

    <div v-else>
      <div class="row">
        <ProductCard
          v-for="product in products"
          :key="product.id"
          :product="product"
        />
      </div>

      <div v-if="products.length === 0" class="text-center text-muted py-5">
        <i class="bi bi-search" style="font-size: 3rem;"></i>
        <p class="mt-2">未找到相关商品</p>
      </div>

      <!-- 分页 -->
      <nav v-if="totalPages > 1" class="mt-4">
        <ul class="pagination justify-content-center">
          <li class="page-item" :class="{ disabled: page <= 0 }">
            <a class="page-link" href="#" @click.prevent="changePage(page - 1)">上一页</a>
          </li>
          <li
            v-for="p in visiblePages"
            :key="p"
            class="page-item"
            :class="{ active: p === page }"
          >
            <a class="page-link" href="#" @click.prevent="changePage(p)">{{ p + 1 }}</a>
          </li>
          <li class="page-item" :class="{ disabled: page >= totalPages - 1 }">
            <a class="page-link" href="#" @click.prevent="changePage(page + 1)">下一页</a>
          </li>
        </ul>
      </nav>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getProducts } from '../api/product'
import ProductCard from '../components/ProductCard.vue'

const route = useRoute()
const products = ref([])
const loading = ref(true)
const keyword = ref('')
const page = ref(0)
const size = ref(12)
const total = ref(0)
const totalPages = ref(0)

const visiblePages = computed(() => {
  const pages = []
  const start = Math.max(0, page.value - 2)
  const end = Math.min(totalPages.value, start + 5)
  for (let i = start; i < end; i++) pages.push(i)
  return pages
})

async function fetchProducts() {
  loading.value = true
  try {
    const params = { page: page.value, size: size.value }
    if (keyword.value) params.keyword = keyword.value
    const res = await getProducts(params)
    products.value = res.data.content || []
    total.value = res.data.totalElements || 0
    totalPages.value = res.data.totalPages || 0
  } catch (e) {
    console.error('加载商品失败:', e)
  } finally {
    loading.value = false
  }
}

function doSearch() {
  page.value = 0
  fetchProducts()
}

function changePage(p) {
  if (p < 0 || p >= totalPages.value) return
  page.value = p
  fetchProducts()
  window.scrollTo(0, 0)
}

onMounted(() => {
  if (route.query.keyword) {
    keyword.value = route.query.keyword
  }
  fetchProducts()
})

watch(() => route.query.keyword, (val) => {
  if (val) {
    keyword.value = val
    doSearch()
  }
})
</script>
