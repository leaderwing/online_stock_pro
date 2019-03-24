package com.online.stock.controller;

import com.online.stock.dto.response.ReportTradingRes;
import com.online.stock.services.ITradingService;
import com.online.stock.utils.ExcelGenerator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ExportExcelController {
    @Autowired
    private ITradingService tradingService;

    @GetMapping(value = "/export/excel/{buyfrom}/{buyto}/{sellfrom}/{sellto}/{symbol}/{account}")
    public ResponseEntity<InputStreamResource> createExcelFile(@PathVariable int buyfrom, @PathVariable int buyto
    ,@PathVariable int sellfrom,@PathVariable int sellto,@PathVariable String symbol,@PathVariable String account) throws IOException {
        System.out.print("$$$ start export report!");
        List<ReportTradingRes> reportTradingRes = new ArrayList<>();
        reportTradingRes = tradingService.getTradingReport(buyfrom,buyto,sellfrom,sellto,symbol,account);
        ByteArrayInputStream in = ExcelGenerator.exportToExcel(reportTradingRes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=trading.xlsx");
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }
}
