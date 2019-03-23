package com.online.stock.repository;

import com.online.stock.model.Cfmast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CfMastRepository extends JpaRepository<Cfmast, String> {
    Cfmast findByCustid(String custId);
}
