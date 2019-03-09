package com.online.stock.services;

import com.online.stock.dto.RegisterRequest;

public interface IAccountService {
    void changePassword(String username, String newPassword);
    String register(RegisterRequest registerRequest);
}
