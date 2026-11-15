import { fileURLToPath, URL } from 'node:url'

import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
import { defineConfig, loadEnv } from 'vite'

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  // loadEnv 读取 .env.[mode] 里的变量；第三个参数传 '' 表示不过滤 VITE_ 前缀
  const env = loadEnv(mode, process.cwd(), '')

  return {
    plugins: [
      vue(),

      /**
       * 自动导入 Vue / Vue Router / Pinia 的 API（ref、computed、useRouter、defineStore…）
       * 以及 Element Plus 的函数式组件（ElMessage、ElMessageBox…）。
       *
       * 这样代码里就不用写 import { ref } from 'vue' 了 ——
       * 代价是「看代码时不知道变量从哪来」，所以它会生成一份类型声明
       * src/types/auto-imports.d.ts，让 IDE 仍能跳转和提示。
       */
      AutoImport({
        imports: ['vue', 'vue-router', 'pinia'],
        resolvers: [ElementPlusResolver({ importStyle: false })],
        dts: 'src/types/auto-imports.d.ts',
      }),

      /**
       * 自动导入 Element Plus 的组件（<el-button>、<el-form>…），实现按需加载：
       * 只有用到的组件才会被打进产物，而不是把整个组件库都打包进去。
       */
      Components({
        resolvers: [ElementPlusResolver({ importStyle: false })],
        dts: 'src/types/components.d.ts',
      }),
    ],

    resolve: {
      alias: {
        // 用 @ 代表 src 目录，避免出现 ../../../ 这种相对路径地狱
        '@': fileURLToPath(new URL('./src', import.meta.url)),
      },
    },

    server: {
      host: '127.0.0.1',
      port: 5173,
      proxy: {
        /**
         * 开发环境跨域方案：浏览器请求 /api/auth/login
         *   → Vite 开发服务器把 /api 前缀去掉，转发到 http://127.0.0.1:8090/auth/login
         * 因为浏览器眼里请求的是同源的 5173 端口，所以根本不存在跨域。
         * （生产环境由 Nginx 做同样的事）
         */
        '/api': {
          target: env.VITE_PROXY_TARGET || 'http://127.0.0.1:8090',
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/api/, ''),
        },
      },
    },
  }
})
