package com.online.stock.dto.request;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
public class AfTypeReq {
    private String acType;

    private String typeName;

    private String afType;

    private String approveCd;

    private int floorLimit;

    private int branchLimit;

    private int teleLimit;

    private int onlineLimit;

    private float tradeRate;

    private float minBal;

    private float margRate;

    private float WarningRate;

    private float processRate;

    private float depoRate;

    private float MiscRate;

}
