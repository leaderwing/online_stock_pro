package com.online.stock.repository;

import com.online.stock.model.SysVar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysVarRepository extends JpaRepository<SysVar, String> {

    SysVar findFirstByGRNAMEAndVARNAME(String grName, String varName);

}
