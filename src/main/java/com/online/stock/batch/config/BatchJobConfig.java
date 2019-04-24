
package com.online.stock.batch.config;

import com.online.stock.batch.service.IBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.SQLException;

@Configuration
@EnableScheduling
public class BatchJobConfig {
    @Autowired
    private IBatchService batchService;

    @Scheduled(fixedRate = 5000)
    public void scheduleGetMatchAndCancelled() throws SQLException {
        System.out.println(
                "start get match and cancel data - " + System.currentTimeMillis() / 1000000);
        batchService.getMatchCancelValue();
        System.out.println(
                "finish get match and cancel data - " + System.currentTimeMillis() / 1000000);
    }
    @Scheduled(fixedRate = 24*60*60*1000)
    public void getToken() {
        System.out.println("get token!");
        batchService.getToken();
        System.out.println("finish get token!");
    }

    @Scheduled(fixedRate = 10000)
    public void scheduleGetPriceData() throws SQLException {
        System.out.println(
                "start get price data - " + System.currentTimeMillis() / 1000000);
        batchService.getPriceValue();
        System.out.println(
                "finish get price data - " + System.currentTimeMillis() / 1000000);
    }

}
