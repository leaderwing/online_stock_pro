package com.online.stock.controller;

import com.online.stock.dto.request.SystemVarAddReq;
import com.online.stock.model.SecuritiesPractice;
import com.online.stock.model.SysVar;
import com.online.stock.services.ISecuritiesService;
import com.online.stock.services.ISystemVarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SecuritiesController {
    @Autowired
    private ISecuritiesService securitiesService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN_2','ROLE_SADMIN')")
    @RequestMapping(value = "/getSecurity",method = RequestMethod.GET)
    public ResponseEntity<List<SecuritiesPractice>> getSecuritiesList() {
        List<SecuritiesPractice> sysVarList = securitiesService.getListSecurities();
        return new ResponseEntity<>(sysVarList, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_2','ROLE_SADMIN')")
    @RequestMapping(value = "/addSecurity", method = RequestMethod.POST)
    public ResponseEntity<Void> addSecurity (@RequestBody SecuritiesPractice req) {
        securitiesService.addSecurity(req);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_2','ROLE_SADMIN')")
    @RequestMapping(value = "/updateSecurity", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateSecurity (@RequestBody SecuritiesPractice req) {
        securitiesService.updateSecurity(req);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_2','ROLE_SADMIN')")
    @RequestMapping(value = "/deleteSecurity", method = RequestMethod.PUT)
    public ResponseEntity<Void> deleteSecurity (@RequestParam String symbol) {
        securitiesService.deleteSecurity(symbol);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
