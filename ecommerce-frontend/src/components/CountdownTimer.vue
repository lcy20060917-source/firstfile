<template>
  <div class="countdown-box">
    <div class="countdown-label">
      <i class="bi bi-clock-fill"></i> {{ title }}
    </div>
    <div class="countdown-digits">
      <span class="digit-block">
        <span class="digit">{{ display.hours }}</span>
        <span class="unit">时</span>
      </span>
      <span class="colon">:</span>
      <span class="digit-block">
        <span class="digit">{{ display.minutes }}</span>
        <span class="unit">分</span>
      </span>
      <span class="colon">:</span>
      <span class="digit-block">
        <span class="digit">{{ display.seconds }}</span>
        <span class="unit">秒</span>
      </span>
    </div>
    <div v-if="expired" class="expired-text">{{ expiredText }}</div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'

const props = defineProps({
  endTime: {
    type: [Number, String],
    required: true
  },
  title: { type: String, default: '限时优惠' },
  expiredText: { type: String, default: '活动已结束' }
})

const emit = defineEmits(['expired'])

const remaining = ref(0)
let timer = null

const display = computed(() => {
  const total = Math.max(0, Math.floor(remaining.value / 1000))
  return {
    hours:   String(Math.floor(total / 3600)).padStart(2, '0'),
    minutes: String(Math.floor((total % 3600) / 60)).padStart(2, '0'),
    seconds: String(total % 60).padStart(2, '0')
  }
})

const expired = computed(() => remaining.value <= 0)

function calcRemaining() {
  const end = typeof props.endTime === 'string'
    ? new Date(props.endTime).getTime()
    : props.endTime
  return end - Date.now()
}

function startTimer() {
  stopTimer()
  remaining.value = calcRemaining()
  if (remaining.value <= 0) {
    emit('expired')
    return
  }
  timer = setInterval(() => {
    remaining.value = calcRemaining()
    if (remaining.value <= 0) {
      stopTimer()
      emit('expired')
    }
  }, 1000)
}

function stopTimer() {
  if (timer) {
    clearInterval(timer)
    timer = null
  }
}

onMounted(startTimer)
onUnmounted(stopTimer)

watch(() => props.endTime, startTimer)
</script>

<style scoped>
.countdown-box {
  background: linear-gradient(135deg, #ff6b35, #e74c3c);
  border-radius: 8px;
  padding: 12px 16px;
  color: #fff;
  display: inline-block;
}
.countdown-label {
  font-size: 0.8rem;
  margin-bottom: 6px;
  opacity: 0.95;
}
.countdown-digits {
  display: flex;
  align-items: center;
  gap: 2px;
}
.digit-block {
  text-align: center;
}
.digit {
  background: rgba(0,0,0,0.3);
  border-radius: 4px;
  padding: 2px 6px;
  font-size: 1.2rem;
  font-weight: 700;
  font-family: 'Courier New', monospace;
  min-width: 28px;
  display: inline-block;
  text-align: center;
}
.unit {
  font-size: 0.7rem;
  display: block;
  opacity: 0.85;
}
.colon {
  font-size: 1.2rem;
  font-weight: 700;
  padding: 0 1px;
}
.expired-text {
  margin-top: 6px;
  font-size: 0.85rem;
  opacity: 0.9;
}
</style>
