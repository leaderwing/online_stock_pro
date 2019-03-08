package com.online.stock.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderRequest {
    private boolean forcedSell;
    private String orderType;
    private float price;
    private int quantity;
    private String side;
    private String symbol;
    private String term;
}
