import { get } from '../utils/request'
import { mockGetProducts, mockGetProduct } from '../mock/products'

export function getProducts(params = {}) {
  return get('/products', params, mockGetProducts)
}

export function getProductDetail(id) {
  return get(`/products/${id}`, {}, (p) => mockGetProduct(id))
}
