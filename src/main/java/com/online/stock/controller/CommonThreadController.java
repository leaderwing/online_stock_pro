package com.online.stock.controller;

import com.online.stock.utils.DateUtils;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import java.util.Date;

@RestController
public class CommonThreadController {
    @Autowired
    private EntityManager entityManager;
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_1','ROLE_ADMIN_2', 'ROLE_SADMIN')")
    @RequestMapping(value = "/endDate", method = RequestMethod.POST)
    public ResponseEntity<Void> runEndDate() {
        int runDate = DateUtils.convertDate_YYYYMMDD(new Date());
        Session session = entityManager.unwrap(Session.class);
        ProcedureCall call = session.createStoredProcedureCall("PKG_ORDER_TRADING.PRC_BATCH");
        call.registerParameter(1, Integer.class, ParameterMode.IN).bindValue(runDate);
        call.getOutputs();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
