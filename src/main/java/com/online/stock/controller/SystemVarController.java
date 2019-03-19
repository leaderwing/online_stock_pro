package com.online.stock.controller;

import com.online.stock.dto.request.SystemVarAddReq;
import com.online.stock.dto.response.SystemVarRes;
import com.online.stock.model.SysVar;
import com.online.stock.services.ISystemVarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SystemVarController {
    @Autowired
    private ISystemVarService systemVarService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN_2','ROLE_SADMIN')")
    @RequestMapping(value = "/getSysVar",method = RequestMethod.GET)
    public ResponseEntity<List<SysVar>> getSystemVar() {
        List<SysVar> sysVarList = systemVarService.getListSysVar();
        return new ResponseEntity<>(sysVarList, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_2','ROLE_SADMIN')")
    @RequestMapping(value = "/addSysVar", method = RequestMethod.POST)
    public ResponseEntity<Void> addSysVar (@RequestBody SystemVarAddReq req) {
        systemVarService.addSysVar(req);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_2','ROLE_SADMIN')")
    @RequestMapping(value = "/updateSysVar", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateSysVar (@RequestBody SystemVarAddReq req) {
        systemVarService.updateSysVar(req);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_2','ROLE_SADMIN')")
    @RequestMapping(value = "/deleteSysVar", method = RequestMethod.PUT)
    public ResponseEntity<Void> deleteSysVar (@RequestParam String GRNAME,
                                              @RequestParam String VARNAME) {
        systemVarService.deleteSysVar(GRNAME, VARNAME);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
