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
// 路由实例：登出后跳转登录页
import { useRouter } from 'vue-router'
// 轻提示：给用户一个退出成功的反馈
import { ElMessage } from 'element-plus'

// 拿到路由实例
const router = useRouter()
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
// 退出登录处理：store 负责请求后端和清本地，这里负责跳转
async function onLogout() {
    // 调用 store 中已改好的 logout()
    await user.logout()
    // 提示用户已退出
    ElMessage.success('已安全退出')
    // 用 replace 跳登录页，避免浏览器后退回到登录态页面
    await router.replace('/login')
}
</script>
<template>
    <div class="layout">
        <!-- 顶部栏 -->
        <header class="topbar">
            <span class="brand">恋爱日记 祝天下有情人终成眷属</span>
            <!-- 顶部右侧操作区 -->
            <div class="top-right">
                <!-- 通知铃铛组件 -->
                <NotificationBell />
                <!-- 当前登录用户昵称 -->
                <span class="uname"> {{ user.userInfo?.nickName }}</span>
                <!-- 退出登录按钮：首页和所有套用本布局的页面都会显示 -->
                <el-button link type="danger" size="small" @click="onLogout">退出登录</el-button>
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