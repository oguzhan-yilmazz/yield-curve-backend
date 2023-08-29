package com.project.yieldcurve;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import com.project.yieldcurve.service.YieldCurveService;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
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
        yieldCurveService.calculateYieldCurve();

    }
}
