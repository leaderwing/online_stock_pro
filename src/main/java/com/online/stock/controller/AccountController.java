package com.online.stock.controller;

import com.online.stock.dto.response.AccountInfoRes;
import com.online.stock.model.Afmast;
import com.online.stock.repository.AfmastRepository;
import com.online.stock.services.IAccountService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AccountController {
    @Autowired
    private IAccountService accountService;
    @Autowired
    private AfmastRepository afmastRepository;
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_1','ROLE_ADMIN_2', 'ROLE_SADMIN')")
    @RequestMapping(value = "/gettk", method = RequestMethod.GET)
    public ResponseEntity<List<AccountInfoRes>> getAccounts() {
        List<AccountInfoRes> resList = new ArrayList<>();
        resList = accountService.getAccountList();
        return new ResponseEntity<>(resList, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_1','ROLE_ADMIN_2', 'ROLE_SADMIN')")
    @RequestMapping(value = "/captaikhoan", method = RequestMethod.POST)
    public ResponseEntity<Void> capTaikhoan(@RequestBody String custId) {
        String userName = "";
        try {
            JSONObject jsonObject = new JSONObject(custId);
            userName = jsonObject.getString("CUSTID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Afmast afmast = afmastRepository.findOneByUsername(userName);
        if (afmast == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        afmast.setStatus("A");
        afmastRepository.save(afmast);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
