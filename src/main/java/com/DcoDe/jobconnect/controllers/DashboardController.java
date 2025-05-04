package com.DcoDe.jobconnect.controllers;


import com.DcoDe.jobconnect.dto.DashboardStatsDTO;
import com.DcoDe.jobconnect.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_EMPLOYER') or hasAuthority('EMPLOYER')")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/employer")
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
