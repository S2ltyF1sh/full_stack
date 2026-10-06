package com.fullstack.booking.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "services")
public class ServiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private Boolean isActive = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    public ServiceItem() {
    }

    public ServiceItem(String n, BigDecimal p, Integer d) {
        name = n;
        price = p;
        durationMinutes = d;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String v) {
        name = v;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal v) {
        price = v;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer v) {
        durationMinutes = v;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean v) {
        isActive = v;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
