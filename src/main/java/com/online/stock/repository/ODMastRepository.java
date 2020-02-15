package com.online.stock.repository;

import com.online.stock.model.ODMast;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ODMastRepository extends JpaRepository<ODMast, String> {
    List<ODMast> findAllByAfacctnoAndExectypeAndTxdateIsLessThanEqualAndTxdateIsGreaterThanEqual(String loggedUsername, String execType, int toDate, int fromDate);
    List<ODMast> findAllByTxdateIsLessThanEqualAndTxdateIsGreaterThanEqual(int toDate, int fromDate);
    List<ODMast> findAllByAfacctnoAndOrderid(String loggedUsername, String orderId);
    List<ODMast> findAllByAfacctnoAndRefOderIdAndExectypeAndTxdateIsLessThanEqualAndTxdateIsGreaterThanEqual(String loggedUsername, String refOderId, String execType, int toDate, int fromDate);

    ODMast findFirstByOrderid(String orderId);
}