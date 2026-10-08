<template>
  <div class="app-shell">
    <header>
      <div>
        <p class="eyebrow">SERVICE BOOKING</p>
        <h1>把时间留给值得的服务</h1>
      </div>
      <span class="online">● 预约系统在线</span>
    </header>
    <main>
      <section class="hero">
        <div>
          <p class="eyebrow">今日可预约</p>
          <h2>选择服务，安排你的专属时间</h2>
          <p class="muted">透明价格 · 灵活时段 · 全程可追踪</p>
        </div>
        <div class="stats">
          <strong>{{ orders.length }}</strong
          ><span>我的预约</span>
        </div>
      </section>
      <div class="grid">
        <section class="panel">
          <div class="section-title">
            <h3>服务目录</h3>
            <button class="ghost" @click="loadData">刷新</button>
          </div>
          <div class="service-list">
            <button
              v-for="service in services"
              :key="service.id"
              class="service"
              :class="{ selected: selectedService?.id === service.id }"
              @click="selectService(service)"
            >
              <span class="service-icon">✦</span>
              <span
                ><b>{{ service.name }}</b
                ><small>{{ service.durationMinutes }} 分钟服务</small></span
              >
              <strong>¥{{ service.price }}</strong>
            </button>
          </div>
        </section>
        <section class="panel booking">
          <h3>预约时间</h3>
          <div v-if="selectedService" class="chosen">
            已选择：<b>{{ selectedService.name }}</b>
          </div>
          <div v-else class="empty">请先从左侧选择服务</div>
          <div class="slots">
            <button
              v-for="slot in slots"
              :key="slot.id"
              :disabled="!slot.isAvailable"
              :class="{ active: selectedSlot?.id === slot.id }"
              @click="selectedSlot = slot"
            >
              <span>{{ formatTime(slot.startsAt) }}</span>
              <small>{{ slot.isAvailable ? "可预约" : "已约满" }}</small>
            </button>
          </div>
          <button
            class="primary"
            :disabled="!selectedService || !selectedSlot || submitting"
            @click="book"
          >
            {{ submitting ? "提交中…" : "确认预约" }}
          </button>
          <p v-if="notice" class="notice">{{ notice }}</p>
        </section>
      </div>
      <section class="panel orders">
        <div class="section-title">
          <h3>我的预约</h3>
          <span class="count">{{ orders.length }} 条记录</span>
        </div>
        <div v-if="orders.length" class="order-table">
          <div v-for="order in orders" :key="order.id" class="order-row">
            <div>
              <b>{{ order.service?.name || "服务预约" }}</b>
              <small>{{ formatDate(order.scheduledAt) }}</small>
            </div>
            <span class="tag" :class="order.status.toLowerCase()">{{
              statusText(order.status)
            }}</span>
            <strong>¥{{ order.totalAmount }}</strong>
            <button
              v-if="order.status === 'PENDING'"
              class="link"
              @click="advance(order)"
            >
              模拟接单
            </button>
            <button
              v-else-if="order.status === 'ASSIGNED'"
              class="link"
              @click="advance(order)"
            >
              开始服务
            </button>
            <button
              v-else-if="order.status === 'IN_PROGRESS'"
              class="link"
              @click="advance(order)"
            >
              完成服务
            </button>
          </div>
        </div>
        <div v-else class="empty">还没有预约，选一个服务开始吧。</div>
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import {
  API_PREFIX,
  DEMO_CUSTOMER_ID,
  DEMO_SERVICES,
  DEMO_SLOTS,
  NEXT_ORDER_STATUS,
  ORDER_STATUS_LABELS,
} from "../global/variables";

// 页面状态：只由当前预约页面使用的响应式变量。
const services = ref([]);
const slots = ref([]);
const orders = ref([]);
const selectedService = ref(null);
const selectedSlot = ref(null);
const submitting = ref(false);
const notice = ref("");

async function api(path, options) {
  const r = await fetch(API_PREFIX + path, options);
  if (!r.ok) throw Error();
  return r.json();
}
async function loadData() {
  try {
    services.value = await api("/services");
    orders.value = await api(`/orders?customerId=${DEMO_CUSTOMER_ID}`);
  } catch {
    services.value = DEMO_SERVICES;
    orders.value = [];
  }
}
async function selectService(s) {
  selectedService.value = s;
  selectedSlot.value = null;
  try {
    slots.value = await api(`/services/${s.id}/slots`);
  } catch {
    slots.value = DEMO_SLOTS;
  }
}
async function book() {
  submitting.value = true;
  try {
    const o = await api("/orders", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        customerId: DEMO_CUSTOMER_ID,
        serviceId: selectedService.value.id,
        timeSlotId: selectedSlot.value.id,
      }),
    });
    orders.value.unshift(o);
    notice.value = "预约成功，订单已进入待接单";
  } catch {
    orders.value.unshift({
      id: Date.now(),
      service: selectedService.value,
      scheduledAt: selectedSlot.value.startsAt,
      totalAmount: selectedService.value.price,
      status: "PENDING",
    });
    notice.value = "预约成功（演示数据）";
  } finally {
    submitting.value = false;
  }
}
async function advance(o) {
  const next = NEXT_ORDER_STATUS[o.status];
  try {
    await api(`/orders/${o.id}/status`, {
      method: "PATCH",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ status: next }),
    });
  } catch {}
  o.status = next;
}
const statusText = (s) => ORDER_STATUS_LABELS[s] || s;
const formatTime = (v) =>
  new Date(v).toLocaleTimeString("zh-CN", {
    hour: "2-digit",
    minute: "2-digit",
  });
const formatDate = (v) =>
  new Date(v).toLocaleString("zh-CN", {
    month: "numeric",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
onMounted(loadData);
</script>

<style src="./App.css"></style>
