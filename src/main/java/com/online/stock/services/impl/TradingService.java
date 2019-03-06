package com.online.stock.services.impl;

import com.online.stock.dto.response.TradingRecords;
import com.online.stock.dto.response.TradingRow;
import com.online.stock.model.ODMast;
import com.online.stock.repository.ODMastRepository;
import com.online.stock.services.ITradingService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradingService implements ITradingService {
    @Autowired
    private ODMastRepository odMastRepository;

    @Override
    public TradingRecords getTradingHistory(String loggedUsername, int fromDate, int toDate,
            String symbol,
            String execType) {
        TradingRecords tradingRecords = new TradingRecords();
        List<TradingRow> tradingRowList = new ArrayList<>();
        List<ODMast> odMastList =
                odMastRepository.findAllByAfacctnoAndTxdateIsLessThanEqualAndTxdateIsGreaterThanEqual(
                        loggedUsername, toDate, fromDate);
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
                BeanUtils.copyProperties(odMast, row);
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
        return null;
    }
}
