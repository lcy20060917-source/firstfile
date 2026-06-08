<template>
  <div class="container py-4">
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb">
        <li class="breadcrumb-item"><router-link to="/">首页</router-link></li>
        <li class="breadcrumb-item"><router-link to="/orders">我的订单</router-link></li>
        <li class="breadcrumb-item active">订单详情</li>
      </ol>
    </nav>

    <div v-if="loading" class="loading-spinner">
      <div class="spinner-border text-primary"></div>
    </div>

    <div v-else-if="order.id">
      <!-- 订单状态 -->
      <div class="card mb-4">
        <div class="card-body">
          <div class="row">
            <div class="col-md-8">
              <h5>订单号: {{ order.orderNo }}</h5>
              <small class="text-muted">创建时间: {{ formatDate(order.createTime) }}</small>
            </div>
            <div class="col-md-4 text-right">
              <span class="badge badge-lg" :class="statusClass(order.status)" style="font-size: 1rem;">
                {{ order.statusDesc }}
              </span>
              <br />
              <span class="text-danger font-weight-bold" style="font-size: 1.5rem;">
                ¥{{ order.totalAmount }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <div class="row">
        <!-- 商品列表 -->
        <div class="col-md-8">
          <div class="card mb-4">
            <div class="card-header"><h5 class="mb-0">商品列表</h5></div>
            <div class="card-body p-0">
              <table class="table mb-0">
                <thead class="thead-light">
                  <tr>
                    <th>商品</th>
                    <th>单价</th>
                    <th>数量</th>
                    <th>小计</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="item in order.items" :key="item.id">
                    <td>{{ item.productName }}</td>
                    <td>¥{{ item.productPrice }}</td>
                    <td>{{ item.quantity }}</td>
                    <td>¥{{ item.totalPrice }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <!-- 收货信息 -->
        <div class="col-md-4">
          <div class="card mb-4">
            <div class="card-header"><h5 class="mb-0">收货信息</h5></div>
            <div class="card-body">
              <p><strong>收货人:</strong> {{ order.receiverName }}</p>
              <p><strong>电话:</strong> {{ order.receiverPhone }}</p>
              <p><strong>地址:</strong> {{ order.receiverAddress }}</p>
            </div>
          </div>

          <div class="card">
            <div class="card-header"><h5 class="mb-0">订单汇总</h5></div>
            <div class="card-body">
              <div class="d-flex justify-content-between mb-2">
                <span>商品总额</span>
                <span>¥{{ order.totalAmount }}</span>
              </div>
              <hr />
              <div class="d-flex justify-content-between">
                <strong>实付金额</strong>
                <strong class="text-danger">¥{{ order.totalAmount }}</strong>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="text-center py-5">
      <p class="text-muted">订单不存在</p>
      <router-link to="/orders" class="btn btn-primary">返回订单列表</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getOrderDetail } from '../api/order'

const route = useRoute()
const order = ref({})
const loading = ref(true)

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

async function fetchOrder() {
  loading.value = true
  try {
    const res = await getOrderDetail(route.params.id)
    order.value = res.data
  } catch (e) {
    console.error('加载订单详情失败:', e)
  } finally {
    loading.value = false
  }
}

onMounted(fetchOrder)
</script>
