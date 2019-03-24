package com.online.stock.controller;

import com.online.stock.dto.request.AfTypeReq;
import com.online.stock.dto.request.SystemVarAddReq;
import com.online.stock.dto.response.AfTypeListRes;
import com.online.stock.model.AfType;
import com.online.stock.model.SysVar;
import com.online.stock.services.IAfTypeService;
import com.online.stock.services.ISystemVarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AFTypeController {
    @Autowired
    private IAfTypeService afTypeService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN_2','ROLE_SADMIN')")
    @RequestMapping(value = "/getAfTypes",method = RequestMethod.GET)
    public ResponseEntity<List<AfType>> getListAfType() {
        List<AfType> afTypeList = afTypeService.getListType();
        return new ResponseEntity<>(afTypeList, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_2','ROLE_SADMIN')")
    @RequestMapping(value = "/addAfType", method = RequestMethod.POST)
    public ResponseEntity<Void> addAfType (@RequestBody AfTypeReq req) {
        afTypeService.addAfType(req);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_2','ROLE_SADMIN')")
    @RequestMapping(value = "/updateAfType", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateAfType (@RequestBody AfTypeReq req) {
        afTypeService.updateAfType(req);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_2','ROLE_SADMIN')")
    @RequestMapping(value = "/deleteAfType", method = RequestMethod.GET)
    public ResponseEntity<Void> deleteAfType (@RequestParam String acType) {
        afTypeService.deleteAfType(acType);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @RequestMapping(value = "/getTypeNames", method = RequestMethod.GET)
    public ResponseEntity<List<AfTypeListRes>> getListTypeName() {
        List<AfTypeListRes> afTypeListRes = new ArrayList<>();
        List<AfType> afTypeList = afTypeService.getListType();
        afTypeList.forEach(afType -> {
            AfTypeListRes res = new AfTypeListRes();
            res.setAcType(afType.getAcType());
            res.setTypeName(afType.getTypeName());
            afTypeListRes.add(res);
        });
        return new ResponseEntity<>(afTypeListRes, HttpStatus.OK);
    }

}
