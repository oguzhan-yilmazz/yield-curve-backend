package com.project.yieldcurve.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.yieldcurve.service.YieldCurveService;

@RestController
@RequestMapping("/api/yieldcurve")
public class YieldCurveController {

    @Autowired
    private YieldCurveService yieldCurveService;
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/calculate")
    public ResponseEntity<Map<String, List<Double>>> calculateYieldCurve() throws Exception  {
        Map<String, List<Double>> result = yieldCurveService.calculateYieldCurve();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
