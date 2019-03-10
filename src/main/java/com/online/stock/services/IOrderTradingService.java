package com.online.stock.services;

public interface IOrderTradingService {
    int checkOrder(String accTno, int quantity, float price, String symbol, String orderType);

    int checkSellOrder(String accTno, int quantity, float price, String symbol, String orderType);

    int saveOrder(String orderId, String symbol, String accTno, String action, String orderType,
                  float price, int quantity, String txTime, int txDate, String floor);
}
