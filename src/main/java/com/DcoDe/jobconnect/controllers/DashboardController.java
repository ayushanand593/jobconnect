package com.DcoDe.jobconnect.controllers;



import com.DcoDe.jobconnect.dto.ApplicationDetailDTO;
import com.DcoDe.jobconnect.dto.CandidateDashboardStatsDTO;
import com.DcoDe.jobconnect.dto.DashboardStatsDTO;
import com.DcoDe.jobconnect.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor

public class DashboardController {

    private final DashboardService dashboardService;


    @GetMapping("/employer")
    @PreAuthorize("hasAuthority('ROLE_EMPLOYER') or hasAuthority('EMPLOYER')")
    public ResponseEntity<DashboardStatsDTO> getEmployerDashboardStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // Default to last 30 days if not specified
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        return ResponseEntity.ok(dashboardService.getEmployerDashboardStats(startDate, endDate));
    }

    @GetMapping("/candidate")
    @PreAuthorize("hasAuthority('ROLE_CANDIDATE') or hasAuthority('CANDIDATE')")
public ResponseEntity<CandidateDashboardStatsDTO> getCandidateDashboardStats(
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    // Default to last 30 days if not specified
    if (startDate == null) {
        startDate = LocalDate.now().minusDays(30);
    }
    if (endDate == null) {
        endDate = LocalDate.now();
    }

    return ResponseEntity.ok(dashboardService.getCandidateDashboardStats(startDate, endDate));
}

@GetMapping("/candidate/applications")
@PreAuthorize("hasAuthority('ROLE_CANDIDATE') or hasAuthority('CANDIDATE')")
public ResponseEntity<List<ApplicationDetailDTO>> getCandidateApplications() {
    return ResponseEntity.ok(dashboardService.getCandidateApplications());
}

@GetMapping("/candidate/applications/{applicationId}")
@PreAuthorize("hasAuthority('ROLE_CANDIDATE') or hasAuthority('CANDIDATE')")
public ResponseEntity<ApplicationDetailDTO> getCandidateApplicationDetail(@PathVariable Long applicationId) {
    return ResponseEntity.ok(dashboardService.getCandidateApplicationDetail(applicationId));
}

    @GetMapping("/company/{companyId}")
    public ResponseEntity<DashboardStatsDTO> getCompanyDashboardStats(
            @PathVariable Long companyId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // Default to last 30 days if not specified
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        return ResponseEntity.ok(dashboardService.getCompanyDashboardStats(companyId, startDate, endDate));
    }
}
