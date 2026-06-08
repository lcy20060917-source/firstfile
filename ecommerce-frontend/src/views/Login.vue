<template>
  <div class="container py-5">
    <div class="row justify-content-center">
      <div class="col-md-5">
        <div class="card shadow">
          <div class="card-body p-4">
            <h3 class="text-center mb-4">用户登录</h3>

            <div v-if="error" class="alert alert-danger">{{ error }}</div>

            <form @submit.prevent="handleLogin">
              <div class="form-group">
                <label>用户名</label>
                <input
                  v-model="form.username"
                  type="text"
                  class="form-control"
                  placeholder="请输入用户名"
                  required
                />
              </div>
              <div class="form-group">
                <label>密码</label>
                <input
                  v-model="form.password"
                  type="password"
                  class="form-control"
                  placeholder="请输入密码"
                  required
                />
              </div>
              <button type="submit" class="btn btn-primary btn-block" :disabled="submitting">
                <span v-if="submitting" class="spinner-border spinner-border-sm mr-2"></span>
                {{ submitting ? '登录中...' : '登录' }}
              </button>
            </form>

            <div class="text-center mt-3">
              <span class="text-muted">还没有账号？</span>
              <router-link to="/register">立即注册</router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import { login } from '../api/auth'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const form = reactive({ username: '', password: '' })
const error = ref('')
const submitting = ref(false)

async function handleLogin() {
  error.value = ''
  submitting.value = true
  try {
    const res = await login(form.username, form.password)
    userStore.setAuth(res.data.token, form.username)
    const redirect = route.query.redirect || '/'
    router.push(redirect)
  } catch (e) {
    error.value = e.message || '登录失败'
  } finally {
    submitting.value = false
  }
}
</script>
