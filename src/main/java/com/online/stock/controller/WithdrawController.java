package com.online.stock.controller;

import com.online.stock.dto.request.TransferMoneyRequest;
import com.online.stock.dto.response.ApproveDepositRes;
import com.online.stock.model.Afmast;
import com.online.stock.repository.AfmastRepository;
import com.online.stock.services.IDepositService;
import com.online.stock.utils.DateUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class WithdrawController {
    private static final String TLTX = "1304";
    @Autowired
    private IDepositService depositService;
    @Autowired
    private AfmastRepository afmastRepository;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN_1','ROLE_SADMIN')")
    @RequestMapping(value = "/withdrawal", method = RequestMethod.POST)
    public ResponseEntity<String> sendWithdraw(HttpServletRequest httpRequest,
                                               @RequestBody TransferMoneyRequest request) throws JSONException {
        //String ipAddress = httpRequest.getHeader("X-FORWARDED-FOR");
        String ipAddress = "127.0.0.1";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        JSONObject jsonObject = new JSONObject();

        //check exist deposit account
        Afmast afmast = afmastRepository.findOneByUsername(request.getAccount());
        if (afmast == null) {
            return new ResponseEntity<>("Tài khoản rút tiền " + request.getAccount()+" không tồn " +
                    "tại!",
                    HttpStatus.NOT_FOUND);
        }
        try {
            int output = depositService.executeDeposit(request, loggedUsername, ipAddress, TLTX);
            if (output == 1) {
                jsonObject.put("result", "Không thực hiện được!");
                return new ResponseEntity<>(jsonObject.toString(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            jsonObject.put("result", "Bạn vừa rút " + request.getAmount() + " cho tài khoản " + request.getAccount());
            return new ResponseEntity<>(jsonObject.toString(),
                    HttpStatus.OK);
        } catch (Exception ex) {
            jsonObject.put("result", "Có lỗi xảy ra!");
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN_2','ROLE_SADMIN')")
    @RequestMapping(value = "/duyetrut", method = RequestMethod.GET)
    public ResponseEntity<List<ApproveDepositRes>> getApproveWithdraw() {
        List<ApproveDepositRes> resList = new ArrayList<>();
        resList = depositService.getListApproveDeposit(TLTX);
        return new ResponseEntity<>(resList,HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN_2','ROLE_SADMIN')")
    @RequestMapping(value = "/withdrawalap",method = RequestMethod.PUT)
    public ResponseEntity<String> approveWithdraw (@RequestBody ApproveDepositRes req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        req.setApproved_acctno(loggedUsername);
        req.setApproved_txtime(DateUtils.convertYYYY_MM_DD(new Date()));
        int output = depositService.approveDeposit(req, TLTX);
        if (output == 1) {
            return new ResponseEntity<>("Không duyệt được rút tiền!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Duyệt rút tiền thành công!", HttpStatus.NO_CONTENT);
    }
}
