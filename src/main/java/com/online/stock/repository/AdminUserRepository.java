package com.online.stock.repository;

import com.online.stock.model.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUser, String> {
}
