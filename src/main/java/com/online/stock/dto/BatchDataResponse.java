package com.online.stock.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BatchDataResponse implements Serializable {
    private String orderId;
    private int qtty;
    private String txtime;
    private int total_qtty;
    private int remain_qtty;
    private double price;
    private double avg_price;
    private int type;
    private String reqOrderId;

    @Override
    public String toString() {
        return orderId + '|' + type + '|' + qtty + '|' + price + '|' + txtime + '|' + total_qtty + '|' + avg_price + '|' + remain_qtty;
    }
}
