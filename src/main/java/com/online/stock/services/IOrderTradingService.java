package com.online.stock.services;

import com.online.stock.dto.response.RateInfoRes;

import java.util.List;

public interface IOrderTradingService {
    int checkOrder(String accTno, int quantity, String price, String symbol, String orderType);

    int checkSellOrder(String accTno, int quantity, String price, String symbol, String orderType);

    int checkCloseOder(int pqtty, int poriginalqtty, String poriginalorderi);

    int saveOrder(String orderId, String symbol, String accTno, String action, String orderType,
                  String price, int quantity, String txTime, int txDate, String floor, int loanDates);

    List<RateInfoRes> getRateInfo (String custId);
}
