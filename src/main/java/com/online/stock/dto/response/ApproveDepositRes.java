package com.online.stock.dto.response;

import lombok.Data;

@Data
public class ApproveDepositRes {
    private String fullname;

    private String created_acctno;

    private String approved_acctno;

    private String approved_txtime;

    private Double amt;

    private  String acctno;

    private int id;
}
