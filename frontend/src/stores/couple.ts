// 响应式与 Pinia
import { ref } from 'vue'
import { defineStore } from 'pinia'
// 关系接口
import { inviteApi, bindApi, confirmApi, getStateApi, unbindApi, type CoupleStateVO } from '@/api/couple'

// 定义关系 store
export const useCoupleStore = defineStore('couple', () => {
    // 四态数据（everBound + couple）
    const state = ref<CoupleStateVO | null>(null)
    // A 端生成的邀请码，邀请后本地保存展示
    const inviteCode = ref('')
    // B 端是否已提交申请
    const applied = ref(false)
    // 关系轮询定时器句柄
    let stateTimer: number | null = null

    // 拉取四态
    async function loadState() {
        state.value = await getStateApi()
    }
    // 生成邀请码并刷新四态
    async function invite() {
        const res = await inviteApi()
        inviteCode.value = res.code
        await loadState()
    }
    // 提交邀请码，标记为已申请
    async function bind(code: string) {
        await bindApi({ code })
        applied.value = true
        await loadState()
    }
    // 确认绑定
    async function confirm(coupleId: number) {
        await confirmApi(coupleId)
        await loadState()
    }
    // 解绑并重置本地态
    async function unbind() {
        await unbindApi()
        inviteCode.value = ''
        applied.value = false
        await loadState()
    }
    // 启动关系轮询（仅"进行中"态用，3 秒一次）
    function startStatePolling() {
        if (stateTimer) return
        stateTimer = window.setInterval(() => loadState(), 3000)
    }
    // 停止关系轮询
    function stopStatePolling() {
        if (stateTimer) {
            clearInterval(stateTimer)
            stateTimer = null
        }
    }

    // 暴露给组件
    return { state, inviteCode, applied, loadState, invite, bind, confirm, unbind, startStatePolling, stopStatePolling }
})
