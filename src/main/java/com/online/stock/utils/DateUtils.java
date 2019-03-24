package com.online.stock.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DateUtils {

    private static final String FORMAT_YYYYMMDD = "yyyyMMdd";

    private static final String FORMAT_ISO = "yyyy-MM-dd'T'HH:mm:ssZ";

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
    public static Date convertDate (String date ) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YYYYMMDD);
        try {
            return  sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return  null;
        }
    }
    public static int getDateYYYYMMDD(String iso_date)  {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = sdf.parse(iso_date.replaceAll("Z$","+0700"));
            int result = convertDate_YYYYMMDD(date);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public static String getHHMMSS(String iso_date)  {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            Date date = sdf.parse(iso_date.replaceAll("Z$","+0700"));
            sdf = new SimpleDateFormat("HH:mm:ss");
            return  sdf.format(date);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        List strArray = new ArrayList();
        strArray.add("1234567|@3345");
        strArray.add("44|555");
        String joinedString = strArray.toString().replace("[","").replace("]","");
        System.out.print(joinedString);
    }
}
