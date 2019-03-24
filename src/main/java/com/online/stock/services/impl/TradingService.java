package com.online.stock.services.impl;

import com.online.stock.dto.response.ReportTradingRes;
import com.online.stock.dto.response.TradingRecords;
import com.online.stock.dto.response.TradingRow;
import com.online.stock.model.ODMast;
import com.online.stock.model.ODMasthist;
import com.online.stock.model.SecuritiesPractice;
import com.online.stock.repository.ODMastRepository;
import com.online.stock.repository.ODMasthistRepository;
import com.online.stock.repository.SecuritiesPracticeRepository;
import com.online.stock.services.ITradingService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;

@Service
public class TradingService implements ITradingService {
    @Autowired
    private ODMastRepository odMastRepository;
    @Autowired
    private ODMasthistRepository odMasthistRepository;
    @Autowired
    private SecuritiesPracticeRepository securitiesPracticeRepository;
    @Autowired
    private EntityManager entityManager;

    @Override
    public TradingRecords getTradingHistory(String loggedUsername, int fromDate, int toDate,
            String symbol,
            String execType) {
        TradingRecords tradingRecords = new TradingRecords();
        List<TradingRow> tradingRowList = new ArrayList<>();
        List<ODMast> odMastList =
                odMastRepository.findAllByAfacctnoAndTxdateIsLessThanEqualAndTxdateIsGreaterThanEqual(
                        loggedUsername, toDate, fromDate);
        List<SecuritiesPractice> securitiesPracticeList = securitiesPracticeRepository.findAll();
        if (!odMastList.isEmpty()) {
            if (StringUtils.isNotBlank(symbol)) {
                odMastList = odMastList.stream()
                        .filter(odMast -> symbol.equals(odMast.getCodeid()))
                        .collect(Collectors.toList());
            }
            if (StringUtils.isNotBlank(execType)) {
                odMastList = odMastList.stream()
                        .filter(odMast -> execType.equals(odMast.getExectype()))
                        .collect(Collectors.toList());
            }
            odMastList.forEach(odMast -> {
                TradingRow row = new TradingRow();
                float basicPrice = 0;
                SecuritiesPractice sp = securitiesPracticeList.stream().filter(securitiesPractice -> securitiesPractice.getSymbol().equals(odMast.getCodeid())).findFirst().orElse(null);
                if (sp != null) {
                    basicPrice = sp.getBasicprice();
                }
                BeanUtils.copyProperties(odMast, row);
                row.setBasicPrice(basicPrice);
                tradingRowList.add(row);
            });
        }
        tradingRecords.setRowList(tradingRowList);
        return tradingRecords;
    }

    @Override
    public TradingRecords getTradingHistoryHits(String loggedUsername, int fromDate, int toDate,
            String symbol,
            String execType) {
        TradingRecords tradingRecords = new TradingRecords();
        List<TradingRow> tradingRowList = new ArrayList<>();
        List<ODMasthist> odMasthistList =
                odMasthistRepository.findAllByAfacctnoAndTxdateIsLessThanEqualAndTxdateIsGreaterThanEqual(
                        loggedUsername, toDate, fromDate);
        if (!odMasthistList.isEmpty()) {
            if (StringUtils.isNotBlank(symbol)) {
                odMasthistList = odMasthistList.stream()
                        .filter(odMast -> symbol.equals(odMast.getCodeid()))
                        .collect(Collectors.toList());
            }
            if (StringUtils.isNotBlank(execType)) {
                odMasthistList = odMasthistList.stream()
                        .filter(odMast -> execType.equals(odMast.getExectype()))
                        .collect(Collectors.toList());
            }
            odMasthistList.forEach(odMast -> {
                TradingRow row = new TradingRow();
                BeanUtils.copyProperties(odMast, row);
                tradingRowList.add(row);
            });
        }
        tradingRecords.setRowList(tradingRowList);
        return tradingRecords;
    }

    @Override
    public List<ReportTradingRes> getTradingReport(int buyfrom, int buyto, int sellfrom, int sellto, String symbol, String account) {
        List<ReportTradingRes> reportTradingRes = new ArrayList<>();
        Session session = entityManager.unwrap(Session.class);
        ProcedureCall call = session.createStoredProcedureCall("PKG_ORDER_TRADING.PRC_GET_REPORT_TRADING");
        call.registerParameter(1, Integer.class, ParameterMode.IN).bindValue(buyfrom);
        call.registerParameter(2, Integer.class, ParameterMode.IN).bindValue(buyto);
        call.registerParameter(3, Integer.class, ParameterMode.IN).bindValue(sellfrom);
        call.registerParameter(4, Integer.class, ParameterMode.IN).bindValue(sellto);
        call.registerParameter(5, String.class, ParameterMode.IN).bindValue(symbol);
        call.registerParameter(6, String.class, ParameterMode.IN).bindValue(account);
        call.registerParameter(7, Class.class, ParameterMode.REF_CURSOR);

        Output output =  call.getOutputs().getCurrent();
        if (output.isResultSet()) {
            List<Object[]> records = ((ResultSetOutput) output).getResultList();
            for (Object[] row : records) {
                ReportTradingRes res = new ReportTradingRes();
                res.setBuyDate(row[0] == null ? null : row[0].toString());
                res.setSellDate(row[1] == null ? null : row[1].toString());
                res.setSymbol(row[2] == null ? null : row[2].toString());
                res.setQuantity(row[3] == null ? 0 : Integer.valueOf(row[3].toString()));
                res.setBuyPrice(row[4] == null ? 0 : Float.parseFloat((row[4].toString())));
                res.setSellPrice(row[5] == null ? 0 : Float.parseFloat(row[5].toString()));
                res.setSumBuyMoney(row[6] == null ? 0 : Float.parseFloat(row[6].toString()));
                res.setSumSellMoney(row[7] == null ? 0 : Float.parseFloat(row[7].toString()));
                res.setBuyFee(row[8] == null ? 0 : Float.parseFloat(row[8].toString()));
                res.setSellFee(row[9] == null ? 0 : Float.parseFloat(row[9].toString()));
                res.setLoanDay(row[10] == null ? 0 : Integer.valueOf(row[10].toString()));
                res.setLoanFee(row[11] == null ? 0 : Float.parseFloat(row[11].toString()));
                res.setProfit(row[12] == null ? 0 : Float.parseFloat(row[12].toString()));
                res.setRemain(row[13] == null ? null : row[13].toString());
                reportTradingRes.add(res);
            }
        }
        return reportTradingRes;
    }
}
