package com.online.stock.services.impl;

import com.online.stock.dto.request.AfTypeReq;
import com.online.stock.model.AfType;
import com.online.stock.model.SysVar;
import com.online.stock.repository.AfTypeRepository;
import com.online.stock.services.IAfTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AfTypeService implements IAfTypeService {
    @Autowired
    private AfTypeRepository afTypeRepository;
    @Override
    public List<AfType> getListType() {
        List<AfType> sysVarList = afTypeRepository.findAll();
        return  sysVarList;
    }

    @Override
    public void addAfType(AfTypeReq req) {
        AfType afType = new AfType();
        BeanUtils.copyProperties(req, afType);
        afTypeRepository.save(afType);
    }

    @Override
    public void updateAfType(AfTypeReq req) {
        AfType afType = afTypeRepository.findByAcType(req.getAcType());
        if (afType != null) {
            BeanUtils.copyProperties(req, afType);
            afTypeRepository.save(afType);
        }
    }

    @Override
    public void deleteAfType(String afTypeId) {
        AfType afType = afTypeRepository.findByAcType(afTypeId);
        if (afType != null) {
          afTypeRepository.delete(afType);
        }
    }
}
