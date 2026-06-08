import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useCartStore = defineStore('cart', () => {
  const count = ref(0)

  function setCount(n) {
    count.value = n
  }

  function increment(n = 1) {
    count.value += n
  }

  return { count, setCount, increment }
})
