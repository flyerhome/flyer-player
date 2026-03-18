import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import Components from 'unplugin-vue-components/vite';
import { AntDesignVueResolver } from 'unplugin-vue-components/resolvers';
import compression from 'vite-plugin-compression';

// https://vite.dev/config/
export default defineConfig({
  base:'/flyer-player/',
  server: {
    host: '0.0.0.0', // 允许所有IP访问（局域网/外网），也可填具体IP如192.168.1.100
    port:8008,
    proxy: {
      '/api': {
        target: 'http://0.0.0.0:8009/',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api/, '')
      }
    }
  },
  build:{
    outDir:'./deploy'
  },
  plugins: [
      vue(),

    Components({
      resolvers: [
        AntDesignVueResolver({
          importStyle: false, // css in js
        }),
      ],
    }),
    compression({
      algorithm: 'gzip', // 启用 gzip 压缩
      threshold: 10240 // 超过 10KB 的文件才压缩
    })
  ],
})
