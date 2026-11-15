//ref响应式变量，computed依赖别的变量算出来
import {ref,computed} from 'vue'
//defineStore:定义一个Pinia store的函数
import {defineStore} from 'pinia'
import { loginApi,type LoginResult } from '@/api/auth'
import {getToken,setToken,setUserInfo,getUserInfo,clearAuthStorage} from '@/utils/storage'

//用户信息的形状,和后端LoginResult对应的字段
export interface UserProfile {
    userId:number,
    nickName:string
}

export const useUserStore = defineStore('user',() => {
    const token = ref(getToken())
    const userInfo = ref<UserProfile | null>(getUserInfo<UserProfile>())
    // 派生状态（只读，自动根据上面两个变）
    //是否登录，根据token判断
    const isLoggedIn = computed(()=> token.value)
    // nickName
    const nickName = computed(() => userInfo.value?.nickName?? '')
    
    // 动作：登录
    // async因为要等网络请求回来
    async function login(username: string,password: string): Promise<UserProfile> {
        const res = await loginApi({username,password}) 
        //1、存到localstorage（持久化）
        setToken(res.token)
        const profile: UserProfile = { userId:res.userId , nickName: res.nickName}
        setUserInfo(profile)

        // 同时更新内存状态
        token.value = res.token
        userInfo.value = profile

        return profile
    }

    //从已存的数据回复内存状态
    // 应用启动时调用一次，将localstorage的值同步进ref
    function setLoginState():void {
        token.value = getToken()
        userInfo.value = getUserInfo<UserProfile>()
    }

    // ── 动作：退出登录 ────────────────────────────────
  // 只清状态和存储，跳转登录页交给调用方（避免 store 里 import router 造成循环依赖）
  function logout(): void {
    clearAuthStorage()
    token.value = ''
    userInfo.value = null
  }

  // setup store 必须 return 出去，组件里才能用到
  return { token, userInfo, isLoggedIn, nickName, login, setLoginState, logout }
})