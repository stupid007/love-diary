<script setup lang="ts">
// 路由出口:匹配到的页面（DefaultLayout及其子页面都会渲染到这里）
import { RouterView } from 'vue-router'
</script>
<template>
  <RouterView></RouterView>
</template>
<style>
/* 全局基础样式 */
html,
body,
#app {
  height: 100%;
  margin: 0;
}

body {
  font-family: system-ui, -apple-system, "Microsoft YaHei", sans-serif;
   background: #070713; 
  color: #303133;
}
</style>
