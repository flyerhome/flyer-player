<script setup>
import Hls from 'hls.js'
import {onBeforeUnmount, onMounted, ref} from "vue";
const videoUrl = 'https://vip.lz-cdn9.com/20220521/10886_5b22593a/index.m3u8'
// hls实例（用于销毁，防止内存泄漏）
let hlsInstance = null
const videoRef = ref()

// 组件挂载后初始化播放器
onMounted(() => {
  const videoElement = videoRef.value
  if (!videoElement) return

  // 1. 检测浏览器是否支持hls.js
  if (Hls.isSupported()) {
    // 初始化hls实例
    hlsInstance = new Hls({
      // 基础配置，可根据需求调整
      enableWorker: true, // 开启web worker提升性能
      lowLatencyMode: false // 关闭低延迟模式（点播场景使用）
    })

    // 绑定视频源
    hlsInstance.loadSource(videoUrl)
    hlsInstance.attachMedia(videoElement)

    // 监听资源解析完成事件
    hlsInstance.on(Hls.Events.MANIFEST_PARSED, () => {
      console.log('HLS视频解析成功，可播放')
      // 如需自动播放，浏览器限制必须静音或用户交互后触发
    })

    // 监听错误事件
    hlsInstance.on(Hls.Events.ERROR, (event, data) => {
      console.error('HLS播放错误：', data)
      // 网络错误自动重试
      if (data.fatal) {
        switch (data.type) {
          case Hls.ErrorTypes.NETWORK_ERROR:
            console.log('网络异常，尝试恢复')
            hlsInstance.startLoad()
            break
          case Hls.ErrorTypes.MEDIA_ERROR:
            console.log('媒体解码异常，尝试恢复')
            hlsInstance.recoverMediaError()
            break
          default:
            // 无法恢复，销毁实例
            destroyHls()
            break
        }
      }
    })
  }
  // 兼容Safari原生HLS支持
  else if (videoElement.canPlayType('application/vnd.apple.mpegurl')) {
    videoElement.src = videoUrl
  }
})

// 销毁实例：组件卸载时清理，防止内存泄漏
const destroyHls = () => {
  if (hlsInstance) {
    hlsInstance.destroy()
    hlsInstance = null
  }
}

// 组件卸载前销毁播放器
onBeforeUnmount(() => {
  destroyHls()
})
</script>

<template>
<div style="position: relative;padding: 0;z-index: 999;width:100%;height: 100%;background: #bbb; display: flex;justify-content: center;align-items: center;flex-direction: column">
  <video ref="videoRef" controls style="width: 90%;height: 90%;background: white;">

  </video>
</div>
</template>

<style scoped>

</style>