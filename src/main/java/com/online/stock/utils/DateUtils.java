package com.online.stock.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final String FORMAT_YYYYMMDD = "yyyyMMdd";

    public static int convertDate_YYYYMMDD(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YYYYMMDD);
        int result = Integer.parseInt(sdf.format(date));
        return result;
    }
    public static String convertYYYY_MM_DD (Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            return sdf.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Date date = new Date();
         System.out.println(convertDate_YYYYMMDD(date));

    }
}
