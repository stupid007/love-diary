<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
// 导入中国省市区行政区划数据（用于级联选择器）和根据代码转文字的工具函数,regionData: 包含完整的省、市、区三级联动数据（数组结构),codeToText: 一个对象/函数，用于将行政区划代码（如 "110000"）转换成中文名称（如 "北京市"）
import { regionData, codeToText } from 'element-china-area-data'
import { updateLocationApi } from '@/api/geo'
import BottomNav from '@/components/BottomNav.vue'

const selected = ref<string[]>([])   // [省code, 市code, 区code]
const detail = ref('')               // 详细地址（门牌等）
// 区/县 adcode → 经纬度中心（取自 public/data/100000_full.json 的 properties.center）
const centerMap = new Map<String, [number, number]>()
onMounted(async () => {
    const geo = await fetch('data//100000_full.json').then(r => r.json())
    for (const f of geo.features) {
        if (f.properties?.center) {
            centerMap.set(String(f.properties.adcode), f.properties.center)
        }
    }
})

async function submit() {
    if (selected.value.length < 3) {
        ElMessage.warning("请先选到区/县")
        return
    }
    //把存满区划代码的数组 如['110000', '110100', '110101']，转换成中文名字的数组 ['北京市', '市辖区', '东城区']。
    const names = selected.value.map(code => codeToText[code])
    const [lng, lat] = centerMap.get(selected.value[2]) ?? [0, 0]

    await updateLocationApi({
        locationName: names.join('') + detail.value,
        province: names[0],
        city: names[1],
        district: names[2],
        lat,
        lng
    })
    ElMessage.success("位置已经保存成功，快回首页地球看看吧~")
}
</script>
<template>
    <div class='settings-page'>
        <h2>我的位置</h2>
        <el-cascader :options="regionData" v-model="selected" placeholder="请选择省/市/区" />
        <el-input v-model="detail" placeholder="详细地址选填"></el-input>
        <el-button type="primary" @click="submit">保存位置</el-button>
        <BottomNav />
    </div>
</template>
<style scoped>
.setting-page {
    position : relative;
    min-height: 100vh;
    padding: 80px 24px 90px;
    background: #070713;
    color: #fff;
    display:flex;
    flex-direction: column;
    max-width: 420px;
}
</style>