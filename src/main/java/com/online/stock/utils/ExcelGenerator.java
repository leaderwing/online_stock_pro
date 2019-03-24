package com.online.stock.utils;

import com.online.stock.dto.request.ExportFilterRequest;
import com.online.stock.dto.response.ReportTradingRes;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelGenerator {
    public static ByteArrayInputStream exportToExcel(List<ReportTradingRes> resList) throws IOException {
        String[] columns = {"Ngày mua", "Ngày bán", "Mã CK", "Số lượng", "Giá mua", " Giá bán", "Tổng tiền mua", "Tổng tiền bán"
        ,"Phí mua (0.2%)", "Phí bán  (0.3%)", "Số ngày vay", "Phí vay", "Lỗ/lãi", "Còn lại"};
        try(
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ){
            CreationHelper createHelper = workbook.getCreationHelper();

            Sheet sheet = workbook.createSheet("trading");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            headerCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            headerCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            headerCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            headerCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            headerCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            headerCellStyle.setBorderRight(CellStyle.BORDER_THIN);
            headerCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            headerCellStyle.setBorderTop(CellStyle.BORDER_THIN);
            headerCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

            // Row for Header
            Row headerRow = sheet.createRow(1);
            // Header
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(headerCellStyle);
            }
            // CellStyle for Age
            CellStyle styleRow = workbook.createCellStyle();

            styleRow.setBorderBottom(CellStyle.BORDER_THIN);
            styleRow.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            styleRow.setBorderLeft(CellStyle.BORDER_THIN);
            styleRow.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            styleRow.setBorderRight(CellStyle.BORDER_THIN);
            styleRow.setRightBorderColor(IndexedColors.BLACK.getIndex());
            styleRow.setBorderTop(CellStyle.BORDER_THIN);
            styleRow.setTopBorderColor(IndexedColors.BLACK.getIndex());
            int rowIdx = 2;
            for (ReportTradingRes res : resList) {
                Row row = sheet.createRow(rowIdx++);
                Cell cell0 = row.createCell(0);
                cell0.setCellStyle(styleRow);
                cell0.setCellValue(DateUtils.convertIntDate(res.getBuyDate()));
                Cell cell1 = row.createCell(1);
                cell1.setCellStyle(styleRow);
                cell1.setCellValue(DateUtils.convertIntDate(res.getSellDate()));
                Cell cell2 = row.createCell(2);
                cell2.setCellStyle(styleRow);
                cell2.setCellValue(res.getSymbol());
                Cell cell3 = row.createCell(3);
                cell3.setCellStyle(styleRow);
                cell3.setCellValue(res.getQuantity());
                Cell cell4 = row.createCell(4);
                cell4.setCellStyle(styleRow);
                cell4.setCellValue(res.getBuyPrice());
                Cell cell5 = row.createCell(5);
                cell5.setCellStyle(styleRow);
                cell5.setCellValue(res.getSellPrice());
                Cell cell6 = row.createCell(6);
                cell6.setCellStyle(styleRow);
                cell6.setCellValue(res.getSumBuyMoney());
                Cell cell7 = row.createCell(7);
                cell7.setCellStyle(styleRow);
                cell7.setCellValue(res.getSumSellMoney());
                Cell cell8 = row.createCell(8);
                cell8.setCellStyle(styleRow);
                cell8.setCellValue(res.getBuyFee());
                Cell cell9 = row.createCell(9);
                cell9.setCellStyle(styleRow);
                cell9.setCellValue(res.getSellFee());
                Cell cell10 = row.createCell(10);
                cell10.setCellStyle(styleRow);
                cell10.setCellValue(res.getLoanDay());
                Cell cell11 = row.createCell(11);
                cell11.setCellStyle(styleRow);
                cell11.setCellValue(res.getLoanFee());
                Cell cell12 = row.createCell(12);
                cell12.setCellStyle(styleRow);
                cell12.setCellValue(res.getProfit());
                Cell cell13 = row.createCell(13);
                cell13.setCellStyle(styleRow);
                cell13.setCellValue(res.getRemain());
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
