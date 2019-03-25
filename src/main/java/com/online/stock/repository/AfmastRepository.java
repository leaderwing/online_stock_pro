package com.online.stock.repository;

import com.online.stock.model.Afmast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Hendi Santika
 *
 */
public interface AfmastRepository extends JpaRepository<Afmast, Long> {
	Afmast findOneByUsername(String username);
	Afmast findFirstByEmail(String email);
}
