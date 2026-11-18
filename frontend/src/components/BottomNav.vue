<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { Document, Picture, Clock, Calendar, Sunny, User, Link} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

// 底部六个导航页
const navs = [
    {
        label: '日记',
        path: '/diary',
        icon: Document
    },
    {
        label: '相册',
        path: '/album',
        icon: Picture
    },
    {
        label: '时光机',
        path: '/timeline',
        icon: Clock
    },
    {
        label: '纪念日',
        path: '/anniversary',
        icon: Calendar
    },
    {
        label: '心情',
        path: 'mood',
        icon: Sunny
    },
    {
        label: '我',
        path: '/settings',
        icon: User
    },
        // 情侣关系入口（排在"我"之前）
    {
        label: '情侣关系绑定',
        path: '/couple',
        icon: Link
    },
]
</script>
<template>
    <div class="bottom-nav">
        <div v-for="n in navs" :key="n.path" class="nav-item" :class="{ active: route.path === n.path }"
            @click="router.push(n.path)">
            <el-icon class="nav-icon">
                <component :is="n.icon" />
            </el-icon>
            <span>{{ n.label }}</span>
        </div>
    </div>
</template>

<style scoped>
.bottom-nav {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    height: 64px;
    display: flex;
    justify-content: space-around;
    align-items: center;
    background: rgba(5, 10, 20, 0.85);
    border-top: 1px solid rgba(127, 212, 255, 0.3);
    backdrop-filter: blur(10px);
    z-index: 50;
}

.nav-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 3px;
    color: #8c9db5;
    font-size: 12px;
    cursor: pointer;
}

.nav-icon {
    width: 22px;
    height: 22px;
}

.nav-item.active,
.nav-item:hover {
    color: #39f0ff
}
</style>