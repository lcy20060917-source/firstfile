<template>
  <div class="container py-4">
    <h3 class="mb-4"><i class="bi bi-cart-fill"></i> 我的购物车</h3>

    <div v-if="loading" class="loading-spinner">
      <div class="spinner-border text-primary"></div>
    </div>

    <div v-else-if="!cartStore.isEmpty">
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
              <tr v-for="item in cartStore.items" :key="item.productId">
                <td>
                  <div class="d-flex align-items-center">
                    <img :src="item.productImageUrl || 'https://via.placeholder.com/60x60'"
                      width="60" height="60" class="rounded mr-3" :alt="item.productName" />
                    <span>{{ item.productName }}</span>
                  </div>
                </td>
                <td>&yen;{{ item.productPrice }}</td>
                <td>
                  <div class="quantity-control">
                    <button class="btn btn-sm btn-outline-secondary"
                      @click="decrease(item)">-</button>
                    <input v-model.number="item.quantity" class="form-control form-control-sm"
                      min="1" @change="changeQty(item)" />
                    <button class="btn btn-sm btn-outline-secondary"
                      @click="increase(item)">+</button>
                  </div>
                </td>
                <td class="text-danger font-weight-bold">&yen;{{ (item.productPrice * item.quantity).toFixed(2) }}</td>
                <td>
                  <button class="btn btn-sm btn-outline-danger" @click="doRemove(item.productId)">
                    <i class="bi bi-trash"></i> 删除
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- 底部操作栏 -->
      <div class="card">
        <div class="card-body">
          <div class="d-flex justify-content-between align-items-center">
            <div>
              <button class="btn btn-outline-danger mr-2" @click="doClear">
                <i class="bi bi-trash"></i> 清空购物车
              </button>
              <router-link to="/products" class="btn btn-outline-secondary">
                <i class="bi bi-arrow-left"></i> 继续购物
              </router-link>
            </div>
            <div class="text-right">
              <div class="mb-1">
                共 <strong>{{ cartStore.totalCount }}</strong> 件商品
              </div>
              <div>
                合计:
                <strong class="text-danger" style="font-size: 1.5rem;">
                  &yen;{{ cartStore.totalAmount.toFixed(2) }}
                </strong>
              </div>
              <router-link to="/checkout" class="btn btn-danger btn-lg mt-2">
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
const loading = ref(true)

async function fetchCart() {
  loading.value = true
  try {
    const res = await getCart()
    // 同步到 Pinia 全局状态
    cartStore.setFromServer(res.data)
  } catch (e) {
    console.error('加载购物车失败:', e)
  } finally {
    loading.value = false
  }
}

async function increase(item) {
  await syncQty(item.productId, item.quantity + 1)
}

async function decrease(item) {
  if (item.quantity <= 1) {
    await doRemove(item.productId)
    return
  }
  await syncQty(item.productId, item.quantity - 1)
}

async function changeQty(item) {
  if (item.quantity < 1) {
    item.quantity = 1
  }
  await syncQty(item.productId, item.quantity)
}

async function syncQty(productId, quantity) {
  try {
    await updateCartQuantity(productId, quantity)
    cartStore.updateQuantity(productId, quantity)
  } catch (e) {
    alert('更新失败: ' + e.message)
    await fetchCart()
  }
}

async function doRemove(productId) {
  if (!confirm('确定要移除该商品吗？')) return
  try {
    await removeFromCart(productId)
    cartStore.removeItem(productId)
  } catch (e) {
    alert('移除失败: ' + e.message)
  }
}

async function doClear() {
  if (!confirm('确定要清空购物车吗？')) return
  try {
    await clearCart()
    cartStore.clear()
  } catch (e) {
    alert('清空失败: ' + e.message)
  }
}

onMounted(fetchCart)
</script>
