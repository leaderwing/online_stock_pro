package com.online.stock.services.impl;

import com.online.stock.dto.RegisterRequest;
import com.online.stock.dto.response.AccountInfoRes;
import com.online.stock.model.Afmast;
import com.online.stock.repository.AfmastRepository;
import com.online.stock.services.IAccountService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements IAccountService {
    @Autowired
    private AfmastRepository userRepository;

    @Override public void changePassword(String username, String newPassword) {
        Afmast appUser = userRepository.findOneByUsername(username);
        if (appUser != null) {
            appUser.setPassword(newPassword);
            userRepository.save(appUser);
        }
    }

    @Override public String register(RegisterRequest registerRequest) {
        String errCode = "";
        SessionFactory sessionFactory = new Configuration().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createSQLQuery(" {call PKG_OPEN_CONTRACTS.prc_create_contracts(?,?,?,?,?,?,?,?,?,?,?,?)} ");
        query.setParameter("p_acctno",String.class);
        query.executeUpdate();
        transaction.commit();
        session.close();
        sessionFactory.close();
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
