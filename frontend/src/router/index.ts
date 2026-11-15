//createRouter:创建路由实例；createWebHistory：用浏览器history模式(URL不带#)
import { createRouter, createWebHistory } from "vue-router"
// 路由配置的类型
import type { RouteRecordRaw } from "vue-router"

import DefaultLayout from "@/layouts/DefaultLayout.vue"
import LoginView from "@/view/auth/LoginView.vue"
import RegisterView from "@/view/auth/RegisterView.vue"
import GlobeView from "@/view/GlobeView.vue"
import { useUserStore } from "@/stores/user"
import { resolveDirective } from "vue"
import { loginApi } from "@/api/auth"
import SettingsView from "@/view/SettingsView.vue"
import PlaceHolderView from "@/view/PlaceHolderView.vue"
import CoupleView from "@/components/CoupleView.vue"

//扩展RouteMeta类型
//路由的meta默认是空的,加上自定义的三个字段的类型，写to.meta.xxx才不报错
declare module 'vue-router' {
    interface RouteMeta {
        //页面标题
        title?: string
        //需要登录才可以访问
        requireAuth?: boolean
        //只有未登录才可以访问
        guestOnly?: boolean
    }
}

// 路由表，所有页面都套在DefaultLayout里（它有RouterView出口）
const routes: RouteRecordRaw[] = [{
    path: '/',
    component: DefaultLayout,
    children: [
        // 空path = 父路径本身，即访问‘/’时显示
        {
            path: '',
            name: 'home',
            component: GlobeView,
            meta: { requireAuth: true, title: '首页' }
        },
        {
            path: 'settings',
            name: 'settings',
            component: SettingsView,
            meta: { requireAuth: true, title: '位置设置' }
        },
        {
            path: 'diary',
            name: 'diary',
            component: PlaceHolderView,
            meta: { requireAuth: true, title: '日记' }
        },
        {
            path: 'album',
            name: 'album',
            component: PlaceHolderView,
            meta: { requireAuth: true, title: '相册' }
        },
        {
            path: 'timeline',
            name: 'timeline',
            component: PlaceHolderView,
            meta: { requireAuth: true, title: '时光机' }
        },
        {
            path: 'anniversary',
            name: 'anniversary',
            component: PlaceHolderView,
            meta: { requireAuth: true, title: '纪念日' }
        },
        {
            path: 'mood',
            name: 'mood',
            component: PlaceHolderView,
            meta: { requireAuth: true, title: '心情' }
        },
        {
            path: 'login',
            name: 'login',
            component: LoginView,
            meta: { guestOnly: true, title: '登录' }
        },
        {
            path: 'register',
            name: 'register',
            component: RegisterView,
            meta: { guestOnly: true, title: '注册' },
        },
        {
            path: 'couple',
            name: 'couple',
            component: CoupleView,
            meta: { requireAuth: true, title: '情侣' }
        },
    ],
},
// 兜底:任何没匹配的路径都跳回首页（首页守卫会决定去哪）
{
    path: '/:pathMatch(.*)*',
    redirect: '/'
},
]
/* createRouter	创建 Vue Router 4 路由器实例
对象参数	配置这个路由器
history: createWebHistory()	使用 HTML5 History 模式，URL 不带 #
routes	传入路由表，决定路径和组件的映射关系
const router	得到路由器实例，用于 app.use(router)、router.beforeEach、router.push 等 */
const router = createRouter({
    history: createWebHistory(),
    routes,
})

/* 全局前置守卫:每次跳转前先判断权限
返回true =放行；返回{name}=改去别处，返回false = 取消
router.beforeEach	全局前置路由守卫，每次跳转前执行
to	即将进入的目标路由对象
to.meta	路由配置中的 meta 信息
to.fullPath	目标完整路径，含 query 和 hash
return true	放行，允许导航
return false	取消导航
return { name: 'login', query: {...} }	重定向到登录页
return { name: 'home' }	重定向到首页
useUserStore()	在守卫内部获取 Pinia store，保证 Pinia 已安装且状态最新 */
router.beforeEach((to) => {
    // 必须在守卫内部取出store，此时pinia已装配，能拿到最新状态
    const userStore = useUserStore()
    const loggedIn = userStore.isLoggedIn
    // 需要登录却没登录->跳登录页,带上redirect（登录后回跳原页面）
    if (to.meta.requireAuth && !loggedIn) {
        return {
            name: 'login',
            query: {
                redirect: to.fullPath
            }
        }
    }
    // 已经登录却还想去登录/注册页面->直接进首页
    if (to.meta.guestOnly && loggedIn) {
        return {
            name: 'home'
        }
    }

    // 其他情况
    return true
})

/* 后置钩子，切换后设置浏览器标题
router.afterEach	注册全局后置守卫，每次导航完成后执行
to	目标路由对象
to.meta.title	读取路由配置中的标题
document.title	设置浏览器标签页标题
三元表达式	有标题就拼上“-恋爱日记”，没有就只显示“恋爱日记” */
router.afterEach((to) => {
    const title = to.meta.title
    document.title = title ? `${title}-恋爱日记` : `恋爱日记`
})

export default router