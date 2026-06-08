<template>
  <div class="col-lg-3 col-md-4 col-sm-6 mb-4">
    <div class="card product-card h-100" @click="goDetail">
      <img
        :src="product.imageUrl || 'https://via.placeholder.com/300x200?text=No+Image'"
        class="card-img-top"
        :alt="product.name"
      />
      <div class="card-body d-flex flex-column">
        <h5 class="card-title">{{ product.name }}</h5>
        <p class="card-text text-muted small flex-grow-1">
          {{ truncatedDesc }}
        </p>
        <div class="d-flex justify-content-between align-items-center mt-2">
          <span class="price">¥{{ product.price }}</span>
          <button class="btn btn-sm btn-primary" @click.stop="handleAddToCart">
            <i class="bi bi-cart-plus"></i> 加入购物车
          </button>
        </div>
        <small class="text-muted mt-1" v-if="product.stock !== undefined">
          {{ product.stock > 0 ? `库存: ${product.stock}` : '已售罄' }}
        </small>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { addToCart } from '../api/cart'

const props = defineProps({
  product: {
    type: Object,
    required: true
  }
})

const router = useRouter()
const userStore = useUserStore()

const truncatedDesc = computed(() => {
  const desc = props.product.description || ''
  return desc.length > 60 ? desc.substring(0, 60) + '...' : desc
})

function goDetail() {
  router.push({ name: 'ProductDetail', params: { id: props.product.id } })
}

async function handleAddToCart() {
  if (!userStore.isLoggedIn) {
    router.push({ name: 'Login', query: { redirect: router.currentRoute.value.fullPath } })
    return
  }
  try {
    await addToCart(props.product.id, 1)
    alert('已加入购物车！')
  } catch (e) {
    alert('加入购物车失败: ' + e.message)
  }
}
</script>
