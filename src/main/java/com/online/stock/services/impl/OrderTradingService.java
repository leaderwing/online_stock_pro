package com.online.stock.services.impl;

import com.online.stock.services.IOrderTradingService;
import io.swagger.models.auth.In;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderTradingService implements IOrderTradingService {
    @Autowired
    private EntityManager entityManager;

    @Override
    public int checkOrder(String accTno, int quantity, float price, String symbol, String order) {
      return   checkOrder(accTno,"mua",quantity,price,symbol);
    }
    private int checkOrder(String accTno, String action, int quantity, float price, String symbol) {
        int p_out = 0;
        Session session = entityManager.unwrap(Session.class);
        ProcedureCall call = session.createStoredProcedureCall("PKG_ORDER_TRADING.PRC_CHECK_CREATE_ORDER");
        call.registerParameter(1, String.class, ParameterMode.IN).bindValue(accTno);
        call.registerParameter(2, String.class, ParameterMode.IN).bindValue(action);
        call.registerParameter(3, String.class, ParameterMode.IN).bindValue(symbol);
        call.registerParameter(4, Integer.class, ParameterMode.IN).bindValue(quantity);
        call.registerParameter(5, Float.class, ParameterMode.IN).bindValue(price);
        call.registerParameter(6,Integer.class,ParameterMode.OUT);

        p_out =  (Integer) call.getOutputs().getOutputParameterValue(6);
        return  p_out;
    }
}
