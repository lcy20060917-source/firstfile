import api from './index'

export function getCart() {
  return api.get('/cart')
}

export function addToCart(productId, quantity = 1) {
  return api.post('/cart/add', { productId, quantity })
}

export function updateCartQuantity(productId, quantity) {
  return api.put('/cart/update', { productId, quantity })
}

export function removeFromCart(productId) {
  return api.delete(`/cart/remove/${productId}`)
}

export function clearCart() {
  return api.delete('/cart/clear')
}
