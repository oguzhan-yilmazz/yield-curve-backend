package com.project.yieldcurve;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import com.project.yieldcurve.service.YieldCurveService;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = {"com.project.yieldcurve"}) // Eklendi
public class YieldcurveApplication implements CommandLineRunner {

    @Autowired
    private YieldCurveService yieldCurveService;

    public YieldcurveApplication(YieldCurveService yieldCurveService) {
        this.yieldCurveService = yieldCurveService;
    }

    public static void main(String[] args) {
        SpringApplication.run(YieldcurveApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        yieldCurveService.calculateYieldCurve("");

    }
}
