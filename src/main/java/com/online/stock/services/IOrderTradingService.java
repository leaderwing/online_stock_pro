package com.online.stock.services;

import com.online.stock.dto.response.RateInfoRes;

import java.util.List;

public interface IOrderTradingService {
    int checkOrder(String accTno, int quantity, double price, String symbol, String orderType);

    int checkSellOrder(String accTno, int quantity, double price, String symbol, String orderType);

    int saveOrder(String orderId, String symbol, String accTno, String action, String orderType,
                  double price, int quantity, String txTime, int txDate, String floor);

    List<RateInfoRes> getRateInfo (String custId);
}
