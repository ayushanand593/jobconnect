package com.DcoDe.jobconnect.services;


import com.DcoDe.jobconnect.dto.ApplicationDetailDTO;
import com.DcoDe.jobconnect.dto.CandidateDashboardStatsDTO;
import com.DcoDe.jobconnect.dto.DashboardStatsDTO;

import java.time.LocalDate;
import java.util.List;

public interface DashboardService {
    DashboardStatsDTO getEmployerDashboardStats(LocalDate startDate, LocalDate endDate);
    DashboardStatsDTO getCompanyDashboardStats(Long companyId, LocalDate startDate, LocalDate endDate);
     CandidateDashboardStatsDTO getCandidateDashboardStats(LocalDate startDate, LocalDate endDate);
     List<ApplicationDetailDTO> getCandidateApplications();
    ApplicationDetailDTO getCandidateApplicationDetail(Long applicationId);
}
