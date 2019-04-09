package com.online.stock.dto;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank
    private String acctno;
    @NotBlank
    private String fullname;
    private String dateofbirth;
    private String sex;
    @NotBlank
    private String idcode;
    private String iddate;
    private String idplace;
    private String address;
    @NotBlank
    private String phone;
    private String bankacctno;
    private String bankname;
    private String accType;
    private String typeName;
    @NotBlank
    private String email;
    private String pin;
}
