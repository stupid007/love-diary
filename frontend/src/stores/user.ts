//ref响应式变量，computed依赖别的变量算出来
import { ref, computed } from 'vue'
//defineStore:定义一个Pinia store的函数
import { defineStore } from 'pinia'
import { loginApi, loginout, type LoginResult } from '@/api/auth'
import { getToken, setToken, setUserInfo, getUserInfo, clearAuthStorage } from '@/utils/storage'

//用户信息的形状,和后端LoginResult对应的字段
export interface UserProfile {
    userId: number,
    nickName: string
}

export const useUserStore = defineStore('user', () => {
    const token = ref(getToken())
    const userInfo = ref<UserProfile | null>(getUserInfo<UserProfile>())
    // 派生状态（只读，自动根据上面两个变）
    //是否登录，根据token判断
    const isLoggedIn = computed(() => token.value)
    // nickName
    const nickName = computed(() => userInfo.value?.nickName ?? '')

    // 动作：登录
    // async因为要等网络请求回来
    async function login(username: string, password: string): Promise<UserProfile> {
        const res = await loginApi({ username, password })
        //1、存到localstorage（持久化）
        setToken(res.token)
        const profile: UserProfile = { userId: res.userId, nickName: res.nickName }
        setUserInfo(profile)

        // 同时更新内存状态
        token.value = res.token
        userInfo.value = profile

        return profile
    }

    //从已存的数据回复内存状态
    // 应用启动时调用一次，将localstorage的值同步进ref
    function setLoginState(): void {
        token.value = getToken()
        userInfo.value = getUserInfo<UserProfile>()
    }

    // ── 动作：退出登录 ────────────────────────────────
    // 先通知后端，再清本地；任何异常都不能把用户卡在登录态
    async function logout(): Promise<void> {
        try {
            // 调用后端接口；此时 token 还在 storage 里，拦截器能带上
            await loginout()
        } finally {
            // 清理 localStorage / sessionStorage 中的 token 和用户信息
            clearAuthStorage()
            // 清空内存 token，isLoggedIn 计算属性立即变为 false
            token.value = ''
            // 清空内存用户信息，顶栏昵称同步消失
            userInfo.value = null
        }
    }


    // setup store 必须 return 出去，组件里才能用到
    return { token, userInfo, isLoggedIn, nickName, login, setLoginState, logout }
})