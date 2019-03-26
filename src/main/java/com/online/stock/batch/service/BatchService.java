//package com.online.stock.batch.service;
//
//import com.online.stock.controller.OrderTradingController;
//import com.online.stock.dto.BatchDataResponse;
//import com.online.stock.dto.Token;
//import com.online.stock.dto.VTOSObject;
//import com.online.stock.model.AdminUser;
//import com.online.stock.model.SecuritiesPractice;
//import com.online.stock.repository.AdminUserRepository;
//import com.online.stock.repository.SecuritiesPracticeRepository;
//import com.online.stock.utils.Constant;
//import netscape.javascript.JSObject;
//import oracle.jdbc.oracore.OracleType;
//import oracle.sql.ARRAY;
//import org.apache.commons.lang.StringUtils;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.hibernate.cfg.Configuration;
//import org.hibernate.procedure.ProcedureCall;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//
//import javax.persistence.EntityManager;
//import javax.persistence.ParameterMode;
//import java.sql.Array;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Service
//public class BatchService implements  IBatchService{
//    private static final Logger LOGGER = LoggerFactory.getLogger(BatchService.class);
//    @Autowired
//    private AdminUserRepository adminUserRepository;
//    @Autowired
//    private EntityManager entityManager;
//    @Autowired
//    private SecuritiesPracticeRepository securitiesPracticeRepository;
//    @Override
//    @Transactional
//    public void getMatchCancelValue() {
//        //AdminUser adminUser = null;
//        AdminUser adminUser = new AdminUser();
//        List<BatchDataResponse> dataResponseList = new ArrayList<>();
//        List<String> list = new ArrayList<>();
//        List<AdminUser> adminUserList = adminUserRepository.findAll();
//        if (!adminUserList.isEmpty()) {
//             adminUser = adminUserList.get(0);
//        }
//        try {
//            RestTemplate restTemplate = new RestTemplate();
//            Token token =
//                    restTemplate.postForObject(Constant.API_URL_TOKEN, adminUser, Token.class);
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("x-auth-token", token.getToken());
//            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
//            ResponseEntity<String> data =
//                    restTemplate.exchange(Constant.API_MATCH_CANCEL_DEAL, HttpMethod.GET, entity,
//                            new ParameterizedTypeReference<String>() {
//                            });
//            String jsonRes = data.getBody();
//            if (StringUtils.isNotBlank(jsonRes)) {
//                try {
//                    JSONObject jsonObject = new JSONObject(jsonRes);
//                    JSONArray jsonArray = jsonObject.getJSONArray("orders");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject obj = jsonArray.getJSONObject(i);
//                        BatchDataResponse response = new BatchDataResponse();
//                        String status = obj.getString("status");
//                        if ("Filled".equals(status)) {
//                            response.setOrderId(obj.getString("id"));
//                            response.setQtty(obj.getInt("quantity"));
//                            response.setTxtime(obj.getString("createdAt"));
//                            response.setPrice(obj.getDouble("price"));
//                            response.setTotal_qtty(obj.getInt("quantity"));
//
//                            JSONArray arr = obj.getJSONArray("orderReports");
//                            response.setAvg_price(arr.getJSONObject(0).getDouble("averagePrice"));
//                            response.setRemain_qtty(obj.getInt("remainingQuantity"));
//                            list.add(response.toString());
//                        } else if ("Cancelled".equals(status) || "PendingCancel".equals(status)) {
//                            response.setOrderId(obj.getString("id"));
//                            response.setQtty(obj.getInt("quantity"));
//                            response.setTxtime(obj.getString("createdAt"));
//                            response.setPrice(0);
//                            response.setTotal_qtty(obj.getInt("quantity"));
//                            response.setAvg_price(0);
//                            response.setRemain_qtty(obj.getInt("remainingQuantity"));
//                            list.add(response.toString());
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                //save database
//                try {
//                    String response = list.toString().replace("[", "").replace("]", "");
//                    System.out.println("$ match data: " + response);
//                    Session session = entityManager.unwrap(Session.class);
//                    ProcedureCall call = session.createStoredProcedureCall("PKG_ORDER_TRADING.PRC_GET_TRADING_RESULT");
//                    call.registerParameter(1, String.class, ParameterMode.IN).bindValue(response);
//                    call.getOutputs();
//                } catch (Exception ex) {
//                    LOGGER.error("error save data! ", ex.getMessage());
//                }
//            }
//        }catch (Exception ex) {
//            System.out.print(" error when get api data: " + ex.getMessage());
//        }
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void getPriceValue() {
//        List<SecuritiesPractice> securitiesPractices = securitiesPracticeRepository.findAll();
//        Set<String> symbols = securitiesPractices.stream().map(SecuritiesPractice::getSymbol).collect(Collectors.toSet());
//        String url = Constant.API_PRICE_DEAL.concat("q=codes:").concat(symbols.toString().replace("[","").replace("]","").replaceAll(" ",""));
//        RestTemplate restTemplate = new RestTemplate();
//        String data = restTemplate.getForObject(url, String.class);
//        if (StringUtils.isNotBlank(data)) {
//            data = data.substring(2, data.length() - 2).replace("\"", "");
//            System.out.println(data);
//            LOGGER.debug(" price data: {data}", data);
//            try {
//                Session session = entityManager.unwrap(Session.class);
//                ProcedureCall call = session.createStoredProcedureCall("PKG_ORDER_TRADING.PRC_UPDATE_TRADING_DATA");
//                call.registerParameter(1, String.class,ParameterMode.IN).bindValue(data);
//                call.getOutputs();
//                session.close();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                LOGGER.error("error save data! "+ ex.getMessage());
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//
//        AdminUser adminUser = new AdminUser();
//        adminUser.setUsername("0001623688");
//        adminUser.setPassword("maiyeu1612");
//        List<BatchDataResponse> dataResponseList = new ArrayList<>();
//        String array[] = new String[1000];
////        List<AdminUser> adminUserList = adminUserRepository.findAll();
////        if (!adminUserList.isEmpty()) {
////             adminUser = adminUserList.get(0);
////        }
//        RestTemplate restTemplate = new RestTemplate();
//        Token token =
//                restTemplate.postForObject(Constant.API_URL_TOKEN, adminUser, Token.class);
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("x-auth-token", token.getToken());
//        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
//        ResponseEntity<String> data =
//                restTemplate.exchange(Constant.API_MATCH_CANCEL_DEAL, HttpMethod.GET, entity,
//                        new ParameterizedTypeReference<String>() {
//                        });
//        String jsonRes = data.getBody();
//        if (StringUtils.isNotBlank(jsonRes)) {
//            try {
//                JSONObject jsonObject = new JSONObject(jsonRes);
//                JSONArray jsonArray = jsonObject.getJSONArray("orders");
//                for (int i = 0;i < jsonArray.length(); i ++) {
//                    JSONObject obj = jsonArray.getJSONObject(i);
//                    BatchDataResponse response = new BatchDataResponse();
//                    String status = obj.getString("status");
//                    if ("Cancelled".equals(status)) {
//                        response.setOrderId(obj.getString("id"));
//                        response.setQtty(obj.getInt("quantity"));
//                        response.setTxtime(obj.getString("createdAt"));
//                        response.setPrice(obj.getDouble("price"));
//                        response.setTotal_qtty(obj.getInt("quantity"));
//
//                        JSONArray arr = obj.getJSONArray("orderReports");
//                        response.setAvg_price(arr.getJSONObject(0).getDouble("averagePrice"));
//                        response.setRemain_qtty(obj.getInt("remainingQuantity"));
//                        array[i] = response.toString();
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//           System.out.print(array);
//        }
//    }
//}
