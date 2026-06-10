<template>
  <div class="container py-5 text-center">
    <div style="font-size: 5rem; color: #2ecc71;">
      <i class="bi bi-check-circle-fill"></i>
    </div>
    <h3 class="mb-3">下单成功！</h3>
    <p class="text-muted mb-2">订单号: <strong>{{ orderNo }}</strong></p>
    <p class="text-muted mb-4">应付金额: <strong class="text-danger">&yen;{{ totalAmount }}</strong></p>
    <div class="d-flex justify-content-center gap-3">
      <router-link :to="{ name: 'OrderDetail', params: { id: orderId } }"
        class="btn btn-primary btn-lg">
        <i class="bi bi-eye"></i> 查看订单
      </router-link>
      <router-link to="/orders" class="btn btn-outline-primary btn-lg">
        <i class="bi bi-list-check"></i> 我的订单
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const orderId = ref(null)
const orderNo = ref('')
const totalAmount = ref('')

onMounted(() => {
  orderId.value = route.params.id
  orderNo.value = route.query.orderNo || ''
  totalAmount.value = route.query.amount || ''
})
</script>
