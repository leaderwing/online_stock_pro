
package com.online.stock.batch.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Transactional
public interface IBatchService {

   void getMatchCancelValue() throws SQLException;

   void  getPriceValue() throws SQLException;

   void getToken ();
}
