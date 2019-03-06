package com.online.stock.dto.response;

import com.online.stock.model.ODMast;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class TradingRow {
    private String orderid;
    private String txdate;
    private String codeid;
    private String exectype;
    private String pricetype;
    private String orderqtty;
    private String quoteprice;
    private String execqtty;
    private String openprice;
    private String orstatus;
    private String cancelqtty;
    private String feeamt;
    private String txtime;
    private String feeacr;
    private String closeprice;
    private String profit;
    private String clearday;
    private String floor;
    private String closedqtty;
}
