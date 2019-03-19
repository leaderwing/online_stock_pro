package com.online.stock.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

public class FileUtils {

    public static void main(String[] args) {
        System.out.println(genRandomPassword(6));
        //getMatrixCode(Arrays.asList("A1","B4"));
    }
    public static String getMatrixCode(List<String> keyList) {
        Properties props = new Properties();
        String requestCode = "";
        try {
            InputStream in = null;
            in = FileUtils.class.getClassLoader().getResourceAsStream("vtosMatrix.properties");
            props.load(in);
        }
        catch (IOException e) {
            // error using the file
            e.printStackTrace();
            System.out.println("not found file!");
        }
        requestCode = props.getProperty(keyList.get(0)) + "," + props.getProperty(keyList.get(1))
                + "," + props.getProperty(keyList.get(2));
        return  requestCode;
    }
    public static String genRandomPassword(int n) {
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }
}
