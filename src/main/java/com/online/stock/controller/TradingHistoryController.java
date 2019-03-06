package com.online.stock.controller;

import com.online.stock.dto.response.TradingRecords;
import com.online.stock.services.ITradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TradingHistoryController {

    @Autowired
    private ITradingService tradingService;
    @RequestMapping(value = "/history",method = RequestMethod.GET)
    public ResponseEntity<TradingRecords> getTradingHistory(@RequestParam String ngay1,
            @RequestParam String ngay2,
            @RequestParam String symbol, @RequestParam String exectype) {
        int fromDate = Integer.parseInt(ngay1);
        int toDate = Integer.parseInt(ngay2);
        TradingRecords tradingRecords =
                tradingService.getTradingHistory(fromDate, toDate, symbol, exectype);
        return new  ResponseEntity<>(tradingRecords,HttpStatus.OK);
    }
}
