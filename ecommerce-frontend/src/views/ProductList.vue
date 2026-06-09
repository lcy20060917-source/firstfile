<template>
  <div class="container py-4">
    <div class="row">
      <!-- 左侧分类筛选 -->
      <div class="col-md-2 d-none d-md-block">
        <div class="card mb-4">
          <div class="card-header fw-bold">商品分类</div>
          <div class="list-group list-group-flush">
            <a href="#" class="list-group-item list-group-item-action"
              :class="{ active: !activeCategory }"
              @click.prevent="filterByCategory(null)">
              全部分类
            </a>
            <a v-for="cat in categories" :key="cat.id" href="#"
              class="list-group-item list-group-item-action"
              :class="{ active: activeCategory === cat.id }"
              @click.prevent="filterByCategory(cat.id)">
              {{ cat.name }}
            </a>
          </div>
        </div>
      </div>

      <!-- 右侧商品区 -->
      <div class="col-md-10">
        <!-- 搜索框 -->
        <div class="search-box mb-3">
          <div class="input-group">
            <input v-model="keyword" type="text" class="form-control form-control-lg"
              placeholder="搜索商品..." @keyup.enter="doSearch" />
            <div class="input-group-append">
              <button class="btn btn-primary" @click="doSearch">
                <i class="bi bi-search"></i> 搜索
              </button>
            </div>
          </div>
        </div>

        <!-- 状态栏 -->
        <div class="d-flex justify-content-between align-items-center mb-3">
          <div>
            <span v-if="keyword">搜索 "{{ keyword }}" </span>
            <span v-if="activeCategory">分类: {{ categoryName }} </span>
            <span class="text-muted">共 {{ total }} 件</span>
          </div>
          <div class="d-md-none">
            <select v-model="activeCategory" class="form-control form-control-sm"
              @change="filterByCategory(activeCategory)">
              <option :value="null">全部分类</option>
              <option v-for="cat in categories" :key="cat.id" :value="cat.id">
                {{ cat.name }}
              </option>
            </select>
          </div>
        </div>

        <!-- 加载状态 -->
        <div v-if="loading" class="text-center py-5">
          <div class="spinner-border text-primary" role="status">
            <span class="sr-only">加载中...</span>
          </div>
          <p class="text-muted mt-2">正在加载第 {{ page + 1 }} 页...</p>
        </div>

        <!-- 商品网格 -->
        <div v-else>
          <div class="row">
            <ProductCard v-for="product in products" :key="product.id" :product="product" />
          </div>

          <div v-if="products.length === 0" class="text-center text-muted py-5">
            <i class="bi bi-inbox" style="font-size: 3rem;"></i>
            <p class="mt-2">暂无符合条件的商品</p>
          </div>

          <!-- 分页 -->
          <nav v-if="totalPages > 1" class="mt-4">
            <ul class="pagination justify-content-center">
              <li class="page-item" :class="{ disabled: page <= 0 }">
                <a class="page-link" href="#" @click.prevent="changePage(page - 1)">
                  <i class="bi bi-chevron-left"></i> 上一页
                </a>
              </li>

              <template v-for="p in visiblePages" :key="p">
                <li v-if="p === -1" class="page-item disabled">
                  <span class="page-link">...</span>
                </li>
                <li v-else class="page-item" :class="{ active: p === page }">
                  <a class="page-link" href="#" @click.prevent="changePage(p)">{{ p + 1 }}</a>
                </li>
              </template>

              <li class="page-item" :class="{ disabled: page >= totalPages - 1 }">
                <a class="page-link" href="#" @click.prevent="changePage(page + 1)">
                  下一页 <i class="bi bi-chevron-right"></i>
                </a>
              </li>
            </ul>
          </nav>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getProducts } from '../api/product'
import { getCategoryList } from '../mock/products'
import { cancelAllPending } from '../utils/request'
import ProductCard from '../components/ProductCard.vue'

const route = useRoute()
const products = ref([])
const loading = ref(false)
const keyword = ref('')
const page = ref(0)
const size = ref(12)
const total = ref(0)
const totalPages = ref(0)
const activeCategory = ref(null)

const categories = getCategoryList()

const categoryName = computed(() => {
  const c = categories.find(c => c.id === activeCategory.value)
  return c ? c.name : ''
})

// 智能分页按钮（带省略号）
const visiblePages = computed(() => {
  const tp = totalPages.value
  const p = page.value
  if (tp <= 7) {
    return Array.from({ length: tp }, (_, i) => i)
  }
  const pages = []
  pages.push(0)
  if (p > 3) pages.push(-1)
  for (let i = Math.max(1, p - 1); i <= Math.min(tp - 2, p + 1); i++) {
    pages.push(i)
  }
  if (p < tp - 4) pages.push(-1)
  pages.push(tp - 1)
  return pages
})

// 当前请求序号（用于识别过期响应）
let requestSeq = 0

async function fetchProducts() {
  loading.value = true
  const seq = ++requestSeq

  try {
    const params = { page: page.value, size: size.value }
    if (keyword.value) params.keyword = keyword.value
    if (activeCategory.value) params.category = activeCategory.value

    const res = await getProducts(params)

    // 忽略过期响应
    if (seq !== requestSeq) {
      console.log(`[Request] 忽略过期响应 seq=${seq}, current=${requestSeq}`)
      return
    }

    products.value = res.data.content || []
    total.value = res.data.totalElements || 0
    totalPages.value = res.data.totalPages || 0
  } catch (e) {
    if (e?.cancelled) return
    console.error('加载商品失败:', e)
  } finally {
    if (seq === requestSeq) {
      loading.value = false
    }
  }
}

function doSearch() {
  page.value = 0
  fetchProducts()
}

function filterByCategory(catId) {
  activeCategory.value = catId
  page.value = 0
  fetchProducts()
}

function changePage(p) {
  if (p < 0 || p >= totalPages.value) return
  page.value = p
  fetchProducts()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(() => {
  if (route.query.keyword) {
    keyword.value = route.query.keyword
  }
  if (route.query.category) {
    activeCategory.value = Number(route.query.category)
  }
  fetchProducts()
})

onUnmounted(() => {
  cancelAllPending()
})

watch(() => route.query.keyword, (val) => {
  if (val !== undefined) {
    keyword.value = val
    doSearch()
  }
})
</script>
