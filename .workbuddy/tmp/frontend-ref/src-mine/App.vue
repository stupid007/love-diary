<script setup lang="ts">
import zhCn from 'element-plus/es/locale/lang/zh-cn'
</script>

<template>
  <!--
    ElConfigProvider 统一设置 Element Plus 的内置文案语言。
    不包这一层的话，分页器、日期选择器、确认框的按钮都会是英文。
  -->
  <el-config-provider :locale="zhCn">
    <router-view />
  </el-config-provider>
</template>
