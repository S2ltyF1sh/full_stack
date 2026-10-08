/**
 * Shared frontend variables.
 *
 * Keep values that are independent from a particular page here so that new
 * components can use the same API settings, demo data and display labels.
 */
export const API_PREFIX = "/api";
export const DEMO_CUSTOMER_ID = 1;

export const DEMO_SERVICES = Object.freeze([
  Object.freeze({
    id: 1,
    name: "深度家庭保洁",
    price: 199,
    durationMinutes: 120,
  }),
  Object.freeze({
    id: 2,
    name: "空调清洗护理",
    price: 129,
    durationMinutes: 60,
  }),
  Object.freeze({
    id: 3,
    name: "家电安装服务",
    price: 89,
    durationMinutes: 45,
  }),
]);

export const DEMO_SLOTS = Object.freeze([
  Object.freeze({ id: 1, startsAt: "2026-10-07T09:00:00", isAvailable: true }),
  Object.freeze({ id: 2, startsAt: "2026-10-07T13:30:00", isAvailable: true }),
  Object.freeze({ id: 3, startsAt: "2026-10-07T18:00:00", isAvailable: true }),
]);

export const ORDER_STATUS_LABELS = Object.freeze({
  PENDING: "待接单",
  ASSIGNED: "已指派",
  IN_PROGRESS: "服务中",
  COMPLETED: "已完成",
  CANCELLED: "已取消",
});

export const NEXT_ORDER_STATUS = Object.freeze({
  PENDING: "ASSIGNED",
  ASSIGNED: "IN_PROGRESS",
  IN_PROGRESS: "COMPLETED",
});
