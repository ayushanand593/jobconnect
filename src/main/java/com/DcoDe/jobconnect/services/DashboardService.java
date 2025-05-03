package com.DcoDe.jobconnect.services;


import com.DcoDe.jobconnect.dto.DashboardStatsDTO;

import java.time.LocalDate;

public interface DashboardService {
    DashboardStatsDTO getEmployerDashboardStats(LocalDate startDate, LocalDate endDate);
    DashboardStatsDTO getCompanyDashboardStats(Long companyId, LocalDate startDate, LocalDate endDate);
}
