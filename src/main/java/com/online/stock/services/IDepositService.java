package com.online.stock.services;

import com.online.stock.dto.request.TransferMoneyRequest;
import com.online.stock.dto.response.ApproveDepositRes;

import java.util.List;

public interface IDepositService {

    int executeDeposit (TransferMoneyRequest request, String accName, String remoteIp, String transferType);

    List<ApproveDepositRes> getListApproveDeposit(String tltx);

    int approveDeposit(ApproveDepositRes res, String transferType);
}
