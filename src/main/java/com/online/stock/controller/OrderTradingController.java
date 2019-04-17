package com.online.stock.controller;

import com.online.stock.dto.request.SellStockRequest;
import com.online.stock.dto.response.CommonInfoRes;
import com.online.stock.dto.response.OrderTradingResponse;
import com.online.stock.dto.response.RateInfoRes;
import com.online.stock.model.MapOrder;
import com.online.stock.model.VGeneralInfo;
import com.online.stock.repository.MapOrderRepository;
import com.online.stock.repository.VGeneralInfoRepository;
import com.online.stock.services.IOrderTradingService;
import com.online.stock.services.IThirdPartyService;
import com.online.stock.services.impl.OrderTradingService;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OrderTradingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderTradingController.class);

    @Autowired
    private IOrderTradingService orderTradingService;
    @Autowired
    private IThirdPartyService thirdPartyService;
    @Autowired
    private MapOrderRepository mapOrderRepository;
    @Autowired
    private VGeneralInfoRepository vGeneralInfoRepository;

    @RequestMapping(value = "/buyNomarl", method = RequestMethod.GET)
    public ResponseEntity<String> buyStock(@RequestParam String floor, @RequestParam int quantity,
                                           @RequestParam double price, @RequestParam String symbol,
                                           @RequestParam String orderType) throws JSONException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        JSONObject jsonObject = new JSONObject();
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
                    jsonObject.put("result", "invalid authen token!");
                    return new ResponseEntity<>(jsonObject.toString(), HttpStatus.NOT_FOUND);
                }
            }
            try {

                 tradingResponse = thirdPartyService.sendOderTrading(vtos_token, orderType, price
                         , quantity, symbol.toUpperCase(),"NB");
                if(StringUtils.isNotBlank(tradingResponse.getError())) {
                    jsonObject.put("result", tradingResponse.getError());
                    return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
                }
                tradingResponse.setFloor(floor);
                tradingResponse.setAcctno(loggedUsername);
                LOGGER.debug(tradingResponse.toString());
            }catch (Exception ex) {
                ex.printStackTrace();
                jsonObject.put("result", "Dữ liệu truyền vào không hợp lệ, vui lòng thử lại!");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            //save order
            int saveResponse = orderTradingService.saveOrder(tradingResponse.getOrderId(), symbol.toUpperCase(),
                    loggedUsername,"Mua",tradingResponse.getOrderType(),
                    tradingResponse.getPrice(),tradingResponse.getQuantity(),
                    tradingResponse.getTxTime(), tradingResponse.getTxDate(),floor);
            if(saveResponse == 1) {
                //error
                jsonObject.put("result", "save failure!");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                jsonObject.put("result", "Order successfully");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
            }

        } else {
            jsonObject.put("result", "Invalid Order Checking!");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = "/sellNomarl", method = RequestMethod.POST)
    public ResponseEntity<String> sellStock(@RequestBody SellStockRequest sellStockRequest) throws JSONException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        if (StringUtils.isBlank(loggedUsername)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        JSONObject jsonObject = new JSONObject();
        int quantity = sellStockRequest.getExecqtty() - sellStockRequest.getClosedqtty();
        if (StringUtils.isBlank(sellStockRequest.getSymbol())
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
                jsonObject.put("result", "invalid authen token!");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.NOT_FOUND);
            }
        }
        int result = orderTradingService.checkSellOrder(loggedUsername, quantity, sellStockRequest.getPrice(),
                sellStockRequest.getSymbol().toUpperCase(), sellStockRequest.getOrderType());
        if (result == 0) {
            OrderTradingResponse tradingResponse  = new OrderTradingResponse();
            try {
                tradingResponse = thirdPartyService.sendOderTrading(vtos_token, sellStockRequest.getOrderType()
                        , sellStockRequest.getPrice(), quantity, sellStockRequest.getSymbol().toUpperCase(),
                        "NS");
                if(StringUtils.isNotBlank(tradingResponse.getError())) {
                    jsonObject.put("result", tradingResponse.getError());
                    return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
                }
                tradingResponse.setOderIDM(sellStockRequest.getOderid());
            }catch (JSONException ex) {
                ex.printStackTrace();
            }
            // save sell order
            int saveResponse = orderTradingService.saveOrder(tradingResponse.getOrderId(),
                    sellStockRequest.getSymbol().toUpperCase(),
                    loggedUsername,"Ban",tradingResponse.getOrderType(),
                    tradingResponse.getPrice(),tradingResponse.getQuantity(),
                    tradingResponse.getTxTime(), tradingResponse.getTxDate(),sellStockRequest.getFloor());
            if(saveResponse == 1) {
                //error
                jsonObject.put("result", "save failure!");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                //update table map_order
                MapOrder mapOrder = new MapOrder(tradingResponse.getOderIDM(), tradingResponse.getOrderId());
                mapOrderRepository.save(mapOrder);
                jsonObject.put("result", "Order successfully");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
            }
        } else {
            jsonObject.put("result", "Invalid Order Checking!");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = "/ttchung", method = RequestMethod.GET)
    public  ResponseEntity<List<CommonInfoRes>> getCommonInfo () {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        if (StringUtils.isBlank(loggedUsername)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<CommonInfoRes> commonInfoRes = new ArrayList<>();

        List<VGeneralInfo> vGeneralInfos = vGeneralInfoRepository.findByCustId(loggedUsername);
        if (vGeneralInfos.size() > 0) {
            vGeneralInfos.forEach(vGeneralInfo -> {
                CommonInfoRes commonInfoRes1 = new CommonInfoRes();
                commonInfoRes1.setTai_san_rong(vGeneralInfo.getTsr());
                commonInfoRes1.setSuc_mua(vGeneralInfo.getBitMax());
                commonInfoRes1.setTy_le_ky_quy(vGeneralInfo.getRealMargrate());
                commonInfoRes1.setDu_no_thuc_te(vGeneralInfo.getTotalLoad());
                commonInfoRes.add(commonInfoRes1);
            });
        }
        return new ResponseEntity<>(commonInfoRes,HttpStatus.OK);
    }
    @RequestMapping(value = "/tttyle", method = RequestMethod.GET)
    public ResponseEntity<List<RateInfoRes>> getRateInfo () {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        if (StringUtils.isBlank(loggedUsername)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<RateInfoRes> rateInfoRes = new ArrayList<>();
        rateInfoRes = orderTradingService.getRateInfo(loggedUsername);
        return new ResponseEntity<>(rateInfoRes, HttpStatus.OK);
    }

}
