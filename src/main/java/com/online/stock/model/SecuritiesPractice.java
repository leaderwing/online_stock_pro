package com.online.stock.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@Entity
@Table(name = "SECURITIES_BESTPRICE")
@Data
public class SecuritiesPractice {

    @Id
    @Column(name = "symbol")
    @NotBlank
    private String symbol;
    @Column(name = "txdate")
    private String txdate;
    @Column(name = "txtime")
    private String txtime;
    @Column(name = "basicprice")
    private String basicprice;
    @Column(name = "ceilingprice")
    private String ceilingprice;
    @Column(name = "floorprice")
    private String floorprice;
    @Column(name = "bidprice1")
    private String bidprice1;
    @Column(name = "bqtty1")
    private String bqtty1;
    @Column(name = "askprice1")
    private String askprice1;
    @Column(name = "aqtty1")
    private String aqtty1;
    @Column(name = "lqtty")
    private String lqtty;
    @Column(name = "curprice")
    private String curprice;
    @Transient
    private Integer usedqtty = 0;
}
