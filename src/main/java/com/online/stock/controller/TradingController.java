package com.online.stock.controller;

import com.online.stock.dto.response.FloorResponse;
import com.online.stock.dto.response.PriceResponse;
import com.online.stock.dto.response.TradingRecords;
import com.online.stock.services.ITradingService;
import com.online.stock.utils.Constant;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TradingController {

    @Autowired
    private ITradingService tradingService;
    @RequestMapping(value = "/history",method = RequestMethod.GET)
    public ResponseEntity<TradingRecords> getTradingHistory(@RequestParam String ngay1,
            @RequestParam String ngay2,
            @RequestParam String symbol, @RequestParam String exectype) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        int fromDate = Integer.parseInt(ngay1);
        int toDate = Integer.parseInt(ngay2);
        TradingRecords tradingRecords =
                tradingService.getTradingHistory(loggedUsername,fromDate, toDate, symbol, exectype);
        return new  ResponseEntity<>(tradingRecords,HttpStatus.OK);
    }
    @RequestMapping(value = "/historyhits",method = RequestMethod.GET)
    public ResponseEntity<TradingRecords> getTradingHistoryHits(@RequestParam String ngay1,
            @RequestParam String ngay2,
            @RequestParam String symbol, @RequestParam String exectype) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        int fromDate = Integer.parseInt(ngay1);
        int toDate = Integer.parseInt(ngay2);
        TradingRecords tradingRecords =
                tradingService.getTradingHistoryHits(loggedUsername, fromDate, toDate, symbol, exectype);
        return new  ResponseEntity<>(tradingRecords,HttpStatus.OK);
    }
    @RequestMapping(value = "/floorname/{symbol}",method = RequestMethod.GET)
    public ResponseEntity<FloorResponse> getFloorName(@PathVariable String symbol) {
        FloorResponse response = new FloorResponse();
        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.getForObject(Constant.API_URL_FLOOR.concat(StringUtils.trim(symbol).toUpperCase()),String.class);
        try {
            JSONObject jObject = new JSONObject(json);
            JSONArray jsonArray = jObject.getJSONObject("data").getJSONArray("hits");
            if(jsonArray.length() == 0) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            JSONObject source = jsonArray.getJSONObject(0).getJSONObject("_source");
            response.setFloorCode(String.valueOf(source.get("floorCode")));
            response.setFloor(String.valueOf(source.get("floor")));
            response.setBasic(String.valueOf(source.get("basic")));
            response.setCeil(String.valueOf(source.get("ceil")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  new ResponseEntity<>(response,HttpStatus.OK);
    }
    @RequestMapping(value = "/price/{symbol}",method = RequestMethod.GET)
    public ResponseEntity<PriceResponse> getFloorPrice(@PathVariable String symbol) {
        PriceResponse response = new PriceResponse();
        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.getForObject(Constant.API_URL_FLOOR_PRICE.concat(StringUtils.trim(symbol).toUpperCase()),String.class);
        json = json.replace("[","").replace("]","")
            .replace("\"","");
        json = json.replace("|", ";");
        String[] priceList = json.split(";");
        response.setM1(priceList[23]);
        response.setKl1(priceList[24]);
        response.setB1(priceList[29]);
        response.setKl2(priceList[30]);
        return  new ResponseEntity<>(response,HttpStatus.OK);
    }


    public static void main(String[] args) {
        FloorResponse response = new FloorResponse();
        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.getForObject(Constant.API_URL_FLOOR.concat(StringUtils.trim("FPT")),String.class);
        try {
            JSONObject jObject = new JSONObject(json);
            JSONArray jsonArray = jObject.getJSONObject("data").getJSONArray("hits");
            if(jsonArray.length() == 0) {

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
