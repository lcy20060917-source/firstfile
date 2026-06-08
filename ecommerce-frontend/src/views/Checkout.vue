<template>
  <div class="container py-4">
    <h3 class="mb-4"><i class="bi bi-credit-card"></i> 确认订单</h3>

    <div v-if="loading" class="loading-spinner">
      <div class="spinner-border text-primary"></div>
    </div>

    <div v-else-if="cart.items && cart.items.length > 0">
      <div class="row">
        <!-- 收货信息 -->
        <div class="col-md-8">
          <div class="card mb-4">
            <div class="card-header"><h5 class="mb-0">收货信息</h5></div>
            <div class="card-body">
              <div class="form-group">
                <label>收货人 <span class="text-danger">*</span></label>
                <input v-model="form.receiverName" class="form-control" placeholder="请输入收货人姓名" required />
              </div>
              <div class="form-group">
                <label>联系电话 <span class="text-danger">*</span></label>
                <input v-model="form.receiverPhone" class="form-control" placeholder="请输入联系电话" required />
              </div>
              <div class="form-group">
                <label>收货地址 <span class="text-danger">*</span></label>
                <textarea v-model="form.receiverAddress" class="form-control" rows="3" placeholder="请输入详细收货地址" required />
              </div>
            </div>
          </div>

          <!-- 商品清单 -->
          <div class="card mb-4">
            <div class="card-header"><h5 class="mb-0">商品清单</h5></div>
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
                  <tr v-for="item in cart.items" :key="item.id">
                    <td>{{ item.productName }}</td>
                    <td>¥{{ item.productPrice }}</td>
                    <td>{{ item.quantity }}</td>
                    <td>¥{{ item.subtotal }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <!-- 订单汇总 -->
        <div class="col-md-4">
          <div class="card">
            <div class="card-header"><h5 class="mb-0">订单汇总</h5></div>
            <div class="card-body">
              <div class="d-flex justify-content-between mb-2">
                <span>商品总数</span>
                <span>{{ cart.totalCount }} 件</span>
              </div>
              <div class="d-flex justify-content-between mb-2">
                <span>商品金额</span>
                <span>¥{{ cart.totalAmount }}</span>
              </div>
              <hr />
              <div class="d-flex justify-content-between mb-3">
                <strong>应付总额</strong>
                <strong class="text-danger" style="font-size: 1.3rem;">¥{{ cart.totalAmount }}</strong>
              </div>
              <button class="btn btn-danger btn-block btn-lg" :disabled="submitting" @click="handleSubmit">
                <span v-if="submitting" class="spinner-border spinner-border-sm mr-2"></span>
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
const cart = ref({ items: [], totalAmount: 0, totalCount: 0 })
const loading = ref(true)
const submitting = ref(false)
const form = reactive({ receiverName: '', receiverPhone: '', receiverAddress: '' })

async function fetchCart() {
  try {
    const res = await getCart()
    cart.value = res.data
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
    cartStore.setCount(0)
    alert('下单成功！订单号: ' + res.data.orderNo)
    router.push({ name: 'OrderDetail', params: { id: res.data.id } })
  } catch (e) {
    alert('下单失败: ' + e.message)
  } finally {
    submitting.value = false
  }
}

onMounted(fetchCart)
</script>
