<script setup lang="ts">
// 父向子传递
const props = defineProps<{ modelValue: boolean }>()

// 子向父亲传递
const emit = defineEmits<{ 'update:modelValue': [boolean]; confirm: [] }>()
// 点击再想想；仅关闭
function onCancel() {
    emit('update:modelValue', false)
}
// 点击我愿意;先通知父组件确认，再关闭
function onConfirm() {
    emit('confirm')
    emit('update:modelValue', false)
}
</script>

<template>
    <!-- 仅当显隐为真时渲染遮罩 -->
    <div v-if="modelValue" class="vow-mask">
        <!-- 金边弹窗主体 -->
        <div class="vow-card">
            <!-- 标题（暖金） -->
            <h2 class="vow-title">结 婚 誓 言</h2>
            <!-- 誓词正文（青蓝） -->
            <p class="vow-text">
                我承诺，以真心为锚，<br>
                在你到来的每寸时光里停靠。<br>
                无论平淡或风暴，<br>
                我都选择与你并肩。
            </p>
            <!-- 按钮区 -->
            <div class="vow-actions">
                <button class="btn-ghost" @click="onCancel">再 想 想</button>
                <button class="btn-coral" @click="onConfirm">我 愿 意</button>
            </div>
        </div>
    </div>
</template>

<style scoped>
.vow-mask {
    position: fixed;
    inset: 0;
    z-index: 1000;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: rgba(5, 8, 20, .6);
    backdrop-filter: blur(4px);
}

/* 金边弹窗 */
.vow-card {
    width: min(90vw, 440px);
    background: rgba(255, 255, 255, .08);
    border: 1px solid rgba(255, 213, 150, .55);
    border-radius: 18px;
    padding: 36px 28px;
    text-align: center;
    box-shadow: 0 0 50px rgba(255, 213, 150, .2);
}

.vow-title {
    color: #ffd596;
    letter-spacing: 6px;
    margin: 0 0 20px;
}

/* 誓词 */
.vow-text {
    color: #5ec8ff;
    line-height: 2;
    margin: 0 0 28px;
}

/* 按钮区 */
.vow-actions {
    display: flex;
    gap: 16px;
    justify-content: center;
}

/* 描边次按钮 */
.btn-ghost {
    padding: 10px 22px;
    border-radius: 10px;
    cursor: pointer;
    background: transparent;
    border: 1px solid rgba(94, 200, 255, .6);
    color: #5ec8ff;
}

/* 粉珊瑚主按钮 */
.btn-coral {
    padding: 10px 26px;
    border-radius: 10px;
    cursor: pointer;
    border: none;
    background: linear-gradient(135deg, #ff7a8a, #ff9aa8);
    color: #fff;
}
</style>