package com.example.froggyblogserver.controller;

import com.example.froggyblogserver.dto.ReportDto;
import com.example.froggyblogserver.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/report")
public class ReportController {
    @Autowired
    private ReportService service;

    @PostMapping("saveOrUpdate")
    public ResponseEntity<?> saveOrUpdate(@RequestBody ReportDto dto){
        return ResponseEntity.ok().body(service.saveOrUpdate(dto));
    }

    @PostMapping("accept/{id}")
    public ResponseEntity<?> accept(@PathVariable String id){
        return ResponseEntity.ok().body(service.acceptReport(id));
    }

    @RequestMapping("search")
    public ResponseEntity<?> search(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size,@RequestParam(required = false) String column,@RequestParam(required = false) String orderBy ){
        if(page == null)
            page = 1;
        if (size == null)
            size = 10;
        return ResponseEntity.ok().body(service.search(page,size,column,orderBy));
    }

}
