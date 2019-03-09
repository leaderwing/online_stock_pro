package com.online.stock.repository;

import com.online.stock.model.ODMast;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ODMastRepository extends JpaRepository<ODMast, String> {
    List<ODMast> findAllByAfacctnoAndTxdateIsLessThanEqualAndTxdateIsGreaterThanEqual(String loggedUsername, int toDate,int fromDate);

    ODMast findFirstByOrderid(String orderId);
}