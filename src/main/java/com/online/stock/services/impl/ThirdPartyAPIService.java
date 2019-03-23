package com.online.stock.services.impl;

import com.online.stock.dto.Token;
import com.online.stock.dto.TokenCode;
import com.online.stock.dto.VTOSObject;
import com.online.stock.dto.request.OrderRequest;
import com.online.stock.dto.response.OrderTradingResponse;
import com.online.stock.model.AdminUser;
import com.online.stock.repository.AdminUserRepository;
import com.online.stock.repository.VtosRepository;
import com.online.stock.services.IThirdPartyService;
import com.online.stock.utils.Constant;
import com.online.stock.utils.DateUtils;
import com.online.stock.utils.FileUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sun.net.www.http.HttpClient;

@Service
public class ThirdPartyAPIService implements IThirdPartyService {
    @Autowired
    private AdminUserRepository adminUserRepository;
    @Autowired
    private VtosRepository vtosRepository;

    public void getAdminAuthen() {
        List<AdminUser> adminUserList = adminUserRepository.findAll();
        if (!adminUserList.isEmpty()) {
            AdminUser adminUser = adminUserList.get(0);
            try {
                RestTemplate restTemplate = new RestTemplate();
                Token token =
                        restTemplate.postForObject(Constant.API_URL_TOKEN, adminUser, Token.class);
                HttpHeaders headers = new HttpHeaders();
                headers.set("x-auth-token", token.getToken());
                HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
                ResponseEntity<VTOSObject> vtos_token =
                        restTemplate.exchange(Constant.API_URL_VTOS, HttpMethod.GET, entity,
                                new ParameterizedTypeReference<VTOSObject>() {
                                });
                // get data from vtos table
                VTOSObject object = vtos_token.getBody();
                String requestCode = FileUtils.getMatrixCode(object.getChallenges());
                System.out.println(requestCode);
                TokenCode tokenCode = new TokenCode(requestCode);
                HttpEntity<TokenCode> newEntity = new HttpEntity<>(tokenCode, headers);
                ResponseEntity<Token> adminToken =
                        restTemplate.exchange(Constant.API_URL_AUTHEN, HttpMethod.POST, newEntity,
                                new ParameterizedTypeReference<Token>() {
                                });
                System.out.println(adminToken.getBody().getToken());
                System.setProperty("vtos", adminToken.getBody().getToken());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public  OrderTradingResponse sendOderTrading(String token, String orderType, Double price,
            Integer quantity, String symbol, String side) throws JSONException {
        OrderTradingResponse response = new OrderTradingResponse();
        OrderRequest request = new OrderRequest(false,orderType,price,quantity,side,symbol.toUpperCase(),"T");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String json = null;
        headers.set("x-auth-token", token);
        HttpEntity<OrderRequest> entity = new HttpEntity<>(request, headers);
        try {
            ResponseEntity<String> responseOrder =
                    restTemplate.exchange(Constant.API_URL_ORDER, HttpMethod.POST, entity,
                            new ParameterizedTypeReference<String>() {
                            });
            json = responseOrder.getBody();
        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            response.setError("Dữ liệu truyền vào không hợp lệ!");
            return  response;
        }
        JSONObject jsonObject = new JSONObject(json);
        if(jsonObject.has("error")) {
            response.setError(jsonObject.getString("error"));
            return  response;
        }
        JSONArray jsonArray =  jsonObject.getJSONArray("orders");
        String createdTime = jsonArray.getJSONObject(0).getString("createdAt");
        //todo : get time, hour
        response.setOrderId(jsonArray.getJSONObject(0).getString("id"));
        response.setSymbol(jsonArray.getJSONObject(0).getString("symbol"));
        response.setOrderType(jsonArray.getJSONObject(0).getString("orderType"));
        response.setQuantity(jsonArray.getJSONObject(0).getInt("quantity"));
        response.setPrice((Double)jsonArray.getJSONObject(0).get("price"));
        // set txtime , txdate
        response.setTxDate(DateUtils.getDateYYYYMMDD(createdTime));
        response.setTxTime(DateUtils.getHHMMSS(createdTime));
        return  response;
    }

    public static void main(String[] args) {
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername("0001623688");
        adminUser.setPassword("maiyeu1612");
        RestTemplate restTemplate = new RestTemplate();
        Token token = restTemplate.postForObject(Constant.API_URL_TOKEN, adminUser, Token.class);
        System.out.println(token.getToken());
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-auth-token", token.getToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<VTOSObject> vtos_token =
                restTemplate.exchange(Constant.API_URL_VTOS, HttpMethod.GET, entity,
                        new ParameterizedTypeReference<VTOSObject>() {
                        });
        // get data from vtos table
        VTOSObject object = vtos_token.getBody();
        System.out.println(object.getChallenges());
        String requestCode = FileUtils.getMatrixCode(object.getChallenges());
        System.out.println(requestCode);
        TokenCode tokenCode = new TokenCode(requestCode);

        HttpEntity<TokenCode> newEntity = new HttpEntity<>(tokenCode, headers);
        ResponseEntity<Token> adminToken =
                restTemplate.exchange(Constant.API_URL_AUTHEN, HttpMethod.POST, newEntity,
                        new ParameterizedTypeReference<Token>() {
                        });
        System.out.println(adminToken.getBody().getToken());
        System.setProperty("token", adminToken.getBody().getToken());

        //sendOderTrading(adminToken.getBody().getToken(), "LO", 21f,100,"FPT");
    }
}
