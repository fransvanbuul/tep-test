package com.example.teptest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class TepTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TepTestApplication.class, args);
    }

    @Autowired
    public void dump(ApplicationContext applicationContext) {
        for(String beanName: applicationContext.getBeanDefinitionNames()) {
            log.debug("Bean: <{}>, class: <{}>", beanName, applicationContext.getBean(beanName).getClass().getName());
        }
    }

}
