package com.online.stock.config;

import com.online.stock.repository.SysVarRepository;
import com.online.stock.utils.Constant;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class SendMailConfig {
    @Autowired
    private SysVarRepository sysVarRepository;

    @Bean
    public JavaMailSender getJavaMailSender() {
        String mail_address =
                sysVarRepository.findFirstByGrnameAndVarname("SYSTEM","MAIL_ADDRESS") == null
                ? "" : sysVarRepository.findFirstByGrnameAndVarname("SYSTEM","MAIL_ADDRESS").getVarvalue() ;
        String mail_password =
                sysVarRepository.findFirstByGrnameAndVarname("SYSTEM","MAIL_PASSWORD") == null
                        ? "" : sysVarRepository.findFirstByGrnameAndVarname("SYSTEM","MAIL_PASSWORD").getVarvalue() ;

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(StringUtils.isNotBlank(mail_address) ? mail_address : Constant.MAIL_ADDRESS);
        mailSender.setPassword(StringUtils.isNotBlank(mail_password) ? mail_password : Constant.MAIL_PASSWORD);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
