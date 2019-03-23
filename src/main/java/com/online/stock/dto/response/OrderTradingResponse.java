package com.online.stock.dto.response;

import lombok.Data;

@Data
public class OrderTradingResponse {
    private String floor;
    private String acctno;
    private String orderId;
    private String symbol;
    private String orderType;
    private int quantity;
    private String txTime;
    private double price;
    private int txDate;
    private String error;
    private String oderIDM;
}
