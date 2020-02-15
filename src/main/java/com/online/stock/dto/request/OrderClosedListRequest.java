package com.online.stock.dto.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderClosedListRequest {
    private String custId;
        private List<String> orderList = new ArrayList<>();
//    private String[] orderList;
}
