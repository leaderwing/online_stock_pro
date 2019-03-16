package com.online.stock.dto.response;

import lombok.Data;

@Data
public class SystemVarRes {
    private String grname;
    private String varname;
    private String varvalue;
    private String vardesc;
    private String en_vardesc;
}
