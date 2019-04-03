package com.online.stock.services.impl;

import com.online.stock.dto.RegisterRequest;
import com.online.stock.dto.response.AccountInfoRes;
import com.online.stock.model.Afmast;
import com.online.stock.repository.AfmastRepository;
import com.online.stock.services.IAccountService;

import java.util.*;

import com.online.stock.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.type.DateType;
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
        call.registerParameter(3, Date.class, ParameterMode.IN).bindValue(DateUtils.convertDate(rq.getDateofbirth()));
        call.registerParameter(4, String.class, ParameterMode.IN).bindValue(rq.getSex());
        call.registerParameter(5, String.class, ParameterMode.IN).bindValue(rq.getIdcode());
        call.registerParameter(6, Date.class, ParameterMode.IN).bindValue(DateUtils.convertDate(rq.getIddate()));
        call.registerParameter(7, String.class, ParameterMode.IN).bindValue(StringUtils.isBlank(rq.getIdplace())?"" : rq.getIdplace());
        call.registerParameter(8, String.class, ParameterMode.IN).bindValue(StringUtils.isBlank(rq.getAddress())?"" : rq.getAddress());
        call.registerParameter(9, String.class, ParameterMode.IN).bindValue(StringUtils.isBlank(rq.getPhone())?"" : rq.getPhone());
        call.registerParameter(10, String.class, ParameterMode.IN).bindValue(rq.getEmail());
        call.registerParameter(11, String.class, ParameterMode.IN).bindValue(rq.getBankacctno());
        call.registerParameter(12, String.class, ParameterMode.IN).bindValue(StringUtils.isBlank(rq.getBankname())?"" : rq.getBankname());
        call.registerParameter(13, String.class, ParameterMode.IN).bindValue(password);
        call.registerParameter(14, String.class, ParameterMode.IN).bindValue(rq.getAccType());
        call.registerParameter(15, String.class, ParameterMode.OUT);
        errCode = (String) call.getOutputs().getOutputParameterValue(15);
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

    @Override
    public RegisterRequest getUserInfo(String custId) {
        RegisterRequest res = new RegisterRequest();
        SessionFactory sessionFactory = new Configuration().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        SQLQuery query = session.createSQLQuery("select c.custId, c.dateofbirth, c.email, c.fullname, c.idcode, c.iddate, c.idplace" +
                " , c.address, c.phone , c.bankacctno, a.pin " +
                " from cfmast c , afmast a where c.custId = a.custId and c.custId = "+custId+" ")
                .addScalar("custId", new StringType())
                .addScalar("dateofbirth", new DateType())
                .addScalar("email", new StringType())
                .addScalar("fullname", new StringType())
                .addScalar("idcode", new StringType())
                .addScalar("iddate", new DateType())
                .addScalar("idplace",new StringType())
                .addScalar("address", new StringType())
                .addScalar("phone",  new StringType())
                .addScalar("bankacctno", new StringType())
                .addScalar("pin", new StringType());
        List<Object[]> rows = query.list();
        if ( rows.size() > 0) {
            Object [] row = rows.get(0);
           res.setAcctno(row[0] == null ? null : row[0].toString());
           res.setDateofbirth(row[1] == null ? null : row[1].toString());
           res.setEmail(row[2] == null ? null : row[2].toString());
           res.setFullname(row[3] == null ? null : row[3].toString());
           res.setIdcode(row[4] == null ? null : row[4].toString());
           res.setIddate(row[5] == null ? null : row[5].toString());
           res.setIdplace(row[6] == null ? null : row[6].toString());
           res.setAddress(row[7] == null ? null : row[7].toString());
           res.setPhone(row[8] == null ? null : row[8].toString());
           res.setBankacctno(row[9] == null ? null : row[9].toString());
           res.setPin(row[10] == null ? null : row[10].toString());
        }
        transaction.commit();
        session.close();
        return res;
    }

    @Override
    public String updateUserInfo(RegisterRequest rq) {
        String errCode = "";
        Session session = entityManager.unwrap(Session.class);
        ProcedureCall call = session.createStoredProcedureCall("PKG_OPEN_CONTRACTS.PRC_CONTRACTEDIT");
        call.registerParameter(1, String.class, ParameterMode.IN).bindValue(rq.getAcctno());
        call.registerParameter(2, String.class, ParameterMode.IN).bindValue(StringUtils.trimToEmpty(rq.getFullname()));
        call.registerParameter(3, String.class, ParameterMode.IN).bindValue(StringUtils.trimToEmpty(rq.getFullname()));
        call.registerParameter(4, String.class, ParameterMode.IN).bindValue(DateUtils.convertDateToDDMMYYYY(StringUtils.trimToEmpty(rq.getDateofbirth())));
        call.registerParameter(5, String.class, ParameterMode.IN).bindValue(StringUtils.trimToEmpty(rq.getSex()));
        call.registerParameter(6, String.class, ParameterMode.IN).bindValue("001");
        call.registerParameter(7, String.class, ParameterMode.IN).bindValue(StringUtils.trimToEmpty(rq.getIdcode()));
        call.registerParameter(8, String.class, ParameterMode.IN).bindValue(DateUtils.convertDateToDDMMYYYY(StringUtils.trimToEmpty(rq.getIddate())));
        call.registerParameter(9, String.class, ParameterMode.IN).bindValue(StringUtils.trimToEmpty(rq.getIdplace()));
        call.registerParameter(10, String.class, ParameterMode.IN).bindValue("VNM");
        call.registerParameter(11, String.class, ParameterMode.IN).bindValue(StringUtils.trimToEmpty(rq.getAddress()));
        call.registerParameter(12, String.class, ParameterMode.IN).bindValue(StringUtils.trimToEmpty(rq.getPhone()));
        call.registerParameter(13, String.class, ParameterMode.IN).bindValue(StringUtils.trimToEmpty(rq.getEmail()));
        call.registerParameter(14, String.class, ParameterMode.IN).bindValue(StringUtils.trimToEmpty(rq.getBankacctno()));
        call.registerParameter(15, String.class, ParameterMode.IN).bindValue("004");
        call.registerParameter(16, String.class, ParameterMode.IN).bindValue("");
        call.registerParameter(17, String.class, ParameterMode.IN).bindValue("");
        call.registerParameter(18, String.class, ParameterMode.IN).bindValue(StringUtils.trimToEmpty(rq.getPin()));
        call.registerParameter(19, String.class, ParameterMode.IN).bindValue("001");
        call.registerParameter(20, String.class, ParameterMode.IN).bindValue("001");
        call.registerParameter(21, String.class, ParameterMode.OUT);
        errCode = (String) call.getOutputs().getOutputParameterValue(21);
        return errCode;
    }

    @Override
    public String getCustId(String branchCode) {
        String result = "";
        SessionFactory sessionFactory = new Configuration().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        SQLQuery query = session.createSQLQuery(
                        "SELECT PKG_OPEN_CONTRACTS.fnc_get_acctno_available('"+branchCode+"') FROM DUAL");
        List<String> rows = query.list();
        if ( rows.size() > 0) {
            result = rows.get(0);
        }
        return  result;
    }
}
