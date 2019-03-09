package com.online.stock.services.impl;

import com.online.stock.dto.RegisterRequest;
import com.online.stock.model.AppUser;
import com.online.stock.repository.AppUserRepository;
import com.online.stock.services.IAccountService;
import java.sql.PreparedStatement;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements IAccountService {
    @Autowired
    private AppUserRepository userRepository;

    @Override public void changePassword(String username, String newPassword) {
        AppUser appUser = userRepository.findOneByUsername(username);
        if (appUser != null) {
            appUser.setPassword(newPassword);
            userRepository.save(appUser);
        }
    }

    @Override public String register(RegisterRequest registerRequest) {
        String errCode = "";
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
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
}
