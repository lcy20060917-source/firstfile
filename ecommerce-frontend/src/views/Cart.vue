<template>
  <div class="container py-4">
    <h3 class="mb-4"><i class="bi bi-cart-fill"></i> 我的购物车</h3>

    <div v-if="loading" class="loading-spinner">
      <div class="spinner-border text-primary"></div>
    </div>

    <div v-else-if="cart.items && cart.items.length > 0">
      <div class="card mb-4">
        <div class="table-responsive">
          <table class="table table-hover mb-0">
            <thead class="thead-light">
              <tr>
                <th>商品</th>
                <th>单价</th>
                <th>数量</th>
                <th>小计</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in cart.items" :key="item.id">
                <td>
                  <div class="d-flex align-items-center">
                    <img
                      :src="item.productImageUrl || 'https://via.placeholder.com/60x60'"
                      width="60"
                      height="60"
                      class="rounded mr-3"
                      :alt="item.productName"
                    />
                    <span>{{ item.productName }}</span>
                  </div>
                </td>
                <td>¥{{ item.productPrice }}</td>
                <td>
                  <div class="quantity-control">
                    <button class="btn btn-sm btn-outline-secondary" @click="updateQty(item, item.quantity - 1)">-</button>
                    <input v-model.number="item.quantity" class="form-control form-control-sm" min="1" @change="updateQty(item, item.quantity)" />
                    <button class="btn btn-sm btn-outline-secondary" @click="updateQty(item, item.quantity + 1)">+</button>
                  </div>
                </td>
                <td class="text-danger font-weight-bold">¥{{ item.subtotal }}</td>
                <td>
                  <button class="btn btn-sm btn-outline-danger" @click="removeItem(item.productId)">
                    <i class="bi bi-trash"></i>
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="card">
        <div class="card-body">
          <div class="d-flex justify-content-between align-items-center">
            <div>
              <button class="btn btn-outline-danger btn-sm mr-2" @click="handleClear">清空购物车</button>
              <router-link to="/products" class="btn btn-outline-secondary btn-sm">继续购物</router-link>
            </div>
            <div>
              <span class="mr-3">
                共 <strong>{{ cart.totalCount }}</strong> 件商品
              </span>
              <span class="mr-3">
                合计: <strong class="text-danger" style="font-size: 1.5rem;">¥{{ cart.totalAmount }}</strong>
              </span>
              <router-link to="/checkout" class="btn btn-danger btn-lg">
                <i class="bi bi-credit-card"></i> 去结算
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="text-center py-5">
      <i class="bi bi-cart" style="font-size: 4rem; color: #ccc;"></i>
      <p class="mt-3 text-muted">购物车是空的</p>
      <router-link to="/products" class="btn btn-primary">去逛逛</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useCartStore } from '../stores/cart'
import { getCart, updateCartQuantity, removeFromCart, clearCart } from '../api/cart'

const cartStore = useCartStore()
const cart = ref({ items: [], totalAmount: 0, totalCount: 0 })
const loading = ref(true)

async function fetchCart() {
  loading.value = true
  try {
    const res = await getCart()
    cart.value = res.data
    cartStore.setCount(res.data.totalCount || 0)
  } catch (e) {
    console.error('加载购物车失败:', e)
  } finally {
    loading.value = false
  }
}

async function updateQty(item, newQty) {
  if (newQty < 1) {
    await removeItem(item.productId)
    return
  }
  try {
    await updateCartQuantity(item.productId, newQty)
    await fetchCart()
  } catch (e) {
    alert('更新失败: ' + e.message)
  }
}

async function removeItem(productId) {
  if (!confirm('确定要移除该商品吗？')) return
  try {
    await removeFromCart(productId)
    await fetchCart()
  } catch (e) {
    alert('移除失败: ' + e.message)
  }
}

async function handleClear() {
  if (!confirm('确定要清空购物车吗？')) return
  try {
    await clearCart()
    cartStore.setCount(0)
    cart.value = { items: [], totalAmount: 0, totalCount: 0 }
  } catch (e) {
    alert('清空失败: ' + e.message)
  }
}

onMounted(fetchCart)
</script>
