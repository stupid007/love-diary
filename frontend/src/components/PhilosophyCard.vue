<script setup lang="ts">
// 声明关闭事件（态D 为页面卡片，这里保留关闭能力供复用）
const emit = defineEmits<{ close: [] }>()
</script>

<template>
  <!-- 暗调金边哲学卡片 -->
  <div class="philo-card">
    <!-- 主句（暖金） -->
    <h2 class="philo-title">你的机会已经没有了，<br>你没有好好珍惜那个她（他）。</h2>
    <!-- 哲学句（灰白） -->
    <p class="philo-text">
      有些门一旦关上，便不再为谁而开；<br>
      不是惩罚，是成长的代价。
    </p>
    <!-- 确认按钮 -->
    <button class="philo-btn" @click="emit('close')">我 知 道 了</button>
  </div>
</template>

<style scoped>
/* 暗调卡片 */
.philo-card {
  position: relative;
  z-index: 1;
  max-width: 520px;
  margin: 0 auto;
  text-align: center;
  background: rgba(20, 16, 30, .5);
  border: 1px solid rgba(255, 213, 150, .35);
  border-radius: 18px;
  padding: 40px 32px;
  box-shadow: 0 0 30px rgba(0, 0, 0, .4);
}

/* 主句 */
.philo-title {
  color: #ffd596;
  line-height: 1.8;
  margin: 0 0 18px;
}

/* 哲学句 */
.philo-text {
  color: rgba(230, 230, 240, .7);
  line-height: 2;
  margin: 0 0 28px;
}

/* 按钮 */
.philo-btn {
  padding: 10px 26px;
  border-radius: 10px;
  cursor: pointer;
  background: transparent;
  border: 1px solid rgba(255, 213, 150, .5);
  color: #ffd596;
}
</style>
