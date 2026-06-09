<template>
  <span class="promo-tag" :class="tagClass" :style="{ backgroundColor: bgColor }">
    <i :class="iconClass"></i>
    {{ label }}
  </span>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  type: {
    type: String,
    default: 'discount',
    validator: (v) => ['discount', 'flash_sale', 'new_arrival', 'limited', 'free_shipping'].includes(v)
  },
  label: { type: String, default: '促销' },
  color: { type: String, default: '' }
})

const tagConfig = {
  discount:       { cls: 'tag-discount',       icon: 'bi-tag-fill',       color: '#e74c3c' },
  flash_sale:     { cls: 'tag-flash',          icon: 'bi-lightning-fill', color: '#ff6b35' },
  new_arrival:    { cls: 'tag-new',            icon: 'bi-star-fill',      color: '#2ecc71' },
  limited:        { cls: 'tag-limited',        icon: 'bi-clock-fill',     color: '#9b59b6' },
  free_shipping:  { cls: 'tag-shipping',       icon: 'bi-truck',          color: '#3498db' }
}

const config = computed(() => tagConfig[props.type] || tagConfig.discount)
const tagClass = computed(() => 'promo-tag-' + props.type)
const bgColor = computed(() => props.color || config.value.color)
const iconClass = computed(() => config.value.icon)
</script>

<style scoped>
.promo-tag {
  display: inline-block;
  padding: 2px 10px;
  border-radius: 3px;
  font-size: 0.75rem;
  font-weight: 600;
  color: #fff;
  white-space: nowrap;
  animation: tagPulse 2s ease-in-out infinite;
}
.promo-tag + .promo-tag {
  margin-left: 4px;
}
@keyframes tagPulse {
  0%, 100% { opacity: 1; }
  50%      { opacity: 0.85; }
}
</style>
