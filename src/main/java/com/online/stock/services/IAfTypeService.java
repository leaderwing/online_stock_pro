package com.online.stock.services;

import com.online.stock.dto.request.AfTypeReq;
import com.online.stock.dto.request.SystemVarAddReq;
import com.online.stock.model.AfType;
import com.online.stock.model.SysVar;

import java.util.List;

public interface IAfTypeService {
    List<AfType> getListType();
    void addAfType(AfTypeReq req);
    void updateAfType(AfTypeReq req);
    void deleteAfType(String afTypeId);

}
