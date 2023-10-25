package com.example.froggyblogserver.service;


import com.example.froggyblogserver.dto.ReportDto;
import com.example.froggyblogserver.generic.GeneralService;
import com.example.froggyblogserver.response.BaseResponse;

public interface ReportService extends GeneralService<ReportDto> {
    BaseResponse search(int page,int size,String column,String orderBy);
    BaseResponse acceptReport(String id);
    BaseResponse abortReport(String id);
}
