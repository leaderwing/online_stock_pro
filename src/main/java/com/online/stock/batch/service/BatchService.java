
package com.online.stock.batch.service;

import com.online.stock.batch.config.ConnectDB;
import com.online.stock.batch.config.ConnectDBPrice;
import com.online.stock.controller.OrderTradingController;
import com.online.stock.dto.BatchDataResponse;
import com.online.stock.dto.Token;
import com.online.stock.dto.VTOSObject;
import com.online.stock.model.AdminUser;
import com.online.stock.model.ODMast;
import com.online.stock.model.SecuritiesPractice;
import com.online.stock.repository.AdminUserRepository;
import com.online.stock.repository.ODMastRepository;
import com.online.stock.repository.SecuritiesPracticeRepository;
import com.online.stock.utils.Constant;
import netscape.javascript.JSObject;
import oracle.jdbc.driver.OracleDriver;
import oracle.jdbc.oracore.OracleType;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.procedure.ProcedureCall;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BatchService implements  IBatchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchService.class);
    @Autowired
    private AdminUserRepository adminUserRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private SecuritiesPracticeRepository securitiesPracticeRepository;
    @Autowired
    private ODMastRepository odMastRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void getMatchCancelValue() throws SQLException {
        //AdminUser adminUser = null;
        List<BatchDataResponse> dataResponseList = new ArrayList<>();
        List<String> list = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        Connection connection = ConnectDB.getInstance().getConnection();
        try {
            String token = System.getProperty("job_token");
            if (StringUtils.isBlank(token)) {
                getToken();
                token = System.getProperty("job_token");
                //token = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJpc3N1ZXIiLCJzdWIiOiJzdWJqZWN0IiwiYXVkIjpbImF1ZGllbmNlIiwiaW9zIiwib25saW5lIiwidHJhZGVhcGkiLCJhdXRoIl0sImV4cCI6MTU1NDIxNzI2MCwibmJmIjoxNTUzNjEyMTYwLCJ0cmFkaW5nRXhwIjowLCJpZGdJZCI6IjAwMDExMDk3NDIiLCJyb2xlcyI6IltPTkxJTkVfVFJBRElORywgUk9MRV9PTkxJTkVfVFJBRElORywgT05MSU5FX1ZJRVdfQUNDT1VOVF9JTkZPLCBST0xFX09OTElORV9WSUVXX0FDQ09VTlRfSU5GT10iLCJhY2NvdW50VHlwZSI6IklORElWSURVQUxfQUNDT1VOVCIsImN1c3RvbWVySWQiOiIwMDAxMTA5NzQyIiwidXNlcklkIjoibnVsbCIsInZlcnNpb24iOiJWMiIsImN1c3RvbWVyTmFtZSI6IlbFqSBCw6EgSMOgbyIsImVtYWlsIjoidHJhbmR1Y3BodTE3NUBnbWFpbC5jb20iLCJ1c2VybmFtZSI6IjAwMDE2MjM2ODgiLCJzdGF0dXMiOiJPTkxJTkVfQUNUSVZFIn0.ICiBSbqLJBY_kF0C135IqKpXjxphON03h6Yr_2ly-qtl9QcOBmPMl8WGKk3glJC5b__6yp1KwwyUQCh2Jcsdzyyu5FYn59x6aFe9Sl9dLU8a1u-QMGkEGyO_5L_HHHrcBAMxeD9zpzy6mzCDwyZfh7PzLECE69r87J0QvBXjHmzDQnBuBsayCQp4q2Q6pPkMJ8htISPfe16fm2-12LhEO5lje3UIXM3EzFV9Rq6zv8XzB_hgdAbDwuyQKs37S6L3z_llIq6_lqP20f7AoYGI4GmBiRJ3akadaIjJrmlaV9OMlSMZf7WHWfJW4BYez_6zheyO7Cx4V3QlTojiaybJJQ";
            }
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-auth-token", token);
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> data =
                    restTemplate.exchange(Constant.API_MATCH_CANCEL_DEAL, HttpMethod.GET, entity,
                            new ParameterizedTypeReference<String>() {
                            });
            String jsonRes = data.getBody();
            if (StringUtils.isNotBlank(jsonRes)) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonRes);
                    JSONArray jsonArray = jsonObject.getJSONArray("orders");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        BatchDataResponse response = new BatchDataResponse();
                        String status = obj.getString("status");
                        if ("Filled".equals(status)) {
                            response.setOrderId(obj.getString("id"));
                            response.setQtty(obj.getInt("quantity"));
                            response.setTxtime(obj.getString("createdAt"));
                            response.setPrice(obj.getDouble("price"));
                            response.setTotal_qtty(obj.getInt("quantity"));

                            JSONArray arr = obj.getJSONArray("orderReports");
                            response.setAvg_price(arr.getJSONObject(0).getDouble("averagePrice"));
                            response.setRemain_qtty(obj.getInt("remainingQuantity"));
                            response.setType(1);
                            list.add(response.toString());
                        } else if ("Cancelled".equals(status) || "PendingCancel".equals(status)) {
                            response.setOrderId(obj.getString("id"));
                            response.setQtty(obj.getInt("quantity"));
                            response.setTxtime(obj.getString("createdAt"));
                            response.setPrice(0);
                            response.setTotal_qtty(obj.getInt("quantity"));
                            response.setAvg_price(0);
                            response.setRemain_qtty(obj.getInt("remainingQuantity"));
                            response.setType(2);
                            JSONArray arr = obj.getJSONArray("orderReports");
                            if (arr.length() >=2) {
                                String reqId = arr.getJSONObject(2).getString("id");
                                ODMast odMast = odMastRepository.findFirstByOrderid(reqId);
                                if (odMast != null) {
                                    odMast.setRefOderId(response.getOrderId());
                                    odMastRepository.save(odMast);
                                }
                            }
                            list.add(response.toString());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    connection.close();
                    return;
                }
                //save database
                try {
                    String[] arrStr = list.toArray(new String[list.size()]);
                    //String response = list.toString().replace("[", "").replace("]", "");
                    System.out.println("$ match data: " + Arrays.toString(arrStr));
                    if (arrStr.length > 0) {
                        ArrayDescriptor arrDesc =
                                ArrayDescriptor.createDescriptor("VARCHAR2_ARRAY", connection);
                        Array array = new ARRAY(arrDesc, connection, arrStr);
                        CallableStatement stmt =
                                connection.prepareCall("BEGIN PKG_ORDER_TRADING.PRC_GET_TRADING_RESULT(?); END;");
                        stmt.setArray(1, array);
                        stmt.execute();
                        stmt.close();
                    }
                } catch (Exception ex) {
                    LOGGER.error("error save data! " + ex.getMessage());
                    connection.close();
                } finally {
                    connection.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.print(" error when get api data: " + ex.getMessage());
        } finally {
            connection.close();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void getPriceValue() throws SQLException {
        List<SecuritiesPractice> securitiesPractices = securitiesPracticeRepository.findAll();
        Set<String> symbols = securitiesPractices.stream().map(SecuritiesPractice::getSymbol).collect(Collectors.toSet());
        String url = Constant.API_PRICE_DEAL.concat("q=codes:").concat(symbols.toString().replace("[", "").replace("]", "").replaceAll(" ", ""));
        RestTemplate restTemplate = new RestTemplate();
        String data = restTemplate.getForObject(url, String.class);
        if (StringUtils.isNotBlank(data)) {
            Connection connection = ConnectDBPrice.getInstance().getConnection();
            data = data.substring(2, data.length() - 2).replace("\"", "");
            System.out.println(data);
            String[] arrayPrice = data.split(",");
            LOGGER.debug(" price data: {data}", data);
            if (arrayPrice.length > 0) {
                try {
                    //connection = ConnectDB.getInstance().getConnection();
                    ArrayDescriptor arrDesc =
                            ArrayDescriptor.createDescriptor("VARCHAR2_ARRAY", connection);
                    Array array = new ARRAY(arrDesc, connection, arrayPrice);
                    CallableStatement stmt =
                            connection.prepareCall("BEGIN PKG_ORDER_TRADING.PRC_UPDATE_TRADING_DATA(?); END;");
                    stmt.setArray(1, array);
                    stmt.execute();
                    stmt.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    LOGGER.error("error save data! " + ex.getMessage());
                } finally {
                    connection.close();
                }
            }
            connection.close();
        }
    }

    @Override
    public void getToken() {
        AdminUser adminUser = new AdminUser();
        List<AdminUser> adminUserList = adminUserRepository.findAll();
        if (!adminUserList.isEmpty()) {
            adminUser = adminUserList.get(0);
        }
        RestTemplate restTemplate = new RestTemplate();
        Token token =
                restTemplate.postForObject(Constant.API_URL_TOKEN, adminUser, Token.class);
        System.setProperty("job_token", token.getToken());
    }

    public static void main(String[] args) {

        AdminUser adminUser = new AdminUser();
        adminUser.setUsername("0001623688");
        adminUser.setPassword("maiyeu1612");
        List<BatchDataResponse> dataResponseList = new ArrayList<>();
        String array[] = new String[1000];

    }
}
