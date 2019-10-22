package com.grocery.demo;

import com.twilio.Twilio;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

//    private static final String ACCOUNT_SID = "ACd176f3083e1333d14939fe6f27d90a99";
//    private static final String AUTH_ID = "4e3327cb4efbe3ecd8e893d809533763";
//
//    static {
//        Twilio.init(ACCOUNT_SID, AUTH_ID);
//    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
