<script setup lang="ts">
// 同 LoginView：导入响应式 API、路由、提示组件、注册接口
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
// 语法：import { registerApi } from '@/api/auth' ── 只导入注册接口函数
// 含义/作用：注册页用不到 userStore.login，直接调 registerApi 发请求
import { registerApi } from '@/api/auth'

const router = useRouter()

// 三个响应式字段：用户名、密码、昵称（昵称可选）
const username = ref('')
const password = ref('')
const nickname = ref('')
const loading = ref(false)

async function onSubmit() {
  // 同样先校验非空
  if (!username.value || !password.value) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    // 语法：await registerApi({ username, password, nickName })
    // 含义：把三个字段作为请求体发给后端 POST /auth/register
    // 作用：nickName 为空时传 undefined，后端收 null（昵称可选）
    await registerApi({
      username: username.value,
      password: password.value,
      nickName: nickname.value || undefined,
    })
    ElMessage.success('注册成功，请登录')
    // 作用：注册完跳登录页，让用户再登录一次（后端注册不返回 token）
    router.push('/login')
  } catch (e) {
    ElMessage.error((e as Error).message || '注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="auth-page">
    <div class="aurora"></div>
    <div class="stars"></div>

    <div class="glass-card">
      <div class="brand">
        <h1 class="title">创建账号 <span class="heart">♡</span></h1>
        <p class="subtitle">开启你们的恋爱日记</p>
      </div>

      <!-- @submit.prevent 阻止刷新并触发 onSubmit -->
      <el-form class="auth-form" @submit.prevent="onSubmit">
        <el-form-item>
          <div class="input-wrap">
            <span class="ico">👤</span>
            <el-input v-model="username" placeholder="用户名（3-20 位）" />
          </div>
        </el-form-item>
        <el-form-item>
          <div class="input-wrap">
            <span class="ico">🔒</span>
            <el-input v-model="password" type="password" show-password placeholder="密码（6-20 位）" />
          </div>
        </el-form-item>
        <el-form-item>
          <div class="input-wrap">
            <span class="ico">💌</span>
            <el-input v-model="nickname" placeholder="昵称（可选）" />
          </div>
        </el-form-item>
        <el-button type="primary" class="submit-btn" :loading="loading" native-type="submit">
          注册
        </el-button>
      </el-form>

      <div class="switch">
        已有账号？<router-link to="/login">去登录</router-link>
      </div>
    </div>
  </div>
</template>

<!-- 复制 LoginView 的 <style scoped> 整段粘贴到这里 -->
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
</style>
