package com.fullstack.booking.repository;

import com.fullstack.booking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
