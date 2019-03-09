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

    public static void main(String[] args) {
        Date date = new Date();
         System.out.println(convertDate_YYYYMMDD(date));

    }
}
