<template>
  <main class="container">
    <h1>Full Stack 项目</h1>
    <p>Vue 前端已启动。</p>
    <button @click="checkApi">检查后端连接</button>
    <p v-if="message" class="message">{{ message }}</p>
  </main>
</template>

<script setup>
import { ref } from 'vue'

const message = ref('')

async function checkApi() {
  try {
    const response = await fetch('/api/health')
    const data = await response.json()
    message.value = data.message
  } catch {
    message.value = '后端连接失败，请确认 backend 已启动。'
  }
}
</script>

<style>
:root { font-family: Inter, system-ui, sans-serif; color: #1f2937; background: #f3f4f6; }
body { margin: 0; }
.container { max-width: 720px; margin: 10vh auto; padding: 3rem; background: white; border-radius: 16px; box-shadow: 0 8px 30px rgb(0 0 0 / 8%); }
button { padding: .7rem 1rem; border: 0; border-radius: 8px; color: white; background: #42b883; cursor: pointer; }
.message { margin-top: 1rem; color: #2563eb; }
</style>
