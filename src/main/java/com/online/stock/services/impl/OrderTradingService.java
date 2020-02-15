package com.online.stock.services.impl;

import com.online.stock.dto.response.AccountInfoRes;
import com.online.stock.dto.response.RateInfoRes;
import com.online.stock.services.IOrderTradingService;
import io.swagger.models.auth.In;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;

import org.apache.commons.lang.StringUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderTradingService implements IOrderTradingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderTradingService.class);
    @Autowired
    private EntityManager entityManager;

    @Override
    public int checkOrder(String accTno, int quantity, String price, String symbol, String order) {
        return checkOrder(accTno, "Mua", quantity, price, symbol);
    }

    @Override
    public int checkSellOrder(String accTno, int quantity, String price, String symbol, String orderType) {
        return checkOrder(accTno, "Ban", quantity, price, symbol);
    }

    @Override
    public int saveOrder(String orderId, String symbol, String accTno, String action, String orderType, String price, int quantity, String txTime, int txDate, String floor,int loanDates) {
        LOGGER.debug("start save order!");
        int p_out = 0;
        Session session = entityManager.unwrap(Session.class);
        ProcedureCall call = session.createStoredProcedureCall("PKG_ORDER_TRADING.PRC_CREATE_ORDER");
        call.registerParameter(1, String.class, ParameterMode.IN).bindValue(orderId);
        call.registerParameter(2, String.class, ParameterMode.IN).bindValue(symbol);
        call.registerParameter(3, String.class, ParameterMode.IN).bindValue(accTno);
        call.registerParameter(4, String.class, ParameterMode.IN).bindValue(action);
        call.registerParameter(5, String.class, ParameterMode.IN).bindValue(orderType);
        call.registerParameter(6, String.class, ParameterMode.IN).bindValue(price);
        call.registerParameter(7, Integer.class, ParameterMode.IN).bindValue(quantity);
        call.registerParameter(8, String.class, ParameterMode.IN).bindValue(txTime);
        call.registerParameter(9, Integer.class, ParameterMode.IN).bindValue(txDate);
        call.registerParameter(10, String.class, ParameterMode.IN).bindValue(floor);
        call.registerParameter(11, Integer.class, ParameterMode.IN).bindValue(loanDates);
        call.registerParameter(12, Integer.class, ParameterMode.OUT);

        p_out = (Integer) call.getOutputs().getOutputParameterValue(12);
        LOGGER.debug("result save order :" + p_out);
        return p_out;
    }

    @Override
    public int checkCloseOder(int pqtty, int poriginalqtty, String poriginalorderi) {
        LOGGER.debug("start save order!");
        int p_out = 0;
        Session session = entityManager.unwrap(Session.class);
        ProcedureCall call = session.createStoredProcedureCall("PKG_ORDER_TRADING.PRC_CHECK_CLOSE_ORDER");
        call.registerParameter(1, Integer.class, ParameterMode.IN).bindValue(pqtty);
        call.registerParameter(2, Integer.class, ParameterMode.IN).bindValue(poriginalqtty);
        call.registerParameter(3, String.class, ParameterMode.IN).bindValue(poriginalorderi);
        call.registerParameter(4, Integer.class, ParameterMode.OUT);

        p_out = (Integer) call.getOutputs().getOutputParameterValue(4);
        LOGGER.debug("result save order :" + p_out);
        return p_out;
    }

    @Override
    public List<RateInfoRes> getRateInfo(String custId) {
        List<RateInfoRes> resList = new ArrayList<>();
        SessionFactory sessionFactory = new Configuration().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        StringBuilder stringBuilder = new StringBuilder("SELECT AFACCTNO, CODEID,SUM(CASE WHEN CLEARDAY < 1 THEN CASE WHEN EXECTYPE = 'Mua' THEN EXECQTTY-CLOSEDQTTY ELSE CLOSEDQTTY-EXECQTTY END ELSE 0  END) SO_DU,  \n" +
                "                    SUM(CASE WHEN CLEARDAY = 1 THEN CASE WHEN EXECTYPE = 'Mua' THEN EXECQTTY-CLOSEDQTTY ELSE CLOSEDQTTY-EXECQTTY END ELSE 0  END) T2,  \n" +
                        "                    SUM(CASE WHEN CLEARDAY = 2 THEN CASE WHEN EXECTYPE = 'Mua' THEN EXECQTTY-CLOSEDQTTY ELSE CLOSEDQTTY-EXECQTTY END ELSE 0  END) T1,  \n" +
                        "                    SUM(CASE WHEN CLEARDAY = 3 THEN CASE WHEN EXECTYPE = 'Mua' THEN EXECQTTY-CLOSEDQTTY ELSE CLOSEDQTTY-EXECQTTY END ELSE 0  END) T0  \n" +
                        "                    FROM ODMAST  \n" );
        stringBuilder.append(" WHERE 1 = 1 ");
        if (StringUtils.isNotBlank(custId)) {
            stringBuilder.append(" AND AFACCTNO = '"+custId+"'");
        }
        stringBuilder.append(" GROUP BY CODEID,AFACCTNO HAVING SUM(EXECQTTY-CLOSEDQTTY) <> 0 ORDER BY CODEID");
        SQLQuery query = session.createSQLQuery(stringBuilder.toString())
                .addScalar("AFACCTNO", new StringType())
                .addScalar("CODEID", new StringType())
                .addScalar("SO_DU", new FloatType())
                .addScalar("T2", new DoubleType())
                .addScalar("T1", new DoubleType())
                .addScalar("T0", new DoubleType());
        List<Object[]> rows = query.list();
        for (Object[] row : rows) {
            RateInfoRes res = new RateInfoRes();
            res.setAfacctno(row[0] == null ? null : row[0].toString());
            res.setCodeid(row[1] == null ? null :row[1].toString());
            res.setSo_du(row[2] == null ? 0 :Float.parseFloat(row[2].toString()));
            res.setT2(row[3] == null ? 0 :Double.parseDouble(row[3].toString()));
            res.setT1(row[4] == null ? 0 :Double.parseDouble(row[4].toString()));
            res.setT0(row[5] == null ? 0 :Double.parseDouble(row[5].toString()));
            resList.add(res);
        }
        transaction.commit();
        session.close();
        sessionFactory.close();
        return resList;
    }

    private int checkOrder(String accTno, String action, int quantity, String price, String symbol) {
        int p_out = 0;
        Session session = entityManager.unwrap(Session.class);
        ProcedureCall call = session.createStoredProcedureCall("PKG_ORDER_TRADING.PRC_CHECK_CREATE_ORDER");
        call.registerParameter(1, String.class, ParameterMode.IN).bindValue(accTno);
        call.registerParameter(2, String.class, ParameterMode.IN).bindValue(action);
        call.registerParameter(3, String.class, ParameterMode.IN).bindValue(symbol);
        call.registerParameter(4, Integer.class, ParameterMode.IN).bindValue(quantity);
        call.registerParameter(5, String.class, ParameterMode.IN).bindValue(price);
        call.registerParameter(6, Integer.class, ParameterMode.OUT);

        p_out = (Integer) call.getOutputs().getOutputParameterValue(6);
        return p_out;
    }

}
