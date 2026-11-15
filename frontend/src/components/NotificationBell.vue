<script setup lang="ts">
import { ref } from 'vue'
import { useNotifyStore } from '@/stores/notify';
// 取store实例（轮询已在DefaultLayout启动）
const notify = useNotifyStore()
// 下拉状态
const open = ref(false)
// 点击铃铛切换
function toggle() {
    open.value = !open.value
}
// 点击遮罩关闭
function close() {
    open.value = false
}
</script>

<template>
    <!-- 铃铛容器 -->
    <div class="bell-wrap">
        <!-- 铃铛按钮 -->
        <button class="bell-btn" @click="toggle">
            <!-- 铃铛图标（金） -->
            <svg viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="#ffd596" stroke-width="1.6">
                <path d="M18 8a6 6 0 1 0-12 0c0 7-3 9-3 9h18s-3-2-3-9" />
                <path d="M13.7 21a2 2 0 0 1-3.4 0" />
            </svg>
            <!-- 未读红点 -->、
            <span v-if="notify.unread > 0" class="badge"> {{ notify.unread }} </span>
        </button>
    </div>
    <!-- 下拉面板 -->
    <div v-if="open" class="panel">
        <!-- 面板标题 -->
        <div class="panel-list">
            <div class="msg" v-for="n in notify.list" :key="n.id" :class="{ unread: !n.isRead }">
                <div class="msg-title">{{ n.title }}</div>
                <div class="msg-content">{{ n.content }}</div>
                <button v-if="!n.isRead" class="msg-read" @click="notify.read(n.id)">标为已读</button>
            </div>
            <!-- 空态 -->
            <div v-if="notify.list.length === 0" class="empty">暂无通知</div>
        </div>
        <!-- 全部已读 -->
        <button class="read-all" @click="notify.readAll()">全 部 已 读</button>
    </div>

    <!-- 点击空白关闭 -->
    <div v-if="open" class="backdrop" @click="close"></div>
</template>

<style scoped>
/* 容器定位 */
.bell-wrap {
    position: relative;
}

/* 铃铛按钮 */
.bell-btn {
    position: relative;
    background: transparent;
    border: none;
    cursor: pointer;
    padding: 4px;
}

/* 红点 */
.badge {
    position: absolute;
    top: -4px;
    right: -6px;
    background: #ff7a8a;
    color: #fff;
    font-size: 11px;
    min-width: 16px;
    height: 16px;
    line-height: 16px;
    border-radius: 8px;
    padding: 0 4px;
}

/* 面板 */
.panel {
    position: absolute;
    right: 0;
    top: 38px;
    z-index: 1001;
    width: 300px;
    max-height: 360px;
    overflow: auto;
    background: rgba(15, 20, 40, .95);
    border: 1px solid rgba(255, 213, 150, .4);
    border-radius: 14px;
    padding: 12px;
    backdrop-filter: blur(10px);
    box-shadow: 0 10px 40px rgba(0, 0, 0, .5);
}

/* 面板标题 */
.panel-head {
    color: #ffd596;
    font-weight: 600;
    padding: 4px 6px 10px;
    border-bottom: 1px solid rgba(255, 255, 255, .08);
}

/* 列表 */
.panel-list {
    margin-top: 8px;
}

/* 单条消息 */
.msg {
    padding: 10px;
    border-radius: 10px;
    margin-bottom: 8px;
    background: rgba(255, 255, 255, .04);
    border: 1px solid transparent;
}

/* 未读青蓝描边 */
.msg.unread {
    border-color: rgba(94, 200, 255, .6);
}

/* 标题 */
.msg-title {
    color: #eaeaf2;
    font-size: 13px;
    margin-bottom: 4px;
}

/* 正文 */
.msg-content {
    color: rgba(230, 230, 240, .6);
    font-size: 12px;
}

/* 标为已读 */
.msg-read {
    margin-top: 6px;
    background: transparent;
    border: 1px solid rgba(94, 200, 255, .5);
    color: #5ec8ff;
    font-size: 11px;
    border-radius: 6px;
    padding: 2px 8px;
    cursor: pointer;
}

/* 全部已读 */
.read-all {
    width: 100%;
    margin-top: 6px;
    padding: 8px;
    border-radius: 8px;
    cursor: pointer;
    background: linear-gradient(135deg, #ff7a8a, #ff9aa8);
    color: #fff;
    border: none;
}

/* 空态 */
.empty {
    color: rgba(230, 230, 240, .4);
    text-align: center;
    padding: 20px 0;
    font-size: 13px;
}

/* 遮罩 */
.backdrop {
    position: fixed;
    inset: 0;
    z-index: 1000;
}
</style>
