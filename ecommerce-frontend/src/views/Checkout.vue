<template>
  <div class="container py-4">
    <h3 class="mb-4"><i class="bi bi-credit-card"></i> 确认订单</h3>

    <div v-if="loading" class="loading-spinner">
      <div class="spinner-border text-primary"></div>
    </div>

    <div v-else-if="!cartStore.isEmpty">
      <div class="row">
        <div class="col-md-8">
          <div class="card mb-4">
            <div class="card-header"><h5 class="mb-0">收货信息</h5></div>
            <div class="card-body">
              <div class="form-group">
                <label>收货人 <span class="text-danger">*</span></label>
                <input v-model="form.receiverName" class="form-control" required />
              </div>
              <div class="form-group">
                <label>联系电话 <span class="text-danger">*</span></label>
                <input v-model="form.receiverPhone" class="form-control" required />
              </div>
              <div class="form-group">
                <label>收货地址 <span class="text-danger">*</span></label>
                <textarea v-model="form.receiverAddress" class="form-control" rows="3" required />
              </div>
            </div>
          </div>

          <div class="card mb-4">
            <div class="card-header"><h5 class="mb-0">商品清单</h5></div>
            <div class="card-body p-0">
              <table class="table mb-0">
                <thead class="thead-light">
                  <tr><th>商品</th><th>单价</th><th>数量</th><th>小计</th></tr>
                </thead>
                <tbody>
                  <tr v-for="item in cartStore.items" :key="item.productId">
                    <td>{{ item.productName }}</td>
                    <td>&yen;{{ item.productPrice }}</td>
                    <td>{{ item.quantity }}</td>
                    <td>&yen;{{ (item.productPrice * item.quantity).toFixed(2) }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <div class="col-md-4">
          <div class="card">
            <div class="card-header"><h5 class="mb-0">订单汇总</h5></div>
            <div class="card-body">
              <div class="d-flex justify-content-between mb-2">
                <span>商品总数</span><span>{{ cartStore.totalCount }} 件</span>
              </div>
              <div class="d-flex justify-content-between mb-2">
                <span>商品金额</span><span>&yen;{{ cartStore.totalAmount.toFixed(2) }}</span>
              </div>
              <hr />
              <div class="d-flex justify-content-between mb-3">
                <strong>应付总额</strong>
                <strong class="text-danger" style="font-size: 1.3rem;">&yen;{{ cartStore.totalAmount.toFixed(2) }}</strong>
              </div>
              <button class="btn btn-danger btn-block btn-lg" :disabled="submitting" @click="handleSubmit">
                {{ submitting ? '提交中...' : '提交订单' }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="text-center py-5">
      <p class="text-muted">购物车为空，无法下单</p>
      <router-link to="/cart" class="btn btn-primary">返回购物车</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '../stores/cart'
import { getCart } from '../api/cart'
import { createOrder } from '../api/order'

const router = useRouter()
const cartStore = useCartStore()
const loading = ref(true)
const submitting = ref(false)
const form = reactive({ receiverName: '', receiverPhone: '', receiverAddress: '' })

async function fetchCart() {
  try {
    const res = await getCart()
    cartStore.setFromServer(res.data)
  } catch (e) {
    console.error('加载购物车失败:', e)
  } finally {
    loading.value = false
  }
}

async function handleSubmit() {
  if (!form.receiverName || !form.receiverPhone || !form.receiverAddress) {
    alert('请填写完整的收货信息')
    return
  }
  submitting.value = true
  try {
    const res = await createOrder(form)
    const order = res.data
    cartStore.clear()
    // 下单成功后跳转到成功页面
    router.push({
      name: 'OrderSuccess',
      params: { id: order.id },
      query: { orderNo: order.orderNo, amount: order.totalAmount }
    })
  } catch (e) {
    alert('下单失败: ' + e.message)
  } finally {
    submitting.value = false
  }
}

onMounted(fetchCart)
</script>
