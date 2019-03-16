package com.online.stock.services;

import com.online.stock.dto.SysGroup;
import com.online.stock.dto.request.SystemVarAddReq;
import com.online.stock.dto.response.SystemVarRes;
import com.online.stock.model.SysVar;

import java.util.List;

public interface ISystemVarService {
    List<SysVar> getListSysVar();
    void addSysVar (SystemVarAddReq req);
    void updateSysVar (SystemVarAddReq req);
    void deleteSysVar (String sysGroup, String varName);

}
