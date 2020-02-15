package com.online.stock.controller;

import com.online.stock.dto.SocketFilter;
import com.online.stock.dto.request.SellStockRequest;
import com.online.stock.dto.response.*;
import com.online.stock.model.MapOrder;
import com.online.stock.model.ODMast;
import com.online.stock.model.VGeneralInfo;
import com.online.stock.repository.MapOrderRepository;
import com.online.stock.repository.ODMastRepository;
import com.online.stock.repository.VGeneralInfoRepository;
import com.online.stock.services.IOrderTradingService;
import com.online.stock.services.IThirdPartyService;
import com.online.stock.services.ITradingService;
import com.online.stock.utils.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.online.stock.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;

@RestController
public class TradingController {

    @Autowired
    private ITradingService tradingService;
    @Autowired
    private IThirdPartyService thirdPartyService;
    @Autowired
    private MapOrderRepository mapOrderRepository;
    @Autowired
    private ODMastRepository odMastRepository;
    @Autowired
    private VGeneralInfoRepository vGeneralInfoRepository;
    @Autowired
    private IOrderTradingService orderTradingService;

    private SimpMessagingTemplate template;

    @Autowired
    public TradingController(SimpMessagingTemplate template) {
        this.template = template;
    }

    public static final String DEFAULT_START_DATE = "20181201";

    @RequestMapping(value = "/gettime", method = RequestMethod.GET)
    public ResponseEntity<String> getCurrentTime() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        Date date = new Date();
        String currentDate = DateUtils.convertIso_date(date);
        jsonObject.put("time", currentDate);
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/route/suminfo", method = RequestMethod.GET)
    public void callSocketSumInfo() throws Exception {
        System.out.println("start send sum info!");
        //        //get ttchung
        List<CommonInfoRes> commonInfoRes = new ArrayList<>();

        List<VGeneralInfo> vGeneralInfos = vGeneralInfoRepository.findAll();
        if (vGeneralInfos.size() > 0) {
            vGeneralInfos.forEach(vGeneralInfo -> {
                CommonInfoRes commonInfoRes1 = new CommonInfoRes();
                commonInfoRes1.setAfacctno(vGeneralInfo.getCustId());
                commonInfoRes1.setTai_san_rong(vGeneralInfo.getTsr());
                commonInfoRes1.setSuc_mua(vGeneralInfo.getBitMax());
                commonInfoRes1.setTy_le_ky_quy(vGeneralInfo.getRealMargrate());
                commonInfoRes1.setDu_no_thuc_te(vGeneralInfo.getTotalLoad());
                commonInfoRes.add(commonInfoRes1);
            });
        }
        this.template.convertAndSend("/topic/ttchung", commonInfoRes);
        System.out.println("end send sum info!");
    }

    @RequestMapping(value = "/route/rate", method = RequestMethod.GET)
    public void callSocketRate() throws Exception {
        System.out.println("start send rate info!");
        //         get tttyle
        List<RateInfoRes> rateInfoRes = new ArrayList<>();
        rateInfoRes = orderTradingService.getRateInfo("");
        this.template.convertAndSend("/topic/tttyle", rateInfoRes);
        System.out.println("end send rate info!");
    }

    @RequestMapping(value = "/route/socket", method = RequestMethod.GET)
    public void callSocketTrading() throws Exception {
        System.out.println("start send trade!");
        int currentDate = DateUtils.convertDate_YYYYMMDD(new Date());
        TradingRecords tradingRecords =
                tradingService.getTradingHistory("", currentDate, currentDate, "", "");
        List<TradingRow> rowList = tradingRecords.getRowList();
        this.template.convertAndSend("/topic/trading", rowList);
        System.out.println("end send trade!");
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public ResponseEntity<TradingRecords> getTradingHistory(@RequestParam String ngay1,
                                                            @RequestParam String ngay2,
                                                            @RequestParam String symbol, @RequestParam String exectype) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        int fromDate = Integer.parseInt(ngay1);
        int toDate = Integer.parseInt(ngay2);
        TradingRecords tradingRecords =
                tradingService.getTradingHistory(loggedUsername, fromDate, toDate, symbol, exectype);
        return new ResponseEntity<>(tradingRecords, HttpStatus.OK);
    }

    @MessageMapping("/app/db")
    @SendTo("/topic/trading")
    public ResponseEntity<TradingRecords> eventListenHistory(SocketFilter socketFilter) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        int fromDate = Integer.parseInt(socketFilter.getNgay1());
        int toDate = Integer.parseInt(socketFilter.getNgay2());
        TradingRecords tradingRecords =
                tradingService.getTradingHistory(loggedUsername, fromDate, toDate, "", "");
        return new ResponseEntity<>(tradingRecords, HttpStatus.OK);
    }

    @RequestMapping(value = "/historyhits", method = RequestMethod.GET)
    public ResponseEntity<TradingRecords> getTradingHistoryHits(@RequestParam String ngay1,
                                                                @RequestParam String ngay2,
                                                                @RequestParam String symbol, @RequestParam String exectype) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        int fromDate = Integer.parseInt(ngay1);
        int toDate = Integer.parseInt(ngay2);
        TradingRecords tradingRecords =
                tradingService.getTradingHistoryHits(loggedUsername, fromDate, toDate, symbol, exectype);
        return new ResponseEntity<>(tradingRecords, HttpStatus.OK);
    }

    @RequestMapping(value = "/floorname/{symbol}", method = RequestMethod.GET)
    public ResponseEntity<FloorResponse> getFloorName(@PathVariable String symbol) {
        FloorResponse response = new FloorResponse();
        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.getForObject(Constant.API_URL_FLOOR.concat(StringUtils.trim(symbol).toUpperCase()), String.class);
        try {
            JSONObject jObject = new JSONObject(json);
            JSONArray jsonArray = jObject.getJSONObject("data").getJSONArray("hits");
            if (jsonArray.length() == 0) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            JSONObject source = jsonArray.getJSONObject(0).getJSONObject("_source");
            response.setFloorCode(String.valueOf(source.get("floorCode")));
            response.setFloor(String.valueOf(source.get("floor")));
            response.setBasic(String.valueOf(source.get("basic")));
            response.setCeil(String.valueOf(source.get("ceil")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/price/{symbol}", method = RequestMethod.GET)
    public ResponseEntity<PriceResponse> getFloorPrice(@PathVariable String symbol) {
        PriceResponse response = new PriceResponse();
        RestTemplate restTemplate = new RestTemplate();
        try {
            String json = restTemplate.getForObject(Constant.API_URL_FLOOR_PRICE.concat(StringUtils.trim(symbol).toUpperCase()), String.class);
            json = json.replace("[", "").replace("]", "")
                    .replace("\"", "");
            json = json.replace("|", ";");
            String[] priceList = json.split(";");
            response.setM1(priceList[23]);
            response.setKl1(priceList[24]);
            response.setB1(priceList[29]);
            response.setKl2(priceList[30]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/huy/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> cancelOrder(@PathVariable String id) {
        if (StringUtils.isBlank(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String vtos_token = System.getProperty("vtos");
        if (StringUtils.isBlank(vtos_token)) {
            //get vtos token
            thirdPartyService.getAdminAuthen();
            vtos_token = System.getProperty("vtos");
            if (StringUtils.isBlank(vtos_token)) {
                return new ResponseEntity<>("Invalid Authen token!", HttpStatus.NOT_FOUND);
            }
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-auth-token", vtos_token);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> response =
                restTemplate.exchange(Constant.API_URL_CANCEL_ORDER.concat(id).concat(
                        "?forcedSell=false"), HttpMethod.DELETE, entity, String.class);
        try {
            JSONObject jsonObject = new JSONObject(response.getBody());
            String refOderId = jsonObject.getString("id");
            if (StringUtils.isBlank(refOderId)) {
                return new ResponseEntity<>("Not Found RefId!", HttpStatus.NOT_FOUND);
            }
            ODMast odMast = odMastRepository.findFirstByOrderid(id);
            if (odMast != null) {
                odMast.setRefOderId(refOderId);
                odMastRepository.save(odMast);
            } else {
                return new ResponseEntity<>("Not Found OrderId", HttpStatus.NOT_FOUND);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Cancel successful!", HttpStatus.OK);
    }

    @RequestMapping(value = "/luongchungse", method = RequestMethod.GET)
    public ResponseEntity<List<HandleAccResponse>> handleAccounts() {
        List<HandleAccResponse> handleAccResponseList = new ArrayList<>();
        List<VGeneralInfo> vGeneralInfos = vGeneralInfoRepository.findAll();

        if (!vGeneralInfos.isEmpty()) {
            handleAccResponseList = vGeneralInfos.stream().map(VGeneralInfo::convertData).collect(Collectors.toList());
        }
        return new ResponseEntity<>(handleAccResponseList, HttpStatus.OK);
    }

    @Autowired
    private EntityManager entityManager;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN_1','ROLE_ADMIN_2', 'ROLE_SADMIN')")
    @RequestMapping(value = "/closedaccount", method = RequestMethod.POST)
    public ResponseEntity<String> runClosed(@RequestBody String account) throws JSONException {
//        String[] a= new String[0];
        String p_out = "sss";
//        int runDate = DateUtils.convertDate_YYYYMMDD(new Date());
        Session session = entityManager.unwrap(Session.class);
        ProcedureCall call = session.createStoredProcedureCall("PKG_ORDER_TRADING.PRC_GET_LIST_ORDERID");
        call.registerParameter(1, String.class, ParameterMode.IN).bindValue(account);
        call.registerParameter(12, String.class, ParameterMode.OUT);

        p_out = (String) call.getOutputs().getOutputParameterValue(12);
        //p_out = "021419-FIXIN01-59635-0-Cross-Cross031717000120604,021419-FIXIN02-120590-0-Online8-Online8031714000120904";
        if (p_out == null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result", "Khong the xu ly tai khoan nay");
            return new ResponseEntity<>(jsonObject.toString(),HttpStatus.BAD_REQUEST);
        } else {

            String[] arg = p_out.split(",");
            //LOGGER.debug("result save order :" + p_out);
            for (int i = 0; i < arg.length; i++) {
                String type = "";
                ODMast tradingRecords =
                        tradingService.getRecordTradingHistory(account, arg[i]);
//                JSONObject jsonObject = new JSONObject(response.getBody());
                System.out.println(tradingRecords.getExectype());

                if (tradingRecords.getExectype().equals("Mua")) {
                    type = "NS";
                } else {
                    type = "NB";
                }
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String loggedUsername = auth.getName();
                if (StringUtils.isBlank(loggedUsername)) {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
                JSONObject jsonObject = new JSONObject();
                int quantity = tradingRecords.getExecqtty() - tradingRecords.getClosedqtty();
                int resultClose = orderTradingService.checkCloseOder(quantity, tradingRecords.getExecqtty(),
                        arg[i]);
                if (resultClose == 0) {
                    if (StringUtils.isBlank(tradingRecords.getCodeid())
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
                            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                        }
                    }
                    OrderTradingResponse tradingResponse = new OrderTradingResponse();
                    try {
                        tradingResponse = thirdPartyService.sendOderTrading(vtos_token, "MP"
                                , null, quantity, tradingRecords.getCodeid(), type);
                        if (StringUtils.isNotBlank(tradingResponse.getError())) {
                            jsonObject.put("result", tradingResponse.getError());
                            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.BAD_REQUEST);
                        }
                        tradingResponse.setOderIDM(arg[i]);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                    // save sell order
                    //get loan date number
                    if (type.equals("NS")) {
                        int loanDates = Math.max(0, tradingResponse.getTxDate() - tradingRecords.getTxdate());
                        int saveResponse = orderTradingService.saveOrder(tradingResponse.getOrderId(),
                                tradingRecords.getCodeid(),
                                account, "Ban", tradingResponse.getOrderType(),
                                tradingResponse.getPrice(), tradingResponse.getQuantity(),
                                tradingResponse.getTxTime(), tradingResponse.getTxDate(), tradingRecords.getFloor(), loanDates);
                        if (saveResponse == 1) {
                            //error
                            jsonObject.put("result", "save failure!");
                            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
                        } else {
                            ODMast odMast = odMastRepository.findFirstByOrderid(arg[i]);
                            odMast.setRefOderId(arg[i]);
                            odMastRepository.save(odMast);
                            //update table map_order
                            MapOrder mapOrder = new MapOrder(tradingResponse.getOderIDM(), tradingResponse.getOrderId());
                            mapOrderRepository.save(mapOrder);
                        }
                    }
                    if (type.equals("NB")) {
                        int saveResponse = orderTradingService.saveOrder(tradingResponse.getOrderId(),
                                tradingRecords.getCodeid(),
                                account, "Mua", tradingResponse.getOrderType(),
                                tradingResponse.getPrice(), tradingResponse.getQuantity(),
                                tradingResponse.getTxTime(), tradingResponse.getTxDate(), tradingRecords.getFloor(), 0);
                        if (saveResponse == 1) {
                            //error
                            jsonObject.put("result", "save failure!");
                            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
                        } else {
                            ODMast odMast = odMastRepository.findFirstByOrderid(arg[i]);
                            odMast.setRefOderId(arg[i]);
                            odMastRepository.save(odMast);
                        }
                    }
                } else {
                    jsonObject.put("result", "Đã tồn tại lệnh đóng");
                    return new ResponseEntity<>(jsonObject.toString(),HttpStatus.OK);
                }
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result", "Thành công!");
            return new ResponseEntity<>(jsonObject.toString(),HttpStatus.OK);

        }
    }

    @RequestMapping(value = "/historyView/{id}", method = RequestMethod.GET)
    public ResponseEntity<TradingRecords> viewAdminHistory(@PathVariable String id) {
        int fromDate = Integer.parseInt(DEFAULT_START_DATE);
        int toDate = DateUtils.convertDate_YYYYMMDD(new Date());
        TradingRecords tradingRecords =
                tradingService.getTradingHistory(id, fromDate, toDate, "", "");
        return new ResponseEntity<>(tradingRecords, HttpStatus.OK);
    }


    public static void main(String[] args) {
        FloorResponse response = new FloorResponse();
        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.getForObject(Constant.API_URL_FLOOR.concat(StringUtils.trim("FPT")), String.class);
        try {
            JSONObject jObject = new JSONObject(json);
            JSONArray jsonArray = jObject.getJSONObject("data").getJSONArray("hits");
            if (jsonArray.length() == 0) {

            }
            JSONObject source = jsonArray.getJSONObject(0).getJSONObject("_source");
            response.setFloorCode(String.valueOf(source.get("floorCode")));
            response.setFloor(String.valueOf(source.get("floor")));
            response.setBasic(String.valueOf(source.get("basic")));
            response.setCeil(String.valueOf(source.get("ceil")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(response.toString());
    }
}
