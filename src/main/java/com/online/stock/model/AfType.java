package com.online.stock.model;

import lombok.Data;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;

@Entity
@Table(name = "AFTYPE")
@Data
public class AfType {
    @Id
    @Column(name = "ACTYPE", unique = true, nullable = false)
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
