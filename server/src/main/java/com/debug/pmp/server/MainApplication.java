package com.debug.pmp.server;
/**
 * Created by Administrator on 2019/7/17.
 */

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author:debug (SteadyJack)
 * @Date: 2019/7/29 22:12
 **/
// 引用工作流 所以加了 (exclude = SecurityAutoConfiguration.class)
@SpringBootApplication (exclude = SecurityAutoConfiguration.class)
@MapperScan(basePackages = "com.debug.pmp.model.mapper")
public class MainApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MainApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainApplication.class, args);
    }
}




































