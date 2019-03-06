package com.online.stock.dto.response;

import lombok.Data;

@Data
public class FloorResponse {
    private String floorCode;
    private String basic;
    private String ceil;
    private String floor;
}
