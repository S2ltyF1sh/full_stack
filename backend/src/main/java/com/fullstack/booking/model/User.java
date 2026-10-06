package com.fullstack.booking.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.CUSTOMER;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Role {
        CUSTOMER, STAFF, ADMIN
    }

    public User() {
    }

    public User(String name, Role role) {
        this.name = name;
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role v) {
        role = v;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
