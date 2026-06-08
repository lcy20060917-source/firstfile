<template>
  <div class="container py-5">
    <div class="row justify-content-center">
      <div class="col-md-5">
        <div class="card shadow">
          <div class="card-body p-4">
            <h3 class="text-center mb-4">用户注册</h3>

            <div v-if="error" class="alert alert-danger">{{ error }}</div>
            <div v-if="success" class="alert alert-success">
              注册成功！<router-link to="/login">去登录</router-link>
            </div>

            <form v-if="!success" @submit.prevent="handleRegister">
              <div class="form-group">
                <label>用户名 <span class="text-danger">*</span></label>
                <input
                  v-model="form.username"
                  type="text"
                  class="form-control"
                  placeholder="3-50个字符"
                  required
                  minlength="3"
                />
              </div>
              <div class="form-group">
                <label>密码 <span class="text-danger">*</span></label>
                <input
                  v-model="form.password"
                  type="password"
                  class="form-control"
                  placeholder="至少6个字符"
                  required
                  minlength="6"
                />
              </div>
              <div class="form-group">
                <label>邮箱</label>
                <input
                  v-model="form.email"
                  type="email"
                  class="form-control"
                  placeholder="选填"
                />
              </div>
              <div class="form-group">
                <label>手机号</label>
                <input
                  v-model="form.phone"
                  type="text"
                  class="form-control"
                  placeholder="选填，如 13800138000"
                />
              </div>
              <button type="submit" class="btn btn-success btn-block" :disabled="submitting">
                <span v-if="submitting" class="spinner-border spinner-border-sm mr-2"></span>
                {{ submitting ? '注册中...' : '注册' }}
              </button>
            </form>

            <div class="text-center mt-3">
              <span class="text-muted">已有账号？</span>
              <router-link to="/login">立即登录</router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { register } from '../api/auth'

const form = reactive({ username: '', password: '', email: '', phone: '' })
const error = ref('')
const success = ref(false)
const submitting = ref(false)

async function handleRegister() {
  error.value = ''
  // 清理空字符串，避免后端校验空邮箱/手机号格式
  const payload = {
    username: form.username,
    password: form.password
  }
  if (form.email.trim()) payload.email = form.email.trim()
  if (form.phone.trim()) payload.phone = form.phone.trim()

  submitting.value = true
  try {
    await register(payload)
    success.value = true
  } catch (e) {
    error.value = e.response?.data?.message || e.message || '注册失败，请检查网络连接'
  } finally {
    submitting.value = false
  }
}
</script>
