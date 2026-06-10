import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

/**
 * 购物车全局状态 (Pinia)
 *
 * 管理: items(列表)、count(数量)、totalAmount(总价)
 * 操作: add、remove、update、clear、setFromServer
 */
export const useCartStore = defineStore('cart', () => {
  // ---- 状态 ----
  const items = ref([])          // 购物车项列表 { id, productId, productName, productPrice, quantity, subtotal }
  const totalCount = ref(0)      // 总件数
  const totalAmount = ref(0)     // 总金额

  // ---- 计算属性 ----
  const isEmpty = computed(() => items.value.length === 0)

  // ---- 操作方法 ----

  function reCalc() {
    let count = 0
    let amount = 0
    for (const item of items.value) {
      count += item.quantity || 0
      amount += (item.productPrice || 0) * (item.quantity || 0)
    }
    totalCount.value = count
    totalAmount.value = amount
  }

  function setFromServer(data) {
    items.value = data.items || []
    totalCount.value = data.totalCount || 0
    totalAmount.value = data.totalAmount || 0
  }

  function addItem(productId, productName, productPrice, quantity = 1) {
    const exist = items.value.find(i => i.productId === productId)
    if (exist) {
      exist.quantity += quantity
      exist.subtotal = exist.productPrice * exist.quantity
    } else {
      items.value.push({
        productId,
        productName,
        productPrice,
        quantity,
        subtotal: productPrice * quantity
      })
    }
    reCalc()
  }

  function removeItem(productId) {
    items.value = items.value.filter(i => i.productId !== productId)
    reCalc()
  }

  function updateQuantity(productId, quantity) {
    const item = items.value.find(i => i.productId === productId)
    if (item) {
      item.quantity = quantity
      item.subtotal = item.productPrice * quantity
      reCalc()
    }
  }

  function clear() {
    items.value = []
    totalCount.value = 0
    totalAmount.value = 0
  }

  return {
    items, totalCount, totalAmount, isEmpty,
    setFromServer, addItem, removeItem, updateQuantity, clear
  }
})
