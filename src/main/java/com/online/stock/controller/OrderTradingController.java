package com.online.stock.controller;

import com.online.stock.dto.request.OrderClosedListRequest;
import com.online.stock.dto.request.SellStockRequest;
import com.online.stock.dto.response.CommonInfoRes;
import com.online.stock.dto.response.OrderTradingResponse;
import com.online.stock.dto.response.RateInfoRes;
import com.online.stock.model.MapOrder;
import com.online.stock.model.ODMast;
import com.online.stock.model.SecuritiesPractice;
import com.online.stock.model.VGeneralInfo;
import com.online.stock.repository.MapOrderRepository;
import com.online.stock.repository.ODMastRepository;
import com.online.stock.repository.SecuritiesPracticeRepository;
import com.online.stock.repository.VGeneralInfoRepository;
import com.online.stock.services.IOrderTradingService;
import com.online.stock.services.IThirdPartyService;
import com.online.stock.services.impl.OrderTradingService;
import com.online.stock.utils.DateUtils;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class OrderTradingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderTradingController.class);

    @Autowired
    private IOrderTradingService orderTradingService;
    @Autowired
    private SecuritiesPracticeRepository securitiesPracticeRepository;
    @Autowired
    private IThirdPartyService thirdPartyService;
    @Autowired
    private MapOrderRepository mapOrderRepository;
    @Autowired
    private VGeneralInfoRepository vGeneralInfoRepository;
    @Autowired
    private ODMastRepository odMastRepository;

    @RequestMapping(value = "/buyNomarl", method = RequestMethod.GET)
    public ResponseEntity<String> buyStock(@RequestParam String floor, @RequestParam int quantity,
                                           @RequestParam String price, @RequestParam String symbol,
                                           @RequestParam String orderType) throws JSONException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        JSONObject jsonObject = new JSONObject();
        OrderTradingResponse tradingResponse = new OrderTradingResponse();
        int result = 0;
        if (StringUtils.isBlank(loggedUsername)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (StringUtils.isBlank(floor) || price == "0.0" || StringUtils.isBlank(symbol)) {
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
                        , quantity, symbol.toUpperCase(), "NB");
                if (StringUtils.isNotBlank(tradingResponse.getError())) {
                    jsonObject.put("result", tradingResponse.getError());
                    return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
                }
                tradingResponse.setFloor(floor);
                tradingResponse.setAcctno(loggedUsername);
                LOGGER.debug(tradingResponse.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
                jsonObject.put("result", "Dữ liệu truyền vào không hợp lệ, vui lòng thử lại!");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            //save order
            int saveResponse = orderTradingService.saveOrder(tradingResponse.getOrderId(), symbol.toUpperCase(),
                    loggedUsername, "Mua", tradingResponse.getOrderType(),
                    tradingResponse.getPrice(), tradingResponse.getQuantity(),
                    tradingResponse.getTxTime(), tradingResponse.getTxDate(), floor, 0);
            if (saveResponse == 1) {
                //error
                jsonObject.put("result", "save failure!");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                jsonObject.put("result", "Order successfully");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
            }

        } else if (result == 1) {
            jsonObject.put("result", "Không đủ tiền mua!");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        } else if (result == 2) {
            jsonObject.put("result", "Không cho phép mua chứng khoán trong này!");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        } else if (result == 3) {
            jsonObject.put("result", "Không cho phép giao dịch nhiều hon khối lượng cho phép!");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        } else if (result == 4) {
            jsonObject.put("result", "Không tồn tại!");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        } else {
            jsonObject.put("result", "Invalid Order Checking!");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/sellNomarl", method = RequestMethod.POST)
    public ResponseEntity<String> sellStock(@RequestBody SellStockRequest sellStockRequest) throws JSONException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername;
        if (sellStockRequest.getIdcusid() != null && sellStockRequest.getIdcusid() != "undefined") {
            loggedUsername = sellStockRequest.getIdcusid();
        } else {
            loggedUsername = auth.getName();
        }
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
        int resultClose = orderTradingService.checkCloseOder(sellStockRequest.getQuantity(), sellStockRequest.getExecqtty(),
                sellStockRequest.getOderid());
        if (resultClose == 0) {
            int result = orderTradingService.checkSellOrder(loggedUsername, sellStockRequest.getQuantity(), sellStockRequest.getPrice(),
                    sellStockRequest.getSymbol().toUpperCase(), sellStockRequest.getOrderType());

            if (result == 0) {
                OrderTradingResponse tradingResponse = new OrderTradingResponse();
                try {
                    tradingResponse = thirdPartyService.sendOderTrading(vtos_token, sellStockRequest.getOrderType()
                            , sellStockRequest.getPrice(), sellStockRequest.getQuantity(), sellStockRequest.getSymbol().toUpperCase(),
                            "NS");
                    if (StringUtils.isNotBlank(tradingResponse.getError())) {
                        jsonObject.put("result", tradingResponse.getError());
                        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
                    }
                    tradingResponse.setOderIDM(sellStockRequest.getOderid());
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                // save sell order
                //get loan date number
                int loanDates = Math.max(0, tradingResponse.getTxDate() - sellStockRequest.getBuyDate());
                int saveResponse = orderTradingService.saveOrder(tradingResponse.getOrderId(),
                        sellStockRequest.getSymbol().toUpperCase(),
                        loggedUsername, "Ban", tradingResponse.getOrderType(),
                        tradingResponse.getPrice(), tradingResponse.getQuantity(),
                        tradingResponse.getTxTime(), tradingResponse.getTxDate(), sellStockRequest.getFloor(), loanDates);
                if (saveResponse == 1) {
                    //error
                    jsonObject.put("result", "save failure!");
                    return new ResponseEntity<>(jsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
                } else {
                    ODMast odMast = odMastRepository.findFirstByOrderid(sellStockRequest.getOderid());
                    odMast.setRefOderId(sellStockRequest.getOderid());
                    odMastRepository.save(odMast);
                    //update table map_order
                    MapOrder mapOrder = new MapOrder(tradingResponse.getOderIDM(), tradingResponse.getOrderId());
                    mapOrderRepository.save(mapOrder);
                    jsonObject.put("result", "Order successfully");
                    return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
                }
            } else if (result == 1) {
                jsonObject.put("result", "Không đủ tiền bán!");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
            } else if (result == 2) {
                jsonObject.put("result", "Không cho phép bán chứng khoán trong này!");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
            } else if (result == 3) {
                jsonObject.put("result", "Không cho phép giao dịch nhiều hon khối lượng cho phép!");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
            } else if (result == 4) {
                jsonObject.put("result", "Không tồn tại!");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
            } else {
                jsonObject.put("result", "Invalid Order Checking!");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
            }
        } else {
            jsonObject.put("result", "Đã tồn tại lệnh đóng");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/ttchung", method = RequestMethod.GET)
    public ResponseEntity<List<CommonInfoRes>> getCommonInfo() {
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
                commonInfoRes1.setLai_lo_tam_tinh(vGeneralInfo.getTempprofit());
                commonInfoRes.add(commonInfoRes1);
            });
        }
        return new ResponseEntity<>(commonInfoRes, HttpStatus.OK);
    }

    @RequestMapping(value = "/tttyle", method = RequestMethod.GET)
    public ResponseEntity<List<RateInfoRes>> getRateInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        if (StringUtils.isBlank(loggedUsername)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<RateInfoRes> rateInfoRes = new ArrayList<>();
        rateInfoRes = orderTradingService.getRateInfo(loggedUsername);
        return new ResponseEntity<>(rateInfoRes, HttpStatus.OK);
    }

    @RequestMapping(value = "/order/closeAll", method = RequestMethod.PUT)
    public ResponseEntity<String> closeAllOrders(@RequestBody OrderClosedListRequest request) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        HashMap<String, String> errors = new HashMap<String, String>();
        List<String> orderIdList = request.getOrderList();
        //9h - 9h15 ato
        //9h15 - 14h30 :mp
        //14h30 - 14h45 atc
        String orderType = "LO";
//        Date date = new Date();   // given date
//        Calendar calendar = GregorianCalendar.getInstance();
//        calendar.setTime(date);
//        int hours = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
//        int minutes = calendar.get(Calendar.MINUTE);;
//        int time = hours * 60 + minutes;
//        if ( time < 9*60 || time > (14*60+45))
//        {
//            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }else if (time >= 9*60 && time <= (9*60 + 15)) {
//            orderType = "ATO";
//        }else  if (time > (9*60+15) && time <= (14*60+30)) {
//            orderType = "MP";
//        }else if (time > (14*60 + 30) && time <= (14*60 + 45)) {
//            orderType = "ATC";
//        }
        String vtos_token = System.getProperty("vtos");
        if (StringUtils.isBlank(vtos_token)) {
            //get vtos token
            thirdPartyService.getAdminAuthen();
            vtos_token = System.getProperty("vtos");
            if (StringUtils.isBlank(vtos_token)) {
                jsonObject.put("result", "Bạn cần đăng nhập lại");
                return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
            }
        }
        String finalVtos_token = vtos_token;
        String finalOrderType = orderType;

        orderIdList.forEach(s -> {
            String type = "";
            List<SecuritiesPractice> securitiesPracticeList = securitiesPracticeRepository.findAll();
            ODMast odMast = odMastRepository.findFirstByOrderid(s);
            int resultClose = orderTradingService.checkCloseOder(odMast.getOrderqtty(), odMast.getExecqtty(),
                    s);
            if (resultClose == 0) {
                if (odMast != null) {
                    float floorPrice = 0;
                    float ceilingPrice = 0;
                    int result;
                    SecuritiesPractice sp = securitiesPracticeList.stream().filter(securitiesPractice -> securitiesPractice.getSymbol().equals(odMast.getCodeid())).findFirst().orElse(null);
                    if (sp != null) {
                        floorPrice = Float.parseFloat(sp.getFloorprice());
                        ceilingPrice = Float.parseFloat(sp.getCeilingprice());
                    }
                    int quantity = odMast.getOrderqtty();
                    if (odMast.getExectype().equals("Mua")) {
                        type = "NS";
                        result = orderTradingService.checkSellOrder(request.getCustId(), quantity, String.valueOf(floorPrice),
                                odMast.getCodeid().toUpperCase(), odMast.getExectype());
                    } else {
                        type = "NB";
                        result = orderTradingService.checkSellOrder(request.getCustId(), quantity, String.valueOf(ceilingPrice),
                                odMast.getCodeid().toUpperCase(), odMast.getExectype());
                    }


                    if (result == 0) {

                        OrderTradingResponse tradingResponse = new OrderTradingResponse();
                        try {
                            if (type.equals("NS")) {
                                tradingResponse = thirdPartyService.sendOderTrading(finalVtos_token, finalOrderType
                                        , String.valueOf(floorPrice), quantity, odMast.getCodeid().toUpperCase(),
                                        type);
                                if (StringUtils.isNotBlank(tradingResponse.getError())) {
                                    errors.put("SUCCESSSELL" + s +"Chứng khoán" + odMast.getCodeid().toUpperCase() + "Khối lượng" + quantity + "Giá" + String.valueOf(floorPrice), tradingResponse.getError());
                                    return;
                                }
                                tradingResponse.setOderIDM(s);
                            }
                            if (type.equals("NB")) {
                                tradingResponse = thirdPartyService.sendOderTrading(finalVtos_token, finalOrderType
                                        , String.valueOf(ceilingPrice), quantity, odMast.getCodeid().toUpperCase(),
                                        type);
                                if (StringUtils.isNotBlank(tradingResponse.getError())) {

                                    errors.put("SUCCESSBUY" + s +"Chứng khoán" + odMast.getCodeid().toUpperCase() + "Khối lượng" + quantity + "Giá" + String.valueOf(ceilingPrice), tradingResponse.getError());
                                    return;
                                }
                                tradingResponse.setOderIDM(s);
                            }

                        } catch (JSONException ex) {
                            errors.put(s, "Chứng khoán" + odMast.getCodeid().toUpperCase() + "Khối lượng" + quantity + "Giá" + String.valueOf(odMast.getQuoteprice()) + "chưa xử lý được");
                            ex.printStackTrace();
                            return;
                        }
                        // save sell order
                        //get loan date number
                        if (type.equals("NS")) {
                            int loanDates = Math.max(0, tradingResponse.getTxDate() - odMast.getTxdate());
                            int saveResponse = orderTradingService.saveOrder(tradingResponse.getOrderId(),
                                    odMast.getCodeid().toUpperCase(),
                                    request.getCustId(), "Ban", tradingResponse.getOrderType(),
                                    tradingResponse.getPrice(), tradingResponse.getQuantity(),
                                    tradingResponse.getTxTime(), tradingResponse.getTxDate(), odMast.getFloor(), loanDates);

                            if (saveResponse == 1) {

                                //error
                                errors.put("ERRSAVE" + s + "Chứng khoán" + odMast.getCodeid().toUpperCase() + "Khối lượng" + quantity + "Giá" + String.valueOf(tradingResponse.getPrice()), "Save Failure");

                            } else {
                                errors.put("SUCCESS" + s + "Chứng khoán" + odMast.getCodeid().toUpperCase() + "Khối lượng" + quantity + "Giá" + String.valueOf(tradingResponse.getPrice()), "Thành công!");
                                odMast.setRefOderId(s);
                                odMastRepository.save(odMast);
                                //update table map_order
                                MapOrder mapOrder = new MapOrder(tradingResponse.getOderIDM(), tradingResponse.getOrderId());
                                mapOrderRepository.save(mapOrder);
                            }
                        }
                        if (type.equals("NB")) {
                            int saveResponse = orderTradingService.saveOrder(tradingResponse.getOrderId(),
                                    odMast.getCodeid().toUpperCase(),
                                    request.getCustId(), "Mua", tradingResponse.getOrderType(),
                                    tradingResponse.getPrice(), tradingResponse.getQuantity(),
                                    tradingResponse.getTxTime(), tradingResponse.getTxDate(), odMast.getFloor(), 0);
                            if (saveResponse == 1) {
                                //error
                                errors.put("ERRSAVE" + s + "Chứng khoán" + odMast.getCodeid().toUpperCase() + "Khối lượng" + quantity + "Giá" + String.valueOf(tradingResponse.getPrice()), "Save Failure");
                            } else {
                                odMast.setRefOderId(s);
                                odMastRepository.save(odMast);
                                errors.put("SUCCESSSAVEBUY" + s + "Chứng khoán" + odMast.getCodeid().toUpperCase() + "Khối lượng" + quantity + "Giá" + String.valueOf(tradingResponse.getPrice()), "Thành công!");
                            }
                        }
                    } else if (result == 1) {

                        errors.put("ERRAMOUNT" + s + "Chứng khoán" + odMast.getCodeid().toUpperCase() + "Khối lượng" + quantity , "Không đủ tiền bán!");

                    } else if (result == 2) {

                        errors.put("ERRCODEID" + s + "Chứng khoán" + odMast.getCodeid().toUpperCase() + "Khối lượng" + quantity , "Không cho phép bán chứng khoán trong này!");

                    } else if (result == 3) {

                        errors.put("ERRQTTY" + s + "Chứng khoán" + odMast.getCodeid().toUpperCase() + "Khối lượng" + quantity , "Không cho phép giao dịch nhiều hon khối lượng cho phép!");

                    } else if (result == 4) {

                        errors.put("ERREXIST" + s + "Chứng khoán" + odMast.getCodeid().toUpperCase() + "Khối lượng" + quantity , "Không tồn tại!");

                    } else {

                        errors.put("ERRCHECK" + s, "Invalid Order Checking!");

                    }
                }
            } else {
                errors.put("ERRCHECKCLOSE" + s +"Chứng khoán" + odMast.getCodeid().toUpperCase() + "Khối lượng" + odMast.getExecqtty() , "Đã tồn tại lệnh đóng");
            }
        });
        jsonObject.put("result", errors);
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    public static void main(String[] args) throws ParseException {
        String orderType = "LO";
        String date1 = "2019-04-03T14:31:12Z";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = sdf.parse(date1.replaceAll("Z$", "+0700"));
        //Date date = new Date();   // given date
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int hours = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        int minutes = calendar.get(Calendar.MINUTE);
        ;
        int time = hours * 60 + minutes;
        if (time < 9 * 60 || time > (14 * 60 + 45)) {
            orderType = "ERR";
        } else if (time >= 9 * 60 && time <= (9 * 60 + 15)) {
            orderType = "ATO";
        } else if (time > (9 * 60 + 15) && time <= (14 * 60 + 30)) {
            orderType = "MP";
        } else if (time > (14 * 60 + 30) && time <= (14 * 60 + 45)) {
            orderType = "ATC";
        }
        System.out.println(orderType);
    }

}
