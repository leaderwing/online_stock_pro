package com.online.stock.dto.response;

import lombok.Data;

@Data
public class SystemVarRes {
    private String GRNAME;
    private String VARNAME;
    private String VARVALUE;
    private String VARDESC;
    private String EN_VARDESC;
}
