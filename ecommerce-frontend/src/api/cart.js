import { get, post, put, del } from '../utils/request'

export function getCart() {
  return get('/cart')
}

export function addToCart(productId, quantity = 1) {
  return post('/cart/add', { productId, quantity })
}

export function updateCartQuantity(productId, quantity) {
  return put('/cart/update', { productId, quantity })
}

export function removeFromCart(productId) {
  return del(`/cart/remove/${productId}`)
}

export function clearCart() {
  return del('/cart/clear')
}
