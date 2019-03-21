package com.online.stock.services.impl;

import com.online.stock.dto.response.AccountInfoRes;
import com.online.stock.dto.response.RateInfoRes;
import com.online.stock.services.IOrderTradingService;
import io.swagger.models.auth.In;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;
import org.hibernate.type.DoubleType;
import org.hibernate.type.FloatType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderTradingService implements IOrderTradingService {
    @Autowired
    private EntityManager entityManager;

    @Override
    public int checkOrder(String accTno, int quantity, double price, String symbol, String order) {
        return checkOrder(accTno, "Mua", quantity, price, symbol);
    }

    @Override
    public int checkSellOrder(String accTno, int quantity, double price, String symbol, String orderType) {
        return checkOrder(accTno, "Ban", quantity, price, symbol);
    }

    @Override
    public int saveOrder(String orderId, String symbol, String accTno, String action, String orderType, double price, int quantity, String txTime, int txDate, String floor) {
        int p_out = 0;
        Session session = entityManager.unwrap(Session.class);
        ProcedureCall call = session.createStoredProcedureCall("PKG_ORDER_TRADING.PRC_CREATE_ORDER");
        call.registerParameter(1, String.class, ParameterMode.IN).bindValue(orderId);
        call.registerParameter(2, String.class, ParameterMode.IN).bindValue(symbol);
        call.registerParameter(3, String.class, ParameterMode.IN).bindValue(accTno);
        call.registerParameter(4, String.class, ParameterMode.IN).bindValue(action);
        call.registerParameter(5, String.class, ParameterMode.IN).bindValue(orderType);
        call.registerParameter(6, Double.class, ParameterMode.IN).bindValue(price);
        call.registerParameter(7, Integer.class, ParameterMode.IN).bindValue(quantity);
        call.registerParameter(8, String.class, ParameterMode.IN).bindValue(txTime);
        call.registerParameter(9, Integer.class, ParameterMode.IN).bindValue(txDate);
        call.registerParameter(10, String.class, ParameterMode.IN).bindValue(floor);
        call.registerParameter(11, Integer.class, ParameterMode.OUT);

        p_out = (Integer) call.getOutputs().getOutputParameterValue(11);
        return p_out;
    }

    @Override
    public List<RateInfoRes> getRateInfo(String custId) {
        List<RateInfoRes> resList = new ArrayList<>();
        SessionFactory sessionFactory = new Configuration().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        SQLQuery query = session.createSQLQuery("SELECT CODEID,SUM(CASE WHEN CLEARDAY < 1 THEN CASE WHEN EXECTYPE = 'Mua' THEN EXECQTTY-CLOSEDQTTY ELSE CLOSEDQTTY-EXECQTTY END ELSE 0  END) SO_DU,  \n" +
                "                    SUM(CASE WHEN CLEARDAY = 1 THEN CASE WHEN EXECTYPE = 'Mua' THEN EXECQTTY-CLOSEDQTTY ELSE CLOSEDQTTY-EXECQTTY END ELSE 0  END) T2,  \n" +
                "                    SUM(CASE WHEN CLEARDAY = 2 THEN CASE WHEN EXECTYPE = 'Mua' THEN EXECQTTY-CLOSEDQTTY ELSE CLOSEDQTTY-EXECQTTY END ELSE 0  END) T1,  \n" +
                "                    SUM(CASE WHEN CLEARDAY = 3 THEN CASE WHEN EXECTYPE = 'Mua' THEN EXECQTTY-CLOSEDQTTY ELSE CLOSEDQTTY-EXECQTTY END ELSE 0  END) T0  \n" +
                "                    FROM ODMAST  \n" +
                "                    WHERE AFACCTNO = "+custId+"  \n" +
                "                    GROUP BY CODEID  \n" +
                "                    HAVING SUM(EXECQTTY-CLOSEDQTTY) <> 0  \n" +
                "                    ORDER BY CODEID ")
                .addScalar("CODEID", new StringType())
                .addScalar("SO_DU", new FloatType())
                .addScalar("T2", new DoubleType())
                .addScalar("T1", new DoubleType())
                .addScalar("T0", new DoubleType());
        List<Object[]> rows = query.list();
        for (Object[] row : rows) {
            RateInfoRes res = new RateInfoRes();
            res.setCustId(row[0].toString());
            res.setSo_du(Float.parseFloat(row[1].toString()));
            res.setT2(Double.parseDouble(row[2].toString()));
            res.setT1(Double.parseDouble(row[3].toString()));
            res.setT0(Double.parseDouble(row[4].toString()));
            resList.add(res);
        }
        transaction.commit();
        session.close();
        return resList;
    }

    private int checkOrder(String accTno, String action, int quantity, double price, String symbol) {
        int p_out = 0;
        Session session = entityManager.unwrap(Session.class);
        ProcedureCall call = session.createStoredProcedureCall("PKG_ORDER_TRADING.PRC_CHECK_CREATE_ORDER");
        call.registerParameter(1, String.class, ParameterMode.IN).bindValue(accTno);
        call.registerParameter(2, String.class, ParameterMode.IN).bindValue(action);
        call.registerParameter(3, String.class, ParameterMode.IN).bindValue(symbol);
        call.registerParameter(4, Integer.class, ParameterMode.IN).bindValue(quantity);
        call.registerParameter(5, Double.class, ParameterMode.IN).bindValue(price);
        call.registerParameter(6, Integer.class, ParameterMode.OUT);

        p_out = (Integer) call.getOutputs().getOutputParameterValue(6);
        return p_out;
    }
}
