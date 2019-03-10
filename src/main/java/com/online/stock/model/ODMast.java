package com.online.stock.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ODMAST")
@Data
public class ODMast {
    @Id
    @Column(name = "ORDERID")
    private String orderid;
    @Column(name = "AFACCTNO")
    private String afacctno;
    @Column(name = "TXDATE")
    private int txdate;
    @Column(name = "CODEID")
    private String codeid;
    @Column(name = "EXECTYPE")
    private String exectype;
    @Column(name = "PRICETYPE")
    private String pricetype;
    @Column(name = "ORDERQTTY")
    private int orderqtty;
    @Column(name = "QUOTEPRICE")
    private int quoteprice;
    @Column(name = "EXECQTTY")
    private int execqtty;
    @Column(name = "OPENPRICE")
    private int openprice;
    @Column(name = "ORSTATUS")
    private String orstatus;
    @Column(name = "CANCELQTTY")
    private int cancelqtty;
    @Column(name = "FEEAMT")
    private String feeamt;
    @Column(name = "TXTIME")
    private String txtime;
    @Column(name = "FEEACR")
    private int feeacr;
    @Column(name = "CLOSEPRICE")
    private int closeprice;
    @Column(name = "PROFIT")
    private int profit;
    @Column(name = "CLEARDAY")
    private int clearday;
    @Column(name = "FLOOR")
    private String floor;
    @Column(name = "CLOSEDQTTY")
    private int closedqtty;
    @Column(name = "REFORDERID")
    private String refOderId;
}
