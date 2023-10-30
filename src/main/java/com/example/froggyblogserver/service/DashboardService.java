package com.example.froggyblogserver.service;

import com.example.froggyblogserver.response.BaseResponse;

public interface DashboardService {
    BaseResponse dashboardPeriod(Integer period);
    BaseResponse dashboardSum();
}
