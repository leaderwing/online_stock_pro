package com.online.stock.controller;

import com.online.stock.dto.request.TransferMoneyRequest;
import com.online.stock.dto.response.ApproveDepositRes;
import com.online.stock.model.Afmast;
import com.online.stock.repository.AfmastRepository;
import com.online.stock.services.IDepositService;
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
import java.util.List;

@RestController
public class DepositController {
    private static final String TLTX = "1340";

    @Autowired
    private IDepositService depositService;
    @Autowired
    private AfmastRepository afmastRepository;

    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    public ResponseEntity<String> sendDeposit(HttpServletRequest httpRequest,
                                               @RequestBody TransferMoneyRequest request) {
        String ipAddress = httpRequest.getHeader("X-FORWARDED-FOR");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();

        //check exist deposit account
        Afmast afmast = afmastRepository.findOneByUsername(request.getAccount());
        if (afmast == null) {
            return new ResponseEntity<>("Tài khoản nộp tiền " + request.getAccount()+" không tồn tại!",
                    HttpStatus.NOT_FOUND);
        }
        try {
            int output = depositService.executeDeposit(request, loggedUsername, ipAddress, TLTX);
            if (output == 1) {
                return new ResponseEntity<>("Không thực hiện được!",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>("Bạn vừa nộp " + request.getAmount() + " cho tài khoản " + request.getAccount(),
                    HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Có lỗi xảy ra!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/duyetnop", method = RequestMethod.GET)
    public ResponseEntity<List<ApproveDepositRes>> getApproveDeposit() {
        List<ApproveDepositRes> resList = new ArrayList<>();
        resList = depositService.getListApproveDeposit(TLTX);
        return new ResponseEntity<>(resList,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/depositap",method = RequestMethod.PUT)
    public ResponseEntity<String> approveDeposit (ApproveDepositRes req) {
        int output = depositService.approveDeposit(req, TLTX);
        if (output == 1) {
            return new ResponseEntity<>("Không duyệt được nạp tiền!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Duyệt nạp tiền thành công!", HttpStatus.NO_CONTENT);
    }
}
