package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.dto.ChartAdminDto;
import com.example.froggyblogserver.repository.AccountRepo;
import com.example.froggyblogserver.repository.PostRepo;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashBoardServiceImpl implements DashboardService {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private PostRepo postRepo;
    @Override
    public BaseResponse dashboardPeriod(Integer period) {
        List<ChartAdminDto> charts = new ArrayList<>();
        var end = LocalDateTime.now();
        var start = end.minusDays(period - 1);
        for (var temp = start;!temp.isAfter(end);temp = temp.plusDays(1)){
            var minTempDate = temp.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toLocalDateTime();
            var maxTempDate = temp.toLocalDate().atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toLocalDateTime();
            charts.add(
                    ChartAdminDto.builder()
                            .date(minTempDate)
                            .accounts(accountRepo.countByDate(minTempDate,maxTempDate).orElse(0L))
                            .posts(postRepo.countByDate(minTempDate,maxTempDate).orElse(0L))
                            .build()
            );
        }
        return new BaseResponse(charts);
    }

    @Override
    public BaseResponse dashboardSum() {
        var chartSum = ChartAdminDto.builder()
                .accounts(accountRepo.countAll().orElse(0L))
                .posts(postRepo.countAll().orElse(0L))
                .build();
        return new BaseResponse(chartSum);
    }
}
