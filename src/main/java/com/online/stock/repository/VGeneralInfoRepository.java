package com.online.stock.repository;

import com.online.stock.model.VGeneralInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VGeneralInfoRepository extends JpaRepository<VGeneralInfo, String> {
    List<VGeneralInfo> findByCustId (String custId);
}
