<script setup lang="ts">
// 语法：import { ref } from 'vue' ── 从 vue 导入具名 API ref
// 含义：ref 是「响应式引用」构造函数，包裹的值变了界面会自动刷新
// 作用：用它声明页面里会变化的数据（输入框内容、按钮 loading）
import { ref } from 'vue'

// 语法：import { useRouter } from 'vue-router' ── 导入路由钩子
// 含义：useRouter() 返回当前应用的路由实例
// 作用：在代码里主动跳转页面（如登录成功去首页）
import { useRouter } from 'vue-router'

// 语法：import { ElMessage } from 'element-plus' ── 导入函数式提示组件
// 含义：ElMessage 是 Element Plus 的全局轻提示（toast）
// 作用：登录成功/失败/校验不通过时弹一条消息
import { ElMessage } from 'element-plus'

// 语法：import { useUserStore } from '@/stores/user' ── 导入 Pinia store
// 含义：useUserStore 返回我们写的用户状态仓库
// 作用：调用里面的 login() 发请求并存 token
import { useUserStore } from '@/stores/user'

// 语法：const router = useRouter() ── 调用钩子拿到实例
// 含义/作用：后续用它做 router.push(路径) 跳转
const router = useRouter()
// 语法：const userStore = useUserStore() ── 调用拿到 store 实例
// 含义/作用：通过它访问 login 等方法与 isLoggedIn 等状态
const userStore = useUserStore()

// 语法：ref('') ── 创建初始值为空字符串的响应式变量
// 含义：username 会和输入框双向绑定
// 作用：收集用户输入，提交时取 username.value 发给后端
const username = ref('')
// 语法/含义/作用：同上，对应密码输入框
const password = ref('')
// 语法：ref(false) ── 布尔响应式变量
// 含义：标记请求是否进行中
// 作用：true 时按钮转圈且防重复点击
const loading = ref(false)

// 语法：async function onSubmit() ── 声明异步函数
// 含义：函数体内有 await（等网络请求）
// 作用：点登录时执行，负责校验 + 调接口 + 跳转
async function onSubmit() {
    // 语法：!username.value ── .value 取响应式变量的真实值；! 取反判空
    // 含义：用户名或密码为空
    // 作用：先校验，不合法直接提示并返回，不发请求
    if (!username.value || !password.value) {
        ElMessage.warning('请输入用户名和密码')
        return
    }
    // 作用：打开按钮 loading 态
    loading.value = true
    try {
        // 语法：await userStore.login(...) ── 等 Promise 完成
        // 含义：调用 store 的登录动作，内部已发请求并存好 token
        // 作用：拿到登录结果（失败会抛异常进入 catch）
        await userStore.login(username.value, password.value)
        ElMessage.success('登录成功')
        // 作用：跳转到首页（路由守卫会放行，因为已有 token）
        router.push('/')
    } catch (e) {
        // 语法：catch (e) ── 捕获上面抛出的错误
        // 含义：e 是错误对象
        // 作用：把后端返回的错误 message 弹出来
        // 语法：(e as Error) ── 类型断言，告诉 TS e 是 Error 类型才能取 .message
        ElMessage.error((e as Error).message || '登录失败')
    } finally {
        // 语法：finally ── try/catch 无论成败都会执行
        // 作用：关掉 loading，按钮恢复正常
        loading.value = false
    }
}
</script>

<template>
    <!-- 根容器：承载背景和卡片 -->
    <div class="auth-page">
        <!-- 装饰层 1：极光光斑（纯视觉，pointer-events:none 不挡点击） -->
        <div class="aurora"></div>
        <!-- 装饰层 2：星空点阵 -->
        <div class="stars"></div>

        <!-- 毛玻璃登录卡片 -->
        <div class="glass-card">
            <!-- 标题区 -->
            <div class="brand">
                <h1 class="title">恋爱日记 <span class="heart">♡</span></h1>
                <p class="subtitle">记录我们的每一个瞬间</p>
            </div>

            <!-- 语法：@submit.prevent="onSubmit" ── 监听表单提交并阻止默认刷新 -->
            <!-- 含义/作用：回车或点 type=submit 按钮时触发 onSubmit，且页面不刷新 -->
            <el-form class="auth-form" @submit.prevent="onSubmit">
                <el-form-item>
                    <!-- input-wrap 是自定义外壳：左边图标 + 右边 el-input -->
                    <div class="input-wrap">
                        <span class="ico">👤</span>
                        <!-- 语法：v-model="username" ── 双向绑定 -->
                        <!-- 含义/作用：输入框内容实时同步到 username 变量 -->
                        <el-input v-model="username" placeholder="用户名（3-20 位）" />
                    </div>
                </el-form-item>
                <el-form-item>
                    <div class="input-wrap">
                        <span class="ico">🔒</span>
                        <!-- type="password" 掩码；show-password 显示小眼睛 -->
                        <el-input v-model="password" type="password" show-password placeholder="密码（6-20 位）" />
                    </div>
                </el-form-item>
                <!-- 语法：:loading="loading" ── 动态绑定按钮 loading 态 -->
                <!-- native-type="submit" ── 让按钮触发表单的 submit 事件 -->
                <el-button type="primary" class="submit-btn" :loading="loading" native-type="submit">
                    登录
                </el-button>
            </el-form>

            <!-- 切换链接：去注册页 -->
            <div class="switch">
                还没有账号？<router-link to="/register">去注册</router-link>
            </div>
        </div>
    </div>
</template>

<style scoped>
/* .auth-page：页面根容器
   语法：position:relative + min-height:100vh + flex 居中
   作用：让卡片垂直水平居中，并作为绝对定位背景层的参照 */
.auth-page {
    position: relative;
    min-height: 100vh;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #070713;
    font-family: system-ui, -apple-system, "Microsoft YaHei", sans-serif;
}

/* .aurora：极光光斑层
   语法：radial-gradient(尺寸 at 位置, 颜色, transparent) 多个叠加；filter:blur 模糊；animation 动画
   作用：营造深空里漂浮的粉/紫/蓝辉光，纯装饰 */
.aurora {
    position: absolute;
    inset: -20%;
    background:
        radial-gradient(40% 50% at 30% 30%, rgba(236, 72, 153, .35), transparent 70%),
        radial-gradient(45% 55% at 70% 60%, rgba(99, 102, 241, .35), transparent 70%),
        radial-gradient(35% 45% at 50% 80%, rgba(56, 189, 248, .25), transparent 70%);
    filter: blur(60px);
    animation: auroraMove 18s ease-in-out infinite alternate;
    pointer-events: none;
}

/* @keyframes：定义动画关键帧；作用：让极光缓慢平移缩放 */
@keyframes auroraMove {
    0% {
        transform: translate(0, 0) scale(1)
    }

    100% {
        transform: translate(3%, -3%) scale(1.15)
    }
}

/* .stars：星空点阵层
   语法：多组 radial-gradient 小圆点 + opacity 动画
   作用：模拟星星闪烁 */
.stars {
    position: absolute;
    inset: 0;
    background-image:
        radial-gradient(1.5px 1.5px at 20% 30%, #fff, transparent),
        radial-gradient(1.5px 1.5px at 70% 20%, #fff, transparent),
        radial-gradient(1px 1px at 40% 70%, #fff, transparent),
        radial-gradient(1.5px 1.5px at 85% 65%, #fff, transparent),
        radial-gradient(1px 1px at 55% 45%, #fff, transparent),
        radial-gradient(1.5px 1.5px at 15% 80%, #fff, transparent);
    opacity: .8;
    animation: twinkle 4s ease-in-out infinite alternate;
    pointer-events: none;
}

@keyframes twinkle {
    from {
        opacity: .35
    }

    to {
        opacity: .9
    }
}

/* .glass-card：毛玻璃卡片
   语法：backdrop-filter:blur(20px) ── 背后内容模糊（玻璃感）；background 半透明；box-shadow 外发光
   作用：在暗背景上做一块半透明发光面板，承载表单 */
.glass-card {
    position: relative;
    z-index: 1;
    width: 380px;
    padding: 32px 28px;
    border-radius: 20px;
    background: rgba(255, 255, 255, .06);
    border: 1px solid rgba(255, 255, 255, .12);
    backdrop-filter: blur(20px);
    -webkit-backdrop-filter: blur(20px);
    box-shadow: 0 0 40px rgba(168, 85, 247, .25), inset 0 0 20px rgba(255, 255, 255, .04);
}

/* .brand / .title / .heart：标题区 */
.brand {
    text-align: center;
    margin-bottom: 8px;
}

.title {
    margin: 0;
    font-size: 26px;
    color: #fff;
    letter-spacing: 2px;
}

.heart {
    color: #ec4899;
}

.subtitle {
    margin: 6px 0 0;
    font-size: 13px;
    color: rgba(255, 255, 255, .55);
}

/* .auth-form：表单上间距 */
.auth-form {
    margin-top: 12px;
}

/* .input-wrap：输入框外壳
   语法：flex 横排；background 半透明；border 默认淡边框；:focus-within 聚焦时变色发光
   作用：把图标和输入框包在一起，做出暗色发光输入框 */
.input-wrap {
    display: flex;
    align-items: center;
    width: 100%;
    background: rgba(255, 255, 255, .05);
    border: 1px solid rgba(255, 255, 255, .12);
    border-radius: 10px;
    padding: 0 12px;
    transition: border-color .2s, box-shadow .2s;
}

.input-wrap:focus-within {
    border-color: #ec4899;
    box-shadow: 0 0 0 3px rgba(236, 72, 153, .25);
}

.ico {
    margin-right: 8px;
    font-size: 15px;
}

/* :deep()：穿透 scoped 样式，改 Element Plus 内部元素 */
.input-wrap :deep(.el-input__wrapper) {
    background: transparent;
    box-shadow: none;
    padding: 0;
}

.input-wrap :deep(.el-input__inner) {
    color: #fff;
}

.input-wrap :deep(.el-input__inner::placeholder) {
    color: rgba(255, 255, 255, .4);
}

/* .submit-btn：渐变主按钮
   语法：linear-gradient 粉→紫→蓝；background-size:200% 让渐变可平移；:hover 移背景位置做流光
   作用：醒目 CTA，hover 有流光动效 */
.submit-btn {
    width: 100%;
    height: 44px;
    border: none;
    border-radius: 10px;
    margin-top: 4px;
    font-size: 15px;
    font-weight: 600;
    color: #fff;
    background: linear-gradient(90deg, #ec4899, #8b5cf6, #38bdf8);
    background-size: 200% 100%;
    box-shadow: 0 6px 20px rgba(139, 92, 246, .45);
    transition: background-position .4s, transform .1s;
}

.submit-btn:hover {
    background-position: 100% 0;
}

.submit-btn:active {
    transform: scale(.98);
}

/* .switch：底部切换链接 */
.switch {
    margin-top: 14px;
    text-align: center;
    font-size: 13px;
    color: rgba(255, 255, 255, .6);
}

.switch :deep(a) {
    color: #ec4899;
    text-decoration: none;
}
/* 覆盖 Chrome 自动填充的白底样式 */
.input-wrap :deep(.el-input__inner:-webkit-autofill) {
    /* 用内阴影画一层和主题一致的深色背景，顶掉浏览器的白色 */
    -webkit-box-shadow: 0 0 0 1000px #14141f inset;
    /* 强制文字为白色（autofill 会把文字染黑） */
    -webkit-text-fill-color: #fff;
    caret-color: #fff;
    transition: background-color 99999s ease-out;
}
</style>
