package com.online.stock.services;

import com.online.stock.dto.response.TradingRecords;

public interface ITradingService {
    TradingRecords getTradingHistory(String loggedUsername, int fromDate, int toDate, String symbol, String execType);

    TradingRecords getTradingHistoryHits(String loggedUsername, int fromDate, int toDate, String symbol, String execType);
}
