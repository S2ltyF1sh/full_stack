package com.fullstack.booking.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "time_slots", uniqueConstraints = @UniqueConstraint(name = "uk_time_slots_service_start", columnNames = {"service_id", "starts_at"}))
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id")
    private ServiceItem service;

    @Column(nullable = false)
    private LocalDateTime startsAt;

    @Column(nullable = false)
    private LocalDateTime endsAt;

    @Column(nullable = false)
    private Integer capacity = 1;

    @Column(nullable = false)
    private Integer bookedCount = 0;

    @Column(nullable = false)
    private Boolean isAvailable = true;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public TimeSlot() {
    }

    public TimeSlot(ServiceItem s, LocalDateTime a, LocalDateTime b) {
        service = s;
        startsAt = a;
        endsAt = b;
    }

    public Long getId() {
        return id;
    }

    public ServiceItem getService() {
        return service;
    }

    public void setService(ServiceItem v) {
        service = v;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(LocalDateTime v) {
        startsAt = v;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(LocalDateTime v) {
        endsAt = v;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer v) {
        capacity = v;
    }

    public Integer getBookedCount() {
        return bookedCount;
    }

    public void setBookedCount(Integer v) {
        bookedCount = v;
    }

    public Boolean getIsAvailable() {
        return isAvailable && bookedCount < capacity;
    }

    public void setIsAvailable(Boolean v) {
        isAvailable = v;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
