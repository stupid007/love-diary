// 响应式与 Pinia
import { ref } from 'vue'
import { defineStore } from 'pinia'
// 通知接口
import { listNotifyApi, readNotifyApi, readAllNotifyApi, type NotificationVO } from '@/api/notify'

// 定义通知 store
export const useNotifyStore = defineStore('notify', () => {
    // 通知列表
    const list = ref<NotificationVO[]>([])
    // 未读数（驱动铃铛红点）
    const unread = ref(0)
    // 轮询定时器
    let timer: number | null = null

    // 拉取列表并同步未读
    async function load() {
        const res = await listNotifyApi()
        list.value = res.list
        unread.value = res.unread
    }
    // 单条已读，本地同步未读计数
    async function read(id: number) {
        await readNotifyApi(id)
        const item = list.value.find(n => n.id === id)
        if (item) item.isRead = true
        unread.value = Math.max(0, unread.value - 1)
    }
    // 全部已读，未读清零
    async function readAll() {
        await readAllNotifyApi()
        list.value.forEach(n => (n.isRead = true))
        unread.value = 0
    }
    // 启动全局轮询（登录后由 DefaultLayout 调，5 秒一次）
    function startPolling() {
        if (timer) return
        load()
        timer = window.setInterval(() => load(), 5000)
    }
    // 停止轮询（退出登录时）
    function stopPolling() {
        if (timer) {
            clearInterval(timer)
            timer = null
        }
    }

    // 暴露给组件
    return { list, unread, load, read, readAll, startPolling, stopPolling }
})
