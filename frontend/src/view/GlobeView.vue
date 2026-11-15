<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'
import Globe from 'globe.gl'
import BottomNav from '@/components/BottomNav.vue'

const globeEl = ref<HTMLElement | null>(null)
let globe: any = null

// 1. 先定义点位的类型
interface CouplePoint {
    id: number
    name: string
    lat: number
    lng: number
    isMe: boolean
    city: string
    distance: string
    snippet: string
    avatar: string
}

// 2. points 数组加上类型
const points: CouplePoint[] = [
    { id: 1, name: '我', lat: 39.9042, lng: 116.4074, isMe: true, city: '北京', distance: '', snippet: '', avatar: '' },
    { id: 2, name: '小鹿', lat: 31.2304, lng: 121.4737, isMe: false, city: '上海', distance: '两地相距：1,065 km', snippet: '今天也很想你', avatar: '' }
]
onMounted(async () => {
    // @ts-ignore
    globe = Globe()(globeEl.value!)
        .globeImageUrl('/textures/earth-dark.jpg')
        .bumpImageUrl('/textures/earth-topology.png')
        .showAtmosphere(false)
        .atmosphereColor('#38f0ff')
        .atmosphereAltitude(0.25)
    globe.renderer().setPixelRatio(Math.min(window.devicePixelRatio, 0.8))
    globe.polygonsTransitionDuration(0)
    // 从 public/data 目录下异步请求全球国家边界数据（GeoJSON 格式）
    const countries = await fetch('/data/countries.geojson').then(r => r.json())
    // 从 public/data 目录下异步请求中国省市边界数据（GeoJSON 格式）
    // const provinces = await fetch('/data/100000_full.json').then(r => r.json())
    // 将国家和省份的边界要素（features 数组）合并，并交给 3D 地球渲染出多边形图层
    globe.polygonsData(countries.features)
        // 设置多边形【顶面】的颜色：深蓝灰色，30% 透明度（隐约透出底下的地球贴图）
        .polygonCapColor(() => 'rgba(10,20,40,0.35)')
        // 设置多边形【侧面】的颜色：更暗的深蓝色，10% 透明度（用于展现厚度，增加立体感）
        .polygonSideColor(() => 'rgba(10,20,40,0.1)')
        // 设置多边形【边框线】的颜色：亮青色（霓虹发光感，勾勒出国家/省份的轮廓）
        .polygonStrokeColor(() => '#7fd4ff')
        // 设置多边形的【海拔高度】：0.01（微凸起，贴合地球表面，防止高度过高产生穿模闪烁）
        .polygonAltitude(0.01),

        // 设置渲染 HTML 元素的数据源（points 是一个数组，包含 lat、lng、avatar、name 等字段）
        // 使用 htmlElementsData 是为了渲染真实的 DOM 元素，而不是 3D 几何体，这样 CSS 样式和 hover 事件才能直接生效
        globe.htmlElementsData(points)
            // 告诉地球：从数据的哪个字段读取【纬度】和【经度】
            .htmlLat((d: CouplePoint) => d.lat)
            .htmlLng((d: CouplePoint) => d.lng)
            // 设置 HTML 元素悬浮在距离地球表面多高的地方（0.05 代表稍微浮在空中，避免和地球模型重叠）
            .htmlAltitude(0.05)
            // 自定义每一个点位的 DOM 结构（d 代表 points 数组里的每一个对象）
            .htmlElement((d: CouplePoint) => {
                // 1. 创建一个普通的 <div> 元素作为容器
                const div = document.createElement('div')
                // 2. 给容器加上类名，方便写 CSS 样式
                div.className = 'globe-marker'
                // 3. 拼装内部的 HTML 结构（使用模板字符串插入动态数据）
                // 这里用了一个小技巧：根据 d.isMe 是否为 true，赋予不同的 class（'me' 或 'ta'），方便用 CSS 区分你和我
                div.innerHTML = `
        <!-- 这个 pin 小圆点用来在地球上标记位置 -->
        <div class="pin ${d.isMe ? 'me' : 'ta'}"></div>
        <!-- 这个 card 卡片就是鼠标悬停时弹出的详细信息 -->
        <div class="card">
          <!-- 显示头像，如果没有头像就显示默认图 -->
          <img class="avatar" src="${d.avatar || '/default-avatar.png'}" />
          <div class="info">
            <!-- 显示昵称 -->
            <div class="nick">${d.name}</div>
            <!-- 显示所在城市 -->
            <div class="city">${d.city}</div>
            <!-- 显示距离信息 -->
            <div class="distance">🧑‍🤝‍🧑 ${d.distance}</div>
            <!-- 显示一句恋爱简讯（比如：❤️ 今天也很喜欢你） -->
            <div class="snippet">❤️ ${d.snippet}</div>
          </div>
        </div>`
                // ↓ 不用 CSS :hover，用真实事件开关卡片
                div.addEventListener('mouseenter', () => div.classList.add('show-card'))
                div.addEventListener('mouseleave', () => div.classList.remove('show-card'))
                // 4. 必须返回这个创建好的 DOM 元素，globe.gl 会接管它并挂载到 3D 地球上
                return div
            })
    // ── 情侣连线 (Arcs) ─────────────────────────
    // 数据源：取出 points 数组里的前两个点（通常代表“我”和“TA”的坐标）
    globe.arcsData([{
        startLat: points[0].lat,    // 起点纬度（你的位置）
        startLng: points[0].lng,    // 起点经度
        endLat: points[1].lat,      // 终点纬度（TA的位置）
        endLng: points[1].lng,      // 终点经度
        color: ['#38f0ff', '#ff5fa2'] // 自定义字段：连线的渐变色（青色 -> 粉色）
    }])
        .arcColor('color')
        .arcDashLength(0.4)           // 设置连线的虚线长度（0 到 1 之间，0.4 表示虚线占连线的 40%）
        .arcDashGap(0.2)              // 设置虚线的间隔（0.2 表示空白部分占 20%）
        .arcDashAnimateTime(1500)     // 动画时间：1500 毫秒，让虚线像流星一样从你飞向 TA
        .arcStroke(0.5)               // 连线在 3D 空间里的粗细程度

    // ── 涟漪 (Rings) ─────────────────────────
    // 数据源：整个 points 数组，给每一个坐标点都加上涟漪特效
    globe.ringsData(points)
        .ringLat((d: CouplePoint) => d.lat)     // 读取纬度
        .ringColor(() => (t: any) => `rgba(56,240,255,${1 - t})`) // 动态颜色：t 是动画进度(0~1)，透明度 1-t 表示涟漪越扩散越透明，最终消失
        .ringMaxRadius(3)                       // 涟漪扩散的最大半径（在地球上看起来像是一个水波纹）
        .ringPropagationSpeed(2)                // 涟漪扩散的速度
        .ringRepeatPeriod(1200)                 // 涟漪重复出现的周期（1200 毫秒 = 1.2 秒重新扩散一次，形成“呼吸”感）

    // ── 视角定位 (pointOfView) ─────────────────
    // 控制 3D 摄像机的视角，直接飞向指定位置
    globe.pointOfView(
        { lat: 30, lng: 110, altitude: 2.2 },   // lat: 纬度 30 (大概在中国中部), lng: 经度 110, altitude: 2.2 (相机距离地面的高度/缩放级别)
        0                                       // 过渡时间：0 表示瞬间定位过去，如果写 2000 就是花 2 秒平滑飞过去
    )
})

onBeforeUnmount(() => { globe?._destructor?.() })
</script>

<template>
    <div class="globe-wrap">
        <div ref="globeEl" class="globe-canvas"></div>
        <h1 class="title">恋爱ing</h1>
        <p class="subtitle">记录我们的每一次心跳</p>
    </div>
    <BottomNav />
</template>

<style scoped>
.globe-wrap {
    position: relative;
    width: 100%;
    height: 100vh;
    background: #070713;
    overflow: hidden;
}

.globe-canvas {
    width: 100%;
    height: 100%;
}

.title {
    position: absolute;
    top: 22px;
    left: 50%;
    transform: translateX(-50%);
    margin: 0;
    color: #fff;
    font-size: 22px;
    letter-spacing: 3px;
    background: linear-gradient(90deg, #38f0ff, #ff5fa2);
    -webkit-background-clip: text;
    background-clip: text;
    -webkit-text-fill-color: transparent;
}

.subtitle {
    position: absolute;
    top: 56px;
    left: 50%;
    transform: translateX(-50%);
    margin: 0;
    color: #8c9db5;
    font-size: 13px;
}

:deep(.globe-marker) {
    position: relative;
    width: 22px;
    height: 22px;
    cursor: pointer;
    pointer-events: auto;
}

:deep(.pin) {
    width: 22px;
    height: 22px;
    border-radius: 50%;
    background: #38f0ff;
    box-shadow: 0 0 12px #38f0ff;
}

:deep(.pin.ta) {
    background: #ff5fa2;
    box-shadow: 0 0 12px #ff5fa2;
}

:deep(.card) {
    position: absolute;
    bottom: 30px;
    left: 50%;
    transform: translateX(-50%) scale(0);
    opacity: 0;
    transition: all .25s ease;
    width: 210px;
    padding: 12px;
    border-radius: 12px;
    background: rgba(10, 20, 40, .85);
    border: 1px solid #ff5fa2;
    backdrop-filter: blur(8px);
    color: #fff;
    pointer-events: none;
    z-index: 20;
}

:deep(.globe-marker.show-card .card) {
    transform: translateX(-50%) scale(1);
    opacity: 1;
}

:deep(.card .avatar) {
    width: 38px;
    height: 38px;
    border-radius: 50%;
}

:deep(.card .nick) {
    font-weight: 600;
    margin: 4px 0;
}

.title,
.subtitle {
    position: absolute;
    z-index: 10;
}
</style>