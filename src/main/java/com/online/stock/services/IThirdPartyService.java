package com.online.stock.services;

import com.online.stock.dto.response.OrderTradingResponse;
import org.json.JSONException;

public interface IThirdPartyService {
    void getAdminAuthen();
    OrderTradingResponse sendOderTrading(String token, String orderType, String price,
                                         Integer quantity, String symbol, String side) throws JSONException;
}
