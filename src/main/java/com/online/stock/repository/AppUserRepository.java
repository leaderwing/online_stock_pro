package com.online.stock.repository;

import com.online.stock.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Hendi Santika
 *
 */
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	public AppUser findOneByUsername(String username);
}
