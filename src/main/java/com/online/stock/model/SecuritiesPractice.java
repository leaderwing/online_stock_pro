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
    private int basicprice;
    @Column(name = "ceilingprice")
    private int ceilingprice;
    @Column(name = "floorprice")
    private int floorprice;
    @Column(name = "bidprice1")
    private int bidprice1;
    @Column(name = "bqtty1")
    private int bqtty1;
    @Column(name = "askprice1")
    private int askprice1;
    @Column(name = "aqtty1")
    private int aqtty1;
    @Column(name = "lqtty")
    private int lqtty;
}
