import api from './index'

export function getProducts(params = {}) {
  return api.get('/products', { params })
}

export function getProductDetail(id) {
  return api.get(`/products/${id}`)
}
