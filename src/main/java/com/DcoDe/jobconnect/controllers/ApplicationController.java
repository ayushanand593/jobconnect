package com.DcoDe.jobconnect.controllers;


import com.DcoDe.jobconnect.dto.ApplicationDTO;
import com.DcoDe.jobconnect.enums.ApplicationStatus;
import com.DcoDe.jobconnect.services.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employer/applications")
@RequiredArgsConstructor
@PreAuthorize("hasRole('EMPLOYER')")
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/job/{jobId}")
    public ResponseEntity<Page<ApplicationDTO>> getApplicationsByJob(
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(applicationService.getApplicationsByJob(jobId, page, size));
    }

    @GetMapping("/job/{jobId}/status/{status}")
    public ResponseEntity<Page<ApplicationDTO>> getApplicationsByJobAndStatus(
            @PathVariable Long jobId,
            @PathVariable ApplicationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(applicationService.getApplicationsByJobAndStatus(jobId, status, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDTO> getApplicationById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateApplicationStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus status) {
        applicationService.updateApplicationStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/company")
    public ResponseEntity<Page<ApplicationDTO>> getCompanyApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(applicationService.getCompanyApplications(page, size));
    }
}
