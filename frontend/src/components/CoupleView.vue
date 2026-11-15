<script setup lang="ts">
// 响应式 API
import { ref, computed, onMounted, onUnmounted } from 'vue'
// 关系 store 与用户 store
import { useCoupleStore } from '@/stores/couple'
import { useUserStore } from '@/stores/user'
// 子组件
import VowConfirm from '@/components/VowConfirm.vue'
import PhilosophyCard from '@/components/PhilosophyCard.vue'

// store 实例
const couple = useCoupleStore()
const user = useUserStore()

// 当前用户 ID（用于判断是否邀请方）
const myId = computed(() => user.userInfo?.userId ?? -1)
// 当前关系
const rel = computed(() => couple.state?.couple ?? null)
// 一生一次是否已锁
const everBound = computed(() => couple.state?.everBound ?? false)
// 是否邀请方（A）
const isInviter = computed(() => rel.value?.userAId === myId.value)

// 四态判定
const view = computed(() => {
  if (rel.value && rel.value.status === 1) return 'bound'    // 态C 已绑定
  if (rel.value && rel.value.status === 0) return 'pending'  // 态B 待确认
  if (couple.inviteCode) return 'invited'                    // A 已发码等 B
  if (couple.applied) return 'applied'                       // B 已申请等 A
  if (everBound.value) return 'gone'                         // 态D 机会已尽
  return 'fresh'                                             // 态A 新用户
})

// 在一起天数（从纪念日算）
const days = computed(() => {
  if (!rel.value?.anniversary) return 0
  const start = new Date(rel.value.anniversary).getTime()
  return Math.max(0, Math.floor((Date.now() - start) / 86400000))
})

// 誓言弹窗与解绑弹窗显隐
const showVow = ref(false)
const showUnbind = ref(false)
// 输入邀请码
const inputCode = ref('')

// 记住誓言弹窗确认后要执行哪种动作
const vowAction = ref<'invite' | 'bind'>('invite')

// 点"生成我的邀请码"：标记 invite 并弹窗
function onInvite() {
  vowAction.value = 'invite'
  showVow.value = true
}

// 点"缔结"：标记 bind 并弹窗
function onBind() {
  vowAction.value = 'bind'
  showVow.value = true
}

// 誓言弹窗确认：按意图分流（关键修复点）
function onVowConfirm() {
  if (vowAction.value === 'bind') {
    couple.bind(inputCode.value.trim())
  } else {
    couple.invite()
  }
}

// 邀请方确认羁绊
function onConfirm() { if (rel.value) couple.confirm(rel.value.id) }
// 复制邀请码
function copyCode() { navigator.clipboard?.writeText(couple.inviteCode) }
// 打开解绑确认
function onUnbind() { showUnbind.value = true }
// 执行解绑
function doUnbind() { couple.unbind(); showUnbind.value = false }
// 进入拉状态 + 启动关系轮询
onMounted(() => { couple.loadState(); couple.startStatePolling() })
// 离开停止轮询
onUnmounted(() => couple.stopStatePolling())
</script>

<template>
  <!-- 神圣婚礼背景根 -->
  <div class="holy">
    <!-- 上浮光斑 ×5 -->
    <span class="holy-particle" style="left:10%; animation-delay:0s"></span>
    <span class="holy-particle" style="left:32%; animation-delay:2s"></span>
    <span class="holy-particle" style="left:55%; animation-delay:4s"></span>
    <span class="holy-particle" style="left:75%; animation-delay:1s"></span>
    <span class="holy-particle" style="left:90%; animation-delay:3s"></span>

    <!-- 态A 新用户 -->
    <div v-if="view === 'fresh'" class="glass-card">
      <h1 class="title">缔结属于你们的契约</h1>
      <p class="sub">两个灵魂若注定相遇，便不必问归期。</p>
      <button class="btn-coral" @click="onInvite">生成我的邀请码</button>
      <div class="bind-row">
        <input v-model="inputCode" class="code-input" placeholder="粘贴 TA 的邀请码" />
        <button class="btn-cyan" @click="onBind">缔 结</button>
      </div>
    </div>

    <!-- 态B 待确认 / 已发码 / 已申请 -->
    <div v-else-if="view === 'invited' || view === 'applied' || view === 'pending'" class="glass-card">
      <template v-if="view === 'invited'">
        <h1 class="title">将这串星芒交予那个 TA</h1>
        <p class="code-show">{{ couple.inviteCode }}</p>
        <button class="btn-ghost" @click="copyCode">复 制</button>
        <p class="sub">静候回应…</p>
      </template>
      <template v-else-if="view === 'applied'">
        <h1 class="title">已向 TA 发起羁绊</h1>
        <p class="sub">静候对方确认，契约即成。</p>
      </template>
      <template v-else>
        <h1 class="title">羁绊待确认</h1>
        <p class="sub">{{ isInviter ? '请确认这份契约' : '已申请，等待对方确认' }}</p>
        <button v-if="isInviter" class="btn-coral" @click="onConfirm">确认羁绊</button>
      </template>
    </div>

    <!-- 态C 已绑定 -->
    <div v-else-if="view === 'bound'" class="glass-card">
      <h1 class="title gold">{{ rel?.nicknameA }} ❤ {{ rel?.nicknameB }}</h1>
      <p class="anniversary">在一起的第 {{ days }} 天 · {{ rel?.anniversary }}</p>
      <div class="vow-box">
        <p>我愿以余生为笺，写满与你有关的晨昏。</p>
        <p>无论顺逆，皆不舍不弃。</p>
      </div>
      <button class="btn-danger" @click="onUnbind">解 除 羁 绊</button>
    </div>

    <!-- 态D 机会已尽 -->
    <div v-else class="philo-wrap">
      <PhilosophyCard />
    </div>

    <!-- 誓言弹窗 -->
    <VowConfirm v-model="showVow" @confirm="onVowConfirm" />
    <!-- 解绑二次确认 -->
    <div v-if="showUnbind" class="vow-mask">
      <div class="vow-card">
        <h2 class="vow-title">确定解除羁绊？</h2>
        <p class="vow-text">解除后，此生便再无重逢之机。</p>
        <div class="vow-actions">
          <button class="btn-ghost" @click="showUnbind = false">再 想 想</button>
          <button class="btn-coral" @click="doUnbind">解 除</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 背景根：深蓝夜空兜底（可换成婚礼底图 url） */
.holy {
  position: relative;
  min-height: 100vh;
  overflow: hidden;
  background: radial-gradient(circle at 50% 0%, #1a2350 0%, #0b1026 60%);
}

/* 顶部金光 */
.holy::before {
  content: '';
  position: absolute;
  inset: 0;
  background: radial-gradient(circle at 50% -10%, rgba(255, 213, 150, .55), transparent 60%);
  mix-blend-mode: screen;
  pointer-events: none;
}

/* 底部青蓝压暗 */
.holy::after {
  content: '';
  position: absolute;
  inset: 0;
  background: radial-gradient(circle at 50% 120%, rgba(94, 200, 255, .45), transparent 70%);
  mix-blend-mode: multiply;
  pointer-events: none;
}

/* 上浮光斑 */
.holy-particle {
  position: absolute;
  bottom: -10px;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: rgba(255, 213, 150, .8);
  box-shadow: 0 0 12px 4px rgba(255, 213, 150, .6);
  animation: floatUp 8s linear infinite;
}

/* 光斑上浮动画 */
@keyframes floatUp {
  0% {
    transform: translateY(0);
    opacity: 0;
  }

  20% {
    opacity: 1;
  }

  100% {
    transform: translateY(-110vh);
    opacity: 0;
  }
}

/* 磨砂金边卡片 */
.glass-card {
  position: relative;
  z-index: 1;
  max-width: 520px;
  margin: 12vh auto 0;
  text-align: center;
  background: rgba(255, 255, 255, .08);
  border: 1px solid rgba(255, 213, 150, .55);
  border-radius: 18px;
  padding: 32px;
  box-shadow: 0 0 40px rgba(94, 200, 255, .15);
  backdrop-filter: blur(12px);
}

/* 标题 */
.title {
  color: #5ec8ff;
  margin: 0 0 12px;
}

/* 金色标题 */
.title.gold {
  color: #ffd596;
}

/* 副文 */
.sub {
  color: #ff7a8a;
  margin: 0 0 20px;
}

/* 邀请码展示 */
.code-show {
  color: #ffd596;
  font-size: 28px;
  letter-spacing: 6px;
  margin: 12px 0;
}

/* 纪念日 */
.anniversary {
  color: #ffd596;
  margin: 8px 0 18px;
}

/* 誓约框 */
.vow-box {
  color: #5ec8ff;
  line-height: 2;
  margin: 0 0 24px;
}

/* 输入行 */
.bind-row {
  display: flex;
  gap: 10px;
  margin-top: 18px;
}

/* 邀请码输入 */
.code-input {
  flex: 1;
  padding: 10px 12px;
  border-radius: 10px;
  background: rgba(255, 255, 255, .06);
  border: 1px solid rgba(94, 200, 255, .4);
  color: #eaeaf2;
}

/* 青蓝次按钮 */
.btn-cyan {
  padding: 10px 22px;
  border-radius: 10px;
  cursor: pointer;
  background: rgba(94, 200, 255, .15);
  border: 1px solid #5ec8ff;
  color: #5ec8ff;
}

/* 危险按钮 */
.btn-danger {
  margin-top: 8px;
  padding: 10px 24px;
  border-radius: 10px;
  cursor: pointer;
  background: rgba(255, 80, 80, .12);
  border: 1px solid rgba(255, 120, 120, .5);
  color: #ff9a9a;
}

/* 哲学卡片包裹 */
.philo-wrap {
  position: relative;
  z-index: 1;
  padding-top: 16vh;
}

/* 下列样式供解绑弹窗复用 */
.vow-mask {
  position: fixed;
  inset: 0;
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(5, 8, 20, .6);
  backdrop-filter: blur(4px);
}

.vow-card {
  width: min(90vw, 440px);
  background: rgba(255, 255, 255, .08);
  border: 1px solid rgba(255, 213, 150, .55);
  border-radius: 18px;
  padding: 36px 28px;
  text-align: center;
}

.vow-title {
  color: #ffd596;
  letter-spacing: 4px;
  margin: 0 0 16px;
}

.vow-text {
  color: #5ec8ff;
  line-height: 2;
  margin: 0 0 24px;
}

.vow-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
}
</style>