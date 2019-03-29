package com.online.stock.dto.response;

import lombok.Data;

@Data
public class RateInfoRes {
    private String custId;
    private String codeid;
    private double so_du;
    private double t2;
    private double t1;
    private double t0;

}
