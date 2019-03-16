package com.online.stock.dto.request;

import com.online.stock.dto.SysGroup;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class SystemVarAddReq {
    @NonNull
    private SysGroup GRNAME;
    @NotBlank
    private String VARNAME;
    private String VARVALUE;
    private String VARDESC;
    private String EN_VARDESC;
}
