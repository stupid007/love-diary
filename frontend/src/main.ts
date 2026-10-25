import { createApp } from 'vue'
import ElementPlus from 'element-plus'                       // 1. 组件库本体
import 'element-plus/dist/index.css'                         // 2. 组件库样式
import zhCn from 'element-plus/es/locale/lang/zh-cn'         // 3. 中文语言包


import App from './App.vue'
import router from './router'
import {createPinia} from 'pinia'

const app = createApp(App)

// 顺序很重要
app.use(createPinia())
app.use(router)
app.use(ElementPlus,{locale:zhCn})

app.mount('#app')

