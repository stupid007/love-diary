//node:url提供fileURLToPath/URL,用来算绝对路径，比（path.resolve更稳定，不受运行目录的影响）
import { fileURLToPath, URL } from 'node:url'

// vue单文件(.vue)编译插件，脚手架自带
import vue from '@vitejs/plugin-vue'
//带类型提示的包裹函数,写配置是有补全
import { defineConfig } from 'vite'

export default defineConfig({
  //插件列表：目前只需要vue
  plugins:[vue()],
  //路径解析
  resolve:{
    alias:{
      // 以后写import{xxx} from '@/api/auth'就等于src/api/auth
      '@':fileURLToPath(new URL('./src',import.meta.url))
    },
  },
  // 开发服务器
  server: {
    port:5173,
    open:true, //npm run dev自动打开浏览器
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:8090',
        changeOrigin: true,
        rewrite: path =>path.replace(/^\/api/,''),
      },
    },
  },
})
 
