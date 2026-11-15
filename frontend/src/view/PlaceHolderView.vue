<script setup lang='ts'>
import { useRoute } from 'vue-router'
import BottomNav from "@/components/BottomNav.vue"

const route = useRoute()
</script>

<template>
    <div class='ph'>
        <h2>{{ route.meta.title }}</h2>
        <p>页面建设中...</p>
        <BottomNav></BottomNav>
    </div>
</template>

<style scoped>
.ph {
    min-height: 100vh;
    padding: 80px 24px 90px;
    background: #070713;
    color:#fff;
}
</style>