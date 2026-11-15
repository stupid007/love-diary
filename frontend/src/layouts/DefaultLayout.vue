<script setup lang="ts">
// RouterView是路由出口，当前路由匹配的页面渲染页在这里
import { RouterView } from 'vue-router'
// 通知铃铛组件
import NotificationBell from '@/components/NotificationBell.vue';
// 通知store 和用户store
import { useUserStore } from '@/stores/user';
import { useNotifyStore } from '@/stores/notify';
// 监听登录态变化
import { watch } from 'vue';

const notify = useNotifyStore()
const user = useUserStore()
watch(() => user.isLoggedIn, (logged) => {
    if (logged) {
        notify.startPolling()
    } else {
        notify.stopPolling()
    }
}, {
    immediate: true
})
</script>
<template>
    <div class="layout">
        <!-- 顶部栏 -->
        <header class="topbar">
            <span class="brand">恋爱日记</span>
            <div class="top-right">
                <!-- 通知铃铛 -->
                <NotificationBell />
                <!-- 当前用户名 -->
                <span class="uname"> {{ user.userInfo?.nickName }}</span>
            </div>
        </header>
        <!-- 路由出口 -->
        <RouterView />
    </div>
</template>

<style scoped>
.layout {
    min-height: 100vh;
}

.topbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 20px;
    background: rgba(11, 16, 38, .6);
    border-bottom: 1px solid rgba(255, 213, 150, .2);
}

/* 品牌名 */
.brand {
    color: #ffd596;
    font-weight: 600;
    letter-spacing: 2px;
}

/* 右侧区 */
.top-right {
    display: flex;
    align-items: center;
    gap: 16px;
}

/* 用户名 */
.uname {
    color: #5ec8ff;
    font-size: 14px;
}
</style>