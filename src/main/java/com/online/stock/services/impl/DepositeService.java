package com.online.stock.services.impl;

import com.online.stock.dto.request.TransferMoneyRequest;
import com.online.stock.dto.response.ApproveDepositRes;
import com.online.stock.services.IDepositService;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class DepositeService implements IDepositService {
    @Autowired
    private EntityManager entityManager;

    @Override
    public int executeDeposit(TransferMoneyRequest request, String accName, String remoteIp,
                              String transferType) {
        int output;
        Session session = entityManager.unwrap(Session.class);
        ProcedureCall call = session.createStoredProcedureCall("PKG_ORDER_TRADING.PRC_CASH");
        call.registerParameter(1, String.class, ParameterMode.IN).bindValue(transferType);
        call.registerParameter(2, String.class, ParameterMode.IN).bindValue(accName);
        call.registerParameter(3, Double.class, ParameterMode.IN).bindValue(request.getAmount());
        call.registerParameter(4, String.class, ParameterMode.IN).bindValue(request.getAccount());
        call.registerParameter(5, String.class, ParameterMode.IN).bindValue(remoteIp);
        call.registerParameter(6, String.class, ParameterMode.IN).bindValue(request.getTxdesc());
        call.registerParameter(7, Integer.class, ParameterMode.OUT);

        output = (Integer) call.getOutputs().getOutputParameterValue(7);
        return output;
    }

    @Override
    public List<ApproveDepositRes> getListApproveDeposit(String tltx) {
        List<ApproveDepositRes> resList = new ArrayList<>();
        SessionFactory sessionFactory = new Configuration().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        SQLQuery query = session.createSQLQuery("select cfmast.FULLNAME,tltrace.CREATED_ACCTNO," +
                "tltrace.APPROVED_ACCTNO, " +
                "tltrace.APPROVED_TXTIME,tltrace.AMT,tltrace.ACCTNO,tltrace.ID from tltrace,cfmast " +
                " where tltrace.tltx = "+tltx+" and tltrace.APPROVED_ACCTNO is null " +
                " and tltrace.APPROVED_TXTIME is null and cfmast.custid = ACCTNO")
                .addScalar("FULLNAME", new StringType())
                .addScalar("CREATED_ACCTNO", new StringType())
                .addScalar("APPROVED_ACCTNO", new StringType())
                .addScalar("APPROVED_TXTIME", new StringType())
                .addScalar("AMT", new FloatType())
                .addScalar("ACCTNO", new StringType())
                .addScalar("ID",new IntegerType());
        List<Object[]> rows = query.list();
        for (Object[] row : rows) {
            ApproveDepositRes res = new ApproveDepositRes();
            res.setFullname(row[0] == null ? null : row[0].toString());
            res.setCreated_acctno(row[1] == null ? null :row[1].toString());
            res.setApproved_acctno(row[2] == null ? null :row[2].toString());
            res.setApproved_txtime(row[3] == null ? null :row[3].toString());
            res.setAmt(row[4] == null ? 0 : Double.valueOf(row[4].toString()));
            res.setAcctno(row[5] == null ? null : row[5].toString());
            res.setId(row[6] == null ? 0 : Integer.parseInt(row[6].toString()));
            resList.add(res);
        }
        transaction.commit();
        session.close();
        sessionFactory.close();
        return resList;
    }

    @Override
    public int approveDeposit(ApproveDepositRes res, String transferType) {
        int output;
        Session session = entityManager.unwrap(Session.class);
        ProcedureCall call = session.createStoredProcedureCall("PKG_ORDER_TRADING.PRC_CASH_APPROVE");
        call.registerParameter(1, Integer.class, ParameterMode.IN).bindValue(res.getId());
        call.registerParameter(2, String.class, ParameterMode.IN).bindValue(transferType);
        call.registerParameter(3, Double.class, ParameterMode.IN).bindValue(res.getAmt());
        call.registerParameter(4, String.class, ParameterMode.IN).bindValue(res.getApproved_acctno());
        call.registerParameter(5, String.class, ParameterMode.IN).bindValue(res.getAcctno());
        call.registerParameter(6, Integer.class, ParameterMode.OUT);
        output = (Integer) call.getOutputs().getOutputParameterValue(6);
        return output;
    }
}
