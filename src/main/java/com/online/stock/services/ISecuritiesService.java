package com.online.stock.services;

import com.online.stock.dto.request.SystemVarAddReq;
import com.online.stock.model.SecuritiesPractice;
import com.online.stock.model.SysVar;

import java.util.List;

public interface ISecuritiesService {
    List<SecuritiesPractice> getListSecurities();
    void addSecurity(SecuritiesPractice req);
    void updateSecurity(SecuritiesPractice req);
    void deleteSecurity(String symbol);

}
