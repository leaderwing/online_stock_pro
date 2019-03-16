package com.online.stock.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class TransferMoneyRequest {
    @NotBlank
    private String account;
    private double amount;
    private String txdesc;
}
