package com.online.stock.services.impl;

import com.online.stock.dto.UsedQuantity;
import com.online.stock.dto.request.SystemVarAddReq;
import com.online.stock.dto.response.TradingRow;
import com.online.stock.model.SecuritiesPractice;
import com.online.stock.model.SysVar;
import com.online.stock.repository.SecuritiesPracticeRepository;
import com.online.stock.repository.SysVarRepository;
import com.online.stock.services.ISecuritiesService;
import com.online.stock.services.ISystemVarService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SecuritiesService implements ISecuritiesService {
    @Autowired
    private SecuritiesPracticeRepository securitiesPracticeRepository;
    @Override
    public List<SecuritiesPractice> getListSecurities() {
        List<SecuritiesPractice> securitiesPractices = securitiesPracticeRepository.findAll();
        Map<String, Integer> map = new HashMap<>();
        SecuritiesPractice row = new SecuritiesPractice();
        SessionFactory sessionFactory = new Configuration().buildSessionFactory();
        Session session = sessionFactory.openSession();
        SQLQuery query = session.createSQLQuery(
                "SELECT CODEID, SUM (EXECQTTY-CLOSEDQTTY) as SUM from ODMAST group by CODEID")
                .addScalar("CODEID",  new StringType())
                .addScalar("SUM", new StringType());
        List<Object[]> rows = query.list();
        for (Object [] obj : rows) {
            map.put(obj[0].toString(), Integer.parseInt(obj[1].toString()));
        }
            securitiesPractices.forEach(securitiesPractice -> {
                if (map.containsKey(securitiesPractice.getSymbol()))
                 {
                   securitiesPractice.setUsedqtty(map.get(securitiesPractice.getSymbol()));
                }
        });
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
