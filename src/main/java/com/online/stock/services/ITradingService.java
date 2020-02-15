package com.online.stock.services;

import com.online.stock.dto.response.ReportTradingRes;
import com.online.stock.dto.response.TradingRecords;
import com.online.stock.model.ODMast;

import java.util.List;

public interface ITradingService {
    TradingRecords getTradingHistory(String loggedUsername, int fromDate, int toDate, String symbol, String execType);

    TradingRecords getTradingHistoryHits(String loggedUsername, int fromDate, int toDate, String symbol, String execType);

    ODMast getRecordTradingHistory(String loggedUsername, String oderId);

    List<ReportTradingRes> getTradingReport(int buyfrom, int buyto, int sellfrom, int sellto, String symbol, String account);
}
