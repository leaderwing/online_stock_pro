package com.online.stock.dto;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequest {
    private String acctno;
    private String fullname;
    private Date dateofbirth;
    private String sex;
    private String idcode;
    private Date iddate;
    private String idplace;
    private String address;
    private String phone;
    private String bankacctno;
    private String bankname;
}
