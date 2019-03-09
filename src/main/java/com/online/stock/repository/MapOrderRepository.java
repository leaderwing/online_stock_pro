package com.online.stock.repository;

import com.online.stock.model.MapOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapOrderRepository extends JpaRepository<MapOrder, String> {
}
