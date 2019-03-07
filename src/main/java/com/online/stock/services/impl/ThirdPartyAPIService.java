package com.online.stock.services.impl;

import com.online.stock.dto.Token;
import com.online.stock.dto.TokenCode;
import com.online.stock.dto.VTOSObject;
import com.online.stock.model.AdminUser;
import com.online.stock.repository.AdminUserRepository;
import com.online.stock.repository.VtosRepository;
import com.online.stock.services.IThirdPartyService;
import com.online.stock.utils.Constant;
import com.online.stock.utils.FileUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
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
        String requestCode = "";
        for (String challenge : object.getChallenges()) {
            String code = null;
            int index = object.getChallenges().indexOf(challenge);
            if (index == 0) {
                code = "8";
            } else if (index == 1) {
                code = "V";
            } else {
                code = "F";
            }
            requestCode = requestCode.concat(code);
            if (index < object.getChallenges().size() - 1) {
                requestCode = requestCode.concat(",");
            }
        }
        System.out.println(requestCode);
        TokenCode tokenCode = new TokenCode(requestCode);

        HttpEntity<TokenCode> newEntity = new HttpEntity<>(tokenCode, headers);
        ResponseEntity<Token> adminToken =
                restTemplate.exchange(Constant.API_URL_AUTHEN, HttpMethod.POST, newEntity,
                        new ParameterizedTypeReference<Token>() {
                        });
        System.out.println(adminToken.getBody().getToken());
        System.setProperty("token", adminToken.getBody().getToken());
    }
}
