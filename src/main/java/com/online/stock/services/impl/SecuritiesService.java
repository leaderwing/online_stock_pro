package com.online.stock.services.impl;

import com.online.stock.dto.request.SystemVarAddReq;
import com.online.stock.model.SecuritiesPractice;
import com.online.stock.model.SysVar;
import com.online.stock.repository.SecuritiesPracticeRepository;
import com.online.stock.repository.SysVarRepository;
import com.online.stock.services.ISecuritiesService;
import com.online.stock.services.ISystemVarService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecuritiesService implements ISecuritiesService {
    @Autowired
    private SecuritiesPracticeRepository securitiesPracticeRepository;
    @Override
    public List<SecuritiesPractice> getListSecurities() {
        List<SecuritiesPractice> securitiesPractices = securitiesPracticeRepository.findAll();
        return  securitiesPractices;
    }

    @Override
    public void addSecurity(SecuritiesPractice req) {
        if ( securitiesPracticeRepository.findBySymbol(req.getSymbol()).size() == 0) {
            securitiesPracticeRepository.save(req);
        }
    }

    @Override
    public void updateSecurity(SecuritiesPractice req) {
        List<SecuritiesPractice> securitiesPractices =
                securitiesPracticeRepository.findBySymbol(req.getSymbol());
        if ( securitiesPractices.size() > 0) {
            securitiesPracticeRepository.save(req);
        }

    }

    @Override
    public void deleteSecurity(String symbol) {
        List<SecuritiesPractice> securitiesPractices =
                securitiesPracticeRepository.findBySymbol(symbol);
        if (securitiesPractices.size() > 0) {
            securitiesPracticeRepository.delete(securitiesPractices.get(0));
        }
    }
}
