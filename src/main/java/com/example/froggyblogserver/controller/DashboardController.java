package com.example.froggyblogserver.controller;

import com.example.froggyblogserver.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService service;
    @GetMapping
    public ResponseEntity<?> chart(@RequestParam Integer period){
        return ResponseEntity.ok().body(service.dashboardPeriod(period));
    }

    @GetMapping("total")
    public ResponseEntity<?> chartSum(){
        return ResponseEntity.ok().body(service.dashboardSum());
    }
}
