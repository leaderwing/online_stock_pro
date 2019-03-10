package com.online.stock.repository;

import com.online.stock.model.ODMast;
import com.online.stock.model.ODMasthist;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ODMasthistRepository extends JpaRepository<ODMasthist, String> {
    List<ODMasthist> findAllByAfacctnoAndTxdateIsLessThanEqualAndTxdateIsGreaterThanEqual(String loggedUsername, int toDate, int fromDate);
}
