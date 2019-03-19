package com.online.stock.controller;

import com.online.stock.dto.request.SellStockRequest;
import com.online.stock.dto.response.OrderTradingResponse;
import com.online.stock.model.MapOrder;
import com.online.stock.repository.MapOrderRepository;
import com.online.stock.services.IOrderTradingService;
import com.online.stock.services.IThirdPartyService;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderTradingController {

    @Autowired
    private IOrderTradingService orderTradingService;
    @Autowired
    private IThirdPartyService thirdPartyService;
    @Autowired
    private MapOrderRepository mapOrderRepository;

    @RequestMapping(value = "/buyNomarl", method = RequestMethod.GET)
    public ResponseEntity<String> buyStock(@RequestParam String floor, @RequestParam int quantity,
                                           @RequestParam float price, @RequestParam String symbol,
                                           @RequestParam String orderType) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        OrderTradingResponse tradingResponse = new OrderTradingResponse();
        int result = 0;
        if (StringUtils.isBlank(loggedUsername)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (StringUtils.isBlank(floor) || price == 0.0 || StringUtils.isBlank(symbol)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        result = orderTradingService.checkOrder(loggedUsername, quantity, price, symbol.toUpperCase(), orderType);
        if (result == 0) {
            // can order
            String vtos_token = System.getProperty("vtos");
            if (StringUtils.isBlank(vtos_token)) {
                //get vtos token
                thirdPartyService.getAdminAuthen();

                vtos_token = System.getProperty("vtos");
                if (StringUtils.isBlank(vtos_token)) {
                    return new ResponseEntity<>("invalid authen token!", HttpStatus.NOT_FOUND);
                }
            }
            try {

                 tradingResponse = thirdPartyService.sendOderTrading(vtos_token, orderType, price
                         , quantity, symbol,"NB");
                if(StringUtils.isNotBlank(tradingResponse.getError())) {
                    return new ResponseEntity<>(tradingResponse.getError(), HttpStatus.BAD_REQUEST);
                }
                tradingResponse.setFloor(floor);
                tradingResponse.setAcctno(loggedUsername);
            }catch (JSONException ex) {
                ex.printStackTrace();
            }
            //save order
            int saveResponse = orderTradingService.saveOrder(tradingResponse.getOrderId(), symbol,
                    loggedUsername,"mua",tradingResponse.getOrderType(),
                    tradingResponse.getPrice(),tradingResponse.getQuantity(),
                    tradingResponse.getTxTime(), tradingResponse.getTxDate(),floor);
            if(saveResponse == 1) {
                //error
                return new ResponseEntity<>("save failure!", HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                return new ResponseEntity<>("Order successfully", HttpStatus.OK);
            }

        } else {
            return new ResponseEntity<>("Invalid Order Checking!", HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = "/sellNomarl", method = RequestMethod.POST)
    public ResponseEntity<String> sellStock(@RequestBody SellStockRequest sellStockRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        if (StringUtils.isBlank(loggedUsername)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        int quantity = sellStockRequest.getExecqtty() - sellStockRequest.getClosedqtty();
        if (sellStockRequest.getPrice() == 0.0
                || StringUtils.isBlank(sellStockRequest.getSymbol())
                || StringUtils.isBlank(sellStockRequest.getOrderType())
                || quantity < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String vtos_token = System.getProperty("vtos");
        if (StringUtils.isBlank(vtos_token)) {
            //get vtos token
            thirdPartyService.getAdminAuthen();
            vtos_token = System.getProperty("vtos");
            if (StringUtils.isBlank(vtos_token)) {
                return new ResponseEntity<>("invalid authen token!", HttpStatus.NOT_FOUND);
            }
        }
        int result = orderTradingService.checkSellOrder(loggedUsername, quantity, sellStockRequest.getPrice(),
                sellStockRequest.getSymbol(), sellStockRequest.getOrderType());
        if (result == 0) {
            OrderTradingResponse tradingResponse  = new OrderTradingResponse();
            try {
                tradingResponse = thirdPartyService.sendOderTrading(vtos_token, sellStockRequest.getOrderType()
                        , sellStockRequest.getPrice(), quantity, sellStockRequest.getSymbol(),
                        "NS");
                if(StringUtils.isNotBlank(tradingResponse.getError())) {
                    return new ResponseEntity<>(tradingResponse.getError(), HttpStatus.BAD_REQUEST);
                }
                tradingResponse.setOderIDM(sellStockRequest.getOderid());
            }catch (JSONException ex) {
                ex.printStackTrace();
            }
            // save sell order
            int saveResponse = orderTradingService.saveOrder(tradingResponse.getOrderId(),
                    sellStockRequest.getSymbol(),
                    loggedUsername,"ban",tradingResponse.getOrderType(),
                    tradingResponse.getPrice(),tradingResponse.getQuantity(),
                    tradingResponse.getTxTime(), tradingResponse.getTxDate(),sellStockRequest.getFloor());
            if(saveResponse == 1) {
                //error
                return new ResponseEntity<>("save failure!", HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                //update table map_order
                MapOrder mapOrder = new MapOrder(tradingResponse.getOderIDM(), tradingResponse.getOrderId());
                mapOrderRepository.save(mapOrder);

                return new ResponseEntity<>("Order successfully", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("Invalid Order Checking!", HttpStatus.BAD_REQUEST);
        }
    }
}
