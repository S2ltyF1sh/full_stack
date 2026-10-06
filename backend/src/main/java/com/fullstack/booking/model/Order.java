package com.fullstack.booking.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private User staff;

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id")
    private ServiceItem service;

    @ManyToOne
    @JoinColumn(name = "time_slot_id")
    private TimeSlot timeSlot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Status {
        PENDING, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED
    }

    /**
     * 允许的状态流转，对应 docs/order-state.md 的状态图。
     * 终态（COMPLETED、CANCELLED）不允许再流转。
     */
    private static final Map<Status, Set<Status>> ALLOWED_TRANSITIONS = buildAllowedTransitions();

    private static Map<Status, Set<Status>> buildAllowedTransitions() {
        Map<Status, Set<Status>> transitions = new EnumMap<>(Status.class);
        transitions.put(Status.PENDING, EnumSet.of(Status.ASSIGNED, Status.CANCELLED));
        transitions.put(Status.ASSIGNED, EnumSet.of(Status.IN_PROGRESS, Status.CANCELLED));
        transitions.put(Status.IN_PROGRESS, EnumSet.of(Status.COMPLETED));
        transitions.put(Status.COMPLETED, EnumSet.noneOf(Status.class));
        transitions.put(Status.CANCELLED, EnumSet.noneOf(Status.class));
        return Collections.unmodifiableMap(transitions);
    }

    /** 当前状态能否流转到 next。 */
    public boolean canTransitionTo(Status next) {
        return ALLOWED_TRANSITIONS.getOrDefault(status, Collections.emptySet()).contains(next);
    }

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User v) {
        customer = v;
    }

    public User getStaff() {
        return staff;
    }

    public void setStaff(User v) {
        staff = v;
    }

    public ServiceItem getService() {
        return service;
    }

    public void setService(ServiceItem v) {
        service = v;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot v) {
        timeSlot = v;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status v) {
        status = v;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime v) {
        scheduledAt = v;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal v) {
        totalAmount = v;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
