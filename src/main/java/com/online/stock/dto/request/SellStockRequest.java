package com.online.stock.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

@Data
public class SellStockRequest {
    private int execqtty;
    private int quantity;
    private String idcusid;
    private int closedqtty;
    @NotBlank
    private String oderid;
    private String price;
    @NotBlank
    private String symbol;
    @NotBlank
    private String orderType;
    @NotBlank
    private String floor;
    private int buyDate;
}
