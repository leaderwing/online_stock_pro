package com.online.stock.services;

public interface IOrderTradingService {
    int checkOrder(String accTno,int quantity,float price,String symbol,String orderType);
}
