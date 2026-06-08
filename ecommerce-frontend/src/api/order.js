import api from './index'

export function createOrder(data) {
  return api.post('/orders', data)
}

export function getOrders(params = {}) {
  return api.get('/orders', { params })
}

export function getOrderDetail(id) {
  return api.get(`/orders/${id}`)
}

export function cancelOrder(id) {
  return api.put(`/orders/${id}/cancel`)
}
