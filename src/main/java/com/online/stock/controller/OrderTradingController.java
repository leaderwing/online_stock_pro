package com.online.stock.controller;

import com.online.stock.services.IOrderTradingService;
import com.online.stock.services.IThirdPartyService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderTradingController {

    @Autowired
    private IOrderTradingService orderTradingService;
    @Autowired
    private IThirdPartyService thirdPartyService;

    @RequestMapping(value = "/buyNomarl", method = RequestMethod.GET)
    public ResponseEntity<String> buyStock(@RequestParam String floor, @RequestParam int quantity,
            @RequestParam float price, @RequestParam String symbol,
            @RequestParam String orderType) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        int result = 0;
        if (StringUtils.isBlank(loggedUsername)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if(StringUtils.isBlank(floor) || price == 0.0 || StringUtils.isBlank(symbol)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        result = orderTradingService.checkOrder(loggedUsername, quantity,price, symbol, orderType);
        if(result == 0) {
            // can order
            String vtos_token = System.getProperty("vtos");
            if(StringUtils.isBlank(vtos_token)) {
                //get vtos token
                thirdPartyService.getAdminAuthen();

                vtos_token = System.getProperty("vtos");
                if(StringUtils.isBlank(vtos_token)) {
                    return new ResponseEntity<>("invalid authen token!",HttpStatus.NOT_FOUND);
                }
            }

        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
