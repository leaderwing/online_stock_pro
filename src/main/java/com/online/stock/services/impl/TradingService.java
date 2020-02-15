package com.online.stock.services.impl;

import com.online.stock.dto.response.ApproveDepositRes;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
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
        List<ODMast> odMastList = new ArrayList<>();
//        List<ODMast> odMastListFake = new ArrayList<>();
        if (StringUtils.isNotBlank(loggedUsername)) {
            SessionFactory sessionFactory = new Configuration().buildSessionFactory();
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            SQLQuery query = session.createSQLQuery("select odmast.ORDERID,odmast.AFACCTNO,odmast.TXDATE,odmast.CODEID, " +
                    "odmast.EXECTYPE,odmast.PRICETYPE,odmast.ORDERQTTY,odmast.QUOTEPRICE,odmast.EXECQTTY,odmast.OPENPRICE, " +
                    "odmast.ORSTATUS,odmast.CANCELQTTY,odmast.FEEAMT,odmast.FEEAMT1,odmast.TXTIME,odmast.FEEACR,odmast.CLOSEPRICE, " +
                    "odmast.PROFIT,odmast.CLEARDAY,odmast.FLOOR,odmast.CLOSEDQTTY,odmast.REFORDERID,securities_bestprice.BASICPRICE from odmast,securities_bestprice where " +
                    "odmast.AFACCTNO = " + loggedUsername + " AND odmast.TXDATE between " + fromDate + " AND " + toDate + "AND odmast.CODEID = securities_bestprice.SYMBOL AND odmast.ORDERID = odmast.REFORDERID")
                    .addScalar("ORDERID", new StringType())
                    .addScalar("AFACCTNO", new StringType())
                    .addScalar("TXDATE", new IntegerType())
                    .addScalar("CODEID", new StringType())
                    .addScalar("EXECTYPE", new StringType())
                    .addScalar("PRICETYPE", new StringType())
                    .addScalar("ORDERQTTY", new IntegerType())
                    .addScalar("QUOTEPRICE", new IntegerType())
                    .addScalar("EXECQTTY", new IntegerType())
                    .addScalar("OPENPRICE", new IntegerType())
                    .addScalar("ORSTATUS", new StringType())
                    .addScalar("CANCELQTTY", new IntegerType())
                    .addScalar("FEEAMT", new StringType())
                    .addScalar("FEEAMT1", new StringType())
                    .addScalar("TXTIME", new StringType())
                    .addScalar("FEEACR", new IntegerType())
                    .addScalar("CLOSEPRICE", new IntegerType())
                    .addScalar("PROFIT", new IntegerType())
                    .addScalar("CLEARDAY", new IntegerType())
                    .addScalar("FLOOR", new StringType())
                    .addScalar("CLOSEDQTTY", new IntegerType())
                    .addScalar("REFORDERID", new StringType())
                    .addScalar("BASICPRICE", new StringType());
            List<Object[]> rows = query.list();
            for (Object[] row : rows) {
                ODMast res = new ODMast();
                res.setOrderid(row[0] == null ? null :row[0].toString());
                res.setAfacctno(row[1] == null ? null :row[1].toString());
                res.setTxdate(row[2] == null ? 0 :Integer.valueOf(row[2].toString()));
                res.setCodeid(row[3] == null ? null :row[3].toString());
                res.setExectype(row[4] == null ? null : row[4].toString());
                res.setPricetype(row[5] == null ? null : row[5].toString());
                res.setOrderqtty(row[6] == null ? 0 :Integer.valueOf(row[6].toString()));
                res.setQuoteprice(row[7] == null ? 0 :Integer.valueOf(row[7].toString()));
                res.setExecqtty(row[8] == null ? 0 :Integer.valueOf(row[8].toString()));
                res.setOpenprice(row[9] == null ? 0 :Integer.valueOf(row[9].toString()));
                res.setOrstatus(row[10] == null ? null : row[10].toString());
                res.setCancelqtty(row[11] == null ? 0 :Integer.valueOf(row[11].toString()));
                res.setFeeamt(row[12] == null ? null : row[12].toString());
                res.setFeeamt1(row[13] == null ? null : row[13].toString());
                res.setTxtime(row[14] == null ? null : row[14].toString());
                res.setFeeacr(row[15] == null ? null : row[15].toString());
                res.setCloseprice(row[16] == null ?  0 :Integer.valueOf(row[16].toString()));
                res.setProfit(row[17] == null ?  0 :Integer.valueOf(row[17].toString()));
                res.setClearday(row[18] == null ?  0 :Integer.valueOf(row[18].toString()));
                res.setFloor(row[19] == null ? null : row[19].toString());
                res.setClosedqtty(row[20] == null ?  0 :Integer.valueOf(row[20].toString()));
                res.setRefOderId(row[21] == null ? null : row[21].toString());
//                res.setBasicPrice(row[22] == null ?  0 :Float.valueOf(row[22].toString()));
                odMastList.add(res);
            }
//            odMastList =
//                    odMastRepository.findAllByAfacctnoAndExectypeAndTxdateIsLessThanEqualAndTxdateIsGreaterThanEqual(
//                            loggedUsername, "Mua", toDate, fromDate);
        } else {
            odMastList =
                    odMastRepository.findAllByTxdateIsLessThanEqualAndTxdateIsGreaterThanEqual(toDate, fromDate);
        }

//        odMastList =
//                odMastRepository.findAllByAfacctnoAndRefOderIdAndExectypeAndTxdateIsLessThanEqualAndTxdateIsGreaterThanEqual(
//                        loggedUsername,  "Mua", toDate, fromDate);

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
                float curPrice = 0;
                SecuritiesPractice sp = securitiesPracticeList.stream().filter(securitiesPractice -> securitiesPractice.getSymbol().equals(odMast.getCodeid())).findFirst().orElse(null);
                if (sp != null) {
                    basicPrice = Float.parseFloat(sp.getBasicprice());
                    curPrice   = Float.parseFloat(sp.getCurprice());
                }
                BeanUtils.copyProperties(odMast, row);
                row.setBasicPrice(basicPrice);
                row.setCurPrice(curPrice);
                tradingRowList.add(row);
            });
        }
        tradingRowList.sort(new Comparator<TradingRow>() {
            @Override
            public int compare(TradingRow o1, TradingRow o2) {
                if (o2.getTxdate() == o1.getTxdate())
                {
                    return o2.getCodeid().compareTo(o1.getCodeid());
                }else {
                    return o2.getTxdate() - o1.getTxdate();
                }
            }
        });
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
        tradingRowList.sort(new Comparator<TradingRow>() {
            @Override
            public int compare(TradingRow o1, TradingRow o2) {
                if (o2.getTxdate() == o1.getTxdate())
                {
                    return o2.getCodeid().compareTo(o1.getCodeid());
                }else {
                    return o2.getTxdate() - o1.getTxdate();
                }
            }
        });
        tradingRecords.setRowList(tradingRowList);
        return tradingRecords;
    }

    public ODMast getRecordTradingHistory(String loggedUsername, String oderId) {
        TradingRecords tradingRecords = new TradingRecords();
        List<TradingRow> tradingRowList = new ArrayList<>();
        ODMast odMastRecord = new ODMast();
        List<ODMast> odMastList =
                odMastRepository.findAllByAfacctnoAndOrderid(
                        loggedUsername, oderId);
        if (!odMastList.isEmpty()) {
            odMastList.forEach(odMast -> {
                TradingRow row = new TradingRow();
                BeanUtils.copyProperties(odMast, row);
                odMastRecord.setExectype(row.getExectype());
                odMastRecord.setExecqtty(row.getExecqtty());
                odMastRecord.setClosedqtty(row.getClosedqtty());
                odMastRecord.setCodeid(row.getCodeid());
                odMastRecord.setTxdate(row.getTxdate());
                odMastRecord.setFloor(row.getFloor());
            });
        }
//        tradingRecords.setRowList(tradingRowList);
        return odMastRecord;
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

        Output output = call.getOutputs().getCurrent();
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
