package com.fullstack.booking.controller;

import com.fullstack.booking.model.*;
import com.fullstack.booking.repository.*;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class BookingController {

    private final UserRepository users;
    private final ServiceRepository services;
    private final TimeSlotRepository slots;
    private final OrderRepository orders;

    public BookingController(UserRepository u, ServiceRepository s, TimeSlotRepository t, OrderRepository o) {
        users = u;
        services = s;
        slots = t;
        orders = o;
    }

    @GetMapping("/users")
    public List<User> users() {
        return users.findAll();
    }

    @PostMapping("/users")
    public User createUser(@RequestBody Map<String, String> b) {
        String name = requiredString(b, "name");
        return users.save(new User(name, parseRole(b.getOrDefault("role", "CUSTOMER"))));
    }

    @GetMapping("/services")
    public List<ServiceItem> services() {
        return services.findByIsActiveTrue();
    }

    @PostMapping("/services")
    public ServiceItem createService(@RequestBody Map<String, String> b) {
        String name = requiredString(b, "name");
        BigDecimal price = requiredDecimal(b, "price");
        Integer durationMinutes = requiredInt(b, "durationMinutes");
        return services.save(new ServiceItem(name, price, durationMinutes));
    }

    @GetMapping("/services/{id}/slots")
    public List<TimeSlot> slots(@PathVariable Long id) {
        return slots.findByServiceIdAndIsAvailableTrue(id);
    }

    @PostMapping("/slots")
    public TimeSlot createSlot(@RequestBody Map<String, String> b) {
        Long serviceId = requiredLong(b, "serviceId");
        ServiceItem service = services.findById(serviceId)
                .orElseThrow(() -> new NoSuchElementException("服务不存在：" + serviceId));
        TimeSlot slot = new TimeSlot(service, requiredDateTime(b, "startsAt"), requiredDateTime(b, "endsAt"));
        return slots.save(slot);
    }

    @GetMapping("/orders")
    public List<Order> orders(@RequestParam(required = false) Long customerId) {
        return customerId == null ? orders.findAll() : orders.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    /** 下单：整段在同一个事务里完成，并对时段加悲观锁，避免并发超卖。 */
    @Transactional
    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> b) {
        Long serviceId = requiredLong(b, "serviceId");
        Long timeSlotId = requiredLong(b, "timeSlotId");
        Long customerId = b.get("customerId") == null ? Long.valueOf(1L) : requiredLong(b, "customerId");

        ServiceItem service = services.findById(serviceId)
                .orElseThrow(() -> new NoSuchElementException("服务不存在：" + serviceId));
        User customer = users.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("用户不存在：" + customerId));
        TimeSlot slot = slots.findByIdForUpdate(timeSlotId)
                .orElseThrow(() -> new NoSuchElementException("时间段不存在：" + timeSlotId));

        if (!slot.getService().getId().equals(serviceId)) {
            throw new IllegalArgumentException("时间段不属于所选服务：" + timeSlotId);
        }

        if (!slot.getIsAvailable()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "时间段已满"));
        }

        slot.setBookedCount(slot.getBookedCount() + 1);
        if (slot.getBookedCount() >= slot.getCapacity()) {
            slot.setIsAvailable(false);
        }
        slots.save(slot);

        Order order = new Order();
        order.setService(service);
        order.setTimeSlot(slot);
        order.setCustomer(customer);
        order.setScheduledAt(slot.getStartsAt());
        order.setTotalAmount(service.getPrice());
        return ResponseEntity.ok(orders.save(order));
    }

    /** 变更订单状态：只允许 docs/order-state.md 状态图里的流转。 */
    @Transactional
    @PatchMapping("/orders/{id}/status")
    public Order status(@PathVariable Long id, @RequestBody Map<String, String> b) {
        Order order = orders.findById(id)
                .orElseThrow(() -> new NoSuchElementException("订单不存在：" + id));
        Order.Status next = parseStatus(requiredString(b, "status"));
        if (next == order.getStatus()) {
            return order;
        }
        if (!order.canTransitionTo(next)) {
            throw new IllegalArgumentException("订单状态不允许从 " + order.getStatus() + " 变更为 " + next);
        }
        order.setStatus(next);
        return orders.save(order);
    }

    /* ---------------------------------------------------------- 入参解析与校验 */

    private static String requiredString(Map<String, ?> b, String key) {
        Object value = b.get(key);
        if (value == null || String.valueOf(value).trim().isEmpty()) {
            throw new IllegalArgumentException("缺少参数：" + key);
        }
        return String.valueOf(value).trim();
    }

    private static Long requiredLong(Map<String, ?> b, String key) {
        try {
            return Long.valueOf(requiredString(b, key));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("参数格式错误：" + key + " 需要是整数");
        }
    }

    private static Integer requiredInt(Map<String, ?> b, String key) {
        try {
            return Integer.valueOf(requiredString(b, key));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("参数格式错误：" + key + " 需要是整数");
        }
    }

    private static BigDecimal requiredDecimal(Map<String, ?> b, String key) {
        try {
            return new BigDecimal(requiredString(b, key));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("参数格式错误：" + key + " 需要是数字");
        }
    }

    private static LocalDateTime requiredDateTime(Map<String, ?> b, String key) {
        try {
            return LocalDateTime.parse(requiredString(b, key));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("参数格式错误：" + key + " 需要是 ISO 时间，例如 2026-10-07T09:00:00");
        }
    }

    private static User.Role parseRole(String value) {
        try {
            return User.Role.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("角色不合法：" + value);
        }
    }

    private static Order.Status parseStatus(String value) {
        try {
            return Order.Status.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("订单状态不合法：" + value);
        }
    }
}
