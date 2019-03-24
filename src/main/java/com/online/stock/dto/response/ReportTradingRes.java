package com.online.stock.dto.response;

import lombok.Data;

@Data
public class ReportTradingRes {
    private String sellDate;
    private String buyDate;
    private String symbol;
    private int quantity;
    private float buyPrice;
    private float sellPrice;
    private float sumBuyMoney;
    private float sumSellMoney;
    private float buyFee;
    private float sellFee;
    private int loanDay;
    private float loanFee;
    private float profit;
    private String remain;
}
