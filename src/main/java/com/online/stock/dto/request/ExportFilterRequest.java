package com.online.stock.dto.request;

import lombok.Data;

@Data
public class ExportFilterRequest {
    private String fromBuyDate;

    private String toBuyDate;

    private String fromSellDate;

    private String toSellDate;

    private String symbol;

    private String account;
}
