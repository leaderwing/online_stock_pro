//package com.online.stock.batch.config;
//
//import com.online.stock.batch.service.IBatchService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//@Configuration
//@EnableScheduling
//public class BatchJobConfig {
//    @Autowired
//    private IBatchService batchService;
//
//    @Scheduled(fixedDelay = 5000)
//    public void scheduleGetMatchAndCancelled() {
//        System.out.println(
//                "start get match and cancel data - " + System.currentTimeMillis() / 1000000);
//        batchService.getMatchCancelValue();
//        System.out.println(
//                "finish get match and cancel data - " + System.currentTimeMillis() / 1000000);
//    }
//    @Scheduled(fixedDelay = 10000)
//    public void scheduleGetPriceData() {
//        System.out.println(
//                "start get price data - " + System.currentTimeMillis() / 1000000);
//        batchService.getPriceValue();
//        System.out.println(
//                "finish get price data - " + System.currentTimeMillis() / 1000000);
//    }
//}
