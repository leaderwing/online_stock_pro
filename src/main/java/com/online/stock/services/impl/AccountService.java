package com.online.stock.services.impl;

import com.online.stock.dto.RegisterRequest;
import com.online.stock.dto.response.AccountInfoRes;
import com.online.stock.model.Afmast;
import com.online.stock.repository.AfmastRepository;
import com.online.stock.services.IAccountService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.online.stock.utils.DateUtils;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;

@Service
public class AccountService implements IAccountService {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private AfmastRepository userRepository;

    @Override public void changePassword(String username, String newPassword) {
        Afmast appUser = userRepository.findOneByUsername(username);
        if (appUser != null) {
            appUser.setPassword(newPassword);
            userRepository.save(appUser);
        }
    }

    @Override public String register(RegisterRequest rq, String password) {
        String errCode = "";
        Session session = entityManager.unwrap(Session.class);
        ProcedureCall call = session.createStoredProcedureCall("PKG_OPEN_CONTRACTS.prc_create_contracts");
        call.registerParameter(1, String.class, ParameterMode.IN).bindValue(rq.getAcctno());
        call.registerParameter(2, String.class, ParameterMode.IN).bindValue(rq.getFullname());
        call.registerParameter(3, String.class, ParameterMode.IN).bindValue(DateUtils.convertYYYY_MM_DD(rq.getDateofbirth()));
        call.registerParameter(4, String.class, ParameterMode.IN).bindValue(rq.getSex());
        call.registerParameter(5, String.class, ParameterMode.IN).bindValue(rq.getIdcode());
        call.registerParameter(6, String.class, ParameterMode.IN).bindValue(DateUtils.convertYYYY_MM_DD(rq.getIddate()));
        call.registerParameter(7, String.class, ParameterMode.IN).bindValue(rq.getIdplace());
        call.registerParameter(8, String.class, ParameterMode.IN).bindValue(rq.getAddress());
        call.registerParameter(9, String.class, ParameterMode.IN).bindValue(rq.getPhone());
        call.registerParameter(10, String.class, ParameterMode.IN).bindValue(rq.getBankacctno());
        call.registerParameter(11, String.class, ParameterMode.IN).bindValue(rq.getBankname());
        call.registerParameter(12, String.class, ParameterMode.IN).bindValue(rq.getAccType());
        call.registerParameter(13, String.class, ParameterMode.IN).bindValue(password);
        call.registerParameter(14, Integer.class, ParameterMode.OUT);
        errCode = (String) call.getOutputs().getOutputParameterValue(14);
        return errCode;
    }

    @Override
    public List<AccountInfoRes> getAccountList() {
        List<AccountInfoRes> resList = new ArrayList<>();
        SessionFactory sessionFactory = new Configuration().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        SQLQuery query = session.createSQLQuery("select cfmast.custid as CUSTID,cfmast.fullname " +
                "as " +
                "FULLNAME," +
                 "afmast.pin as PIN  from afmast, cfmast where cfmast.custid = afmast.acctno and afmast.status = 'P'")
                .addScalar("CUSTID", new StringType())
                .addScalar("FULLNAME", new StringType())
                .addScalar("PIN", new StringType());
        List<Object[]> rows = query.list();
        for (Object[] row : rows) {
            AccountInfoRes res = new AccountInfoRes();
            res.setCUSTID(row[0].toString());
            res.setFULLNAME(row[1].toString());
            res.setPIN(row[2].toString());
            resList.add(res);
        }
        transaction.commit();
        session.close();
        return resList;
    }
}
