package com.online.stock.dto.response;

import lombok.Data;

@Data
public class CommonInfoRes {
    private String afacctno;

    private long tai_san_rong;

    private float ty_le_ky_quy;

    private long suc_mua;

    private long du_no_thuc_te;
}
