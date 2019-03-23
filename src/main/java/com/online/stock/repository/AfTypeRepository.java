package com.online.stock.repository;

import com.online.stock.model.AfType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AfTypeRepository extends JpaRepository<AfType, String> {

    AfType findByAcType (String typeId);

}
