package com.online.stock.dto.request;

import com.online.stock.dto.SysGroup;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class SystemVarAddReq {
    @NonNull
    private SysGroup grname;
    @NotBlank
    private String varname;
    private String varvalue;
    private String vardesc;
    private String en_vardesc;
}
