package com.online.stock.dto.response;

import com.online.stock.model.ODMast;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class TradingRow {
    private String orderid;

    private String afacctno;

    private int txdate;

    private String codeid;

    private String exectype;

    private String pricetype;

    private int orderqtty;

    private int quoteprice;

    private int execqtty;

    private int openprice;

    private String orstatus;

    private int cancelqtty;

    private String feeamt;

    private String feeamt1;

    private String txtime;

    private int feeacr;

    private int closeprice;

    private int profit;

    private int clearday;

    private String floor;

    private int closedqtty;

    private String refOderId;

    private float basicPrice;

    private float curPrice;

    private int loanDates;

}
