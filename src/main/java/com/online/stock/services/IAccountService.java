package com.online.stock.services;

import com.online.stock.dto.RegisterRequest;
import com.online.stock.dto.response.AccountInfoRes;

import java.util.List;

public interface IAccountService {
    void changePassword(String username, String newPassword);
    String register(RegisterRequest registerRequest, String password);

    List<AccountInfoRes> getAccountList();

    RegisterRequest getUserInfo (String custId);

    String updateUserInfo (RegisterRequest request);

    String getCustId (String branchCode);
}
