<template>
  <div class="container py-4">
    <h3 class="mb-4"><i class="bi bi-list-check"></i> 我的订单</h3>

    <div v-if="loading" class="loading-spinner">
      <div class="spinner-border text-primary"></div>
    </div>

    <div v-else-if="orders.length > 0">
      <div v-for="order in orders" :key="order.id" class="card order-card">
        <div class="order-header">
          <div>
            <small class="text-muted">订单号: {{ order.orderNo }}</small>
            <br />
            <small class="text-muted">{{ formatDate(order.createTime) }}</small>
          </div>
          <div class="text-right">
            <span class="badge" :class="statusClass(order.status)">
              {{ order.statusDesc }}
            </span>
            <br />
            <span class="text-danger font-weight-bold">¥{{ order.totalAmount }}</span>
          </div>
        </div>
        <div class="card-body">
          <div class="d-flex justify-content-between align-items-center">
            <div>
              <small class="text-muted">
                收货人: {{ order.receiverName }} | {{ order.receiverPhone }}
              </small>
            </div>
            <div>
              <button
                v-if="order.status === 0"
                class="btn btn-sm btn-outline-danger mr-2"
                @click="handleCancel(order.id)"
              >
                取消订单
              </button>
              <router-link
                :to="{ name: 'OrderDetail', params: { id: order.id } }"
                class="btn btn-sm btn-outline-primary"
              >
                查看详情
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <nav v-if="totalPages > 1" class="mt-4">
        <ul class="pagination justify-content-center">
          <li class="page-item" :class="{ disabled: page <= 0 }">
            <a class="page-link" href="#" @click.prevent="changePage(page - 1)">上一页</a>
          </li>
          <li class="page-item" :class="{ disabled: page >= totalPages - 1 }">
            <a class="page-link" href="#" @click.prevent="changePage(page + 1)">下一页</a>
          </li>
        </ul>
      </nav>
    </div>

    <div v-else class="text-center py-5">
      <i class="bi bi-inbox" style="font-size: 3rem; color: #ccc;"></i>
      <p class="mt-3 text-muted">暂无订单</p>
      <router-link to="/products" class="btn btn-primary">去购物</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getOrders, cancelOrder } from '../api/order'

const orders = ref([])
const loading = ref(true)
const page = ref(0)
const totalPages = ref(0)

function statusClass(status) {
  switch (status) {
    case 0: return 'badge-warning'
    case 1: return 'badge-success'
    case 2: return 'badge-secondary'
    default: return 'badge-info'
  }
}

function formatDate(dateStr) {
  return dateStr ? dateStr.replace('T', ' ').substring(0, 19) : ''
}

async function fetchOrders() {
  loading.value = true
  try {
    const res = await getOrders({ page: page.value, size: 10 })
    orders.value = res.data.content || []
    totalPages.value = res.data.totalPages || 0
  } catch (e) {
    console.error('加载订单失败:', e)
  } finally {
    loading.value = false
  }
}

function changePage(p) {
  page.value = p
  fetchOrders()
}

async function handleCancel(orderId) {
  if (!confirm('确定要取消该订单吗？')) return
  try {
    await cancelOrder(orderId)
    alert('订单已取消')
    await fetchOrders()
  } catch (e) {
    alert('取消失败: ' + e.message)
  }
}

onMounted(fetchOrders)
</script>
