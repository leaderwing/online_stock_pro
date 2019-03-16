package com.online.stock.repository;

import com.online.stock.model.SecuritiesPractice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecuritiesPracticeRepository extends JpaRepository<SecuritiesPractice,  String> {

    List<SecuritiesPractice> findBySymbol (String symbol);
}
