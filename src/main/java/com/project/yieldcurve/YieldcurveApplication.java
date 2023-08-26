package com.project.yieldcurve;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.project.yieldcurve.service.YieldCurveService;

@SpringBootApplication
public class YieldcurveApplication implements CommandLineRunner {

    @Autowired
    private YieldCurveService yieldCurveService;

    public static void main(String[] args) {
        SpringApplication.run(YieldcurveApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String, List<Double>> result = yieldCurveService.calculateYieldCurve();
        // Sonuçları burada işleyebilirsiniz
        System.out.println("son sonuclar burda:  "+result);
    }
}
