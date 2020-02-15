package com.online.stock.dto.response;

import com.online.stock.model.VGeneralInfo;
import lombok.Data;

@Data
public class HandleAccResponse {
    private String CUSTID;
    private String REAL_MARGRATE;
    private String TSR;
    private  String TEMPPROFIT;
}
