<template>
  <div class="container py-4">
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb">
        <li class="breadcrumb-item"><router-link to="/">首页</router-link></li>
        <li class="breadcrumb-item"><router-link to="/products">商品列表</router-link></li>
        <li class="breadcrumb-item active">{{ product.name }}</li>
      </ol>
    </nav>

    <div v-if="loading" class="loading-spinner">
      <div class="spinner-border text-primary"></div>
    </div>

    <div v-else-if="product.id" class="card">
      <div class="card-body">
        <div class="row">
          <!-- 商品图片 -->
          <div class="col-md-5 position-relative">
            <img
              :src="product.imageUrl || 'https://via.placeholder.com/400x400?text=No+Image'"
              class="img-fluid rounded"
              :alt="product.name"
            />
          </div>

          <!-- 商品信息 -->
          <div class="col-md-7">
            <h3>{{ product.name }}</h3>

            <!-- 子组件1: 促销标签 -->
            <div class="mb-2" v-if="promo">
              <PromoTag
                v-for="tag in promo.tags"
                :key="tag.type"
                :type="tag.type"
                :label="tag.label"
                :color="tag.color"
              />
            </div>

            <div class="price my-3" style="font-size: 2rem; color: #e74c3c;">
              <span v-if="promo?.originalPrice" class="mr-2">
                <s class="text-muted" style="font-size: 1rem;">
                  &yen;{{ promo.originalPrice }}
                </s>
              </span>
              &yen;{{ product.price }}
            </div>

            <!-- 子组件2: 倒计时 -->
            <div class="mb-3" v-if="promo?.endTime">
              <CountdownTimer
                :end-time="promo.endTime"
                :title="promo.countdownTitle || '限时优惠'"
                @expired="onPromoExpired"
              />
            </div>

            <hr />
            <p class="text-muted">{{ product.description || '暂无描述' }}</p>
            <div class="mt-3">
              <span class="badge" :class="product.stock > 0 ? 'badge-success' : 'badge-danger'">
                {{ product.stock > 0 ? `有货 (库存: ${product.stock})` : '已售罄' }}
              </span>
            </div>
            <div class="mt-4">
              <div class="quantity-control mb-3">
                <label class="mr-3 font-weight-bold">数量:</label>
                <button class="btn btn-outline-secondary btn-sm" @click="qty > 1 && qty--">-</button>
                <input v-model.number="qty" type="number" class="form-control form-control-sm" min="1" />
                <button class="btn btn-outline-secondary btn-sm" @click="qty++">+</button>
              </div>
              <button
                class="btn btn-danger btn-lg mr-3"
                :disabled="product.stock <= 0"
                @click="handleAddToCart"
              >
                <i class="bi bi-cart-plus"></i> 加入购物车
              </button>
              <button
                class="btn btn-warning btn-lg"
                :disabled="product.stock <= 0"
                @click="handleBuyNow"
              >
                <i class="bi bi-lightning-fill"></i> 立即购买
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="text-center py-5">
      <p class="text-muted">商品不存在</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { getProductDetail } from '../api/product'
import { addToCart } from '../api/cart'
import PromoTag from '../components/PromoTag.vue'
import CountdownTimer from '../components/CountdownTimer.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const product = ref({})
const loading = ref(true)
const qty = ref(1)
const promoExpired = ref(false)

// 父组件计算的促销数据，传给子组件
const promo = computed(() => {
  if (promoExpired.value) return null
  const p = product.value
  if (!p || !p.promo) return null

  const now = Date.now()
  if (p.promo.endTime && new Date(p.promo.endTime).getTime() < now) {
    return null
  }
  return {
    tags: p.promo.tags || [],
    endTime: p.promo.endTime || null,
    countdownTitle: p.promo.countdownTitle || '限时优惠',
    originalPrice: p.promo.originalPrice || null
  }
})

function onPromoExpired() {
  promoExpired.value = true
}

async function fetchProduct() {
  loading.value = true
  try {
    const res = await getProductDetail(route.params.id)
    product.value = res.data
  } catch (e) {
    console.error('加载商品详情失败:', e)
  } finally {
    loading.value = false
  }
}

async function handleAddToCart() {
  if (!userStore.isLoggedIn) {
    router.push({ name: 'Login', query: { redirect: route.fullPath } })
    return
  }
  try {
    await addToCart(product.value.id, qty.value)
    alert('已加入购物车！')
  } catch (e) {
    alert('操作失败: ' + e.message)
  }
}

async function handleBuyNow() {
  if (!userStore.isLoggedIn) {
    router.push({ name: 'Login', query: { redirect: '/checkout' } })
    return
  }
  await addToCart(product.value.id, qty.value)
  router.push('/cart')
}

onMounted(fetchProduct)
</script>
