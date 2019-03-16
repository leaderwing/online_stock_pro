package com.online.stock.dto.response;

import lombok.Data;

@Data
public class ApproveDepositRes {
    private String FULLNAME;

    private String CREATED_ACCTNO;

    private String APPROVED_ACCTNO;

    private String APPROVED_TXTIME;

    private Double AMT;

    private  String ACCTNO;

    private int ID;
}
