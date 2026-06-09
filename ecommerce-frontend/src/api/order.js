import { get, post, put } from '../utils/request'

export function createOrder(data) {
  return post('/orders', data)
}

export function getOrders(params = {}) {
  return get('/orders', params)
}

export function getOrderDetail(id) {
  return get(`/orders/${id}`)
}

export function cancelOrder(id) {
  return put(`/orders/${id}/cancel`)
}
