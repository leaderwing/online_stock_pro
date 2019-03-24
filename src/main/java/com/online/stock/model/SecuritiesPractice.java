package com.online.stock.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SECURITIES_BESTPRICE")
@Data
public class SecuritiesPractice {

    @Id
    @Column(name = "symbol")
    @NotBlank
    private String symbol;
    @Column(name = "txdate")
    private int txdate;
    @Column(name = "txtime")
    private String txtime;
    @Column(name = "basicprice")
    private float basicprice;
    @Column(name = "ceilingprice")
    private float ceilingprice;
    @Column(name = "floorprice")
    private float floorprice;
    @Column(name = "bidprice1")
    private float bidprice1;
    @Column(name = "bqtty1")
    private float bqtty1;
    @Column(name = "askprice1")
    private float askprice1;
    @Column(name = "aqtty1")
    private float aqtty1;
    @Column(name = "lqtty")
    private float lqtty;
}
