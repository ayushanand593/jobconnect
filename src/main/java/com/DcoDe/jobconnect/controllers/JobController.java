package com.DcoDe.jobconnect.controllers;

import com.DcoDe.jobconnect.dto.ApplicationCreateDTO;
import com.DcoDe.jobconnect.dto.JobCreateDTO;
import com.DcoDe.jobconnect.dto.JobDTO;
import com.DcoDe.jobconnect.enums.JobStatus;
import com.DcoDe.jobconnect.services.ApplicationService;
import com.DcoDe.jobconnect.services.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    @Autowired
    private final ApplicationService applicationService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_EMPLOYER') or hasAuthority('EMPLOYER')")
    public ResponseEntity<JobDTO> createJob(@Valid @RequestBody JobCreateDTO jobDto) {
        JobDTO createdJob = jobService.createJob(jobDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @GetMapping("/jobId/{jobId}")
    public ResponseEntity<JobDTO> getJobByJobId(@PathVariable String jobId) {
        return ResponseEntity.ok(jobService.getJobByJobId(jobId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<JobDTO> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobCreateDTO jobDto) {
        return ResponseEntity.ok(jobService.updateJob(id, jobDto));
    }

    @PutMapping("/jobId/{jobId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<JobDTO> updateJobByJobId(
            @PathVariable String jobId,
            @Valid @RequestBody JobCreateDTO jobDto) {
        return ResponseEntity.ok(jobService.updateJobByJobId(jobId, jobDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/jobId/{jobId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Void> deleteJobByJobId(@PathVariable String jobId) {
        jobService.deleteJobByJobId(jobId);
        return ResponseEntity.noContent().build();
    }

       @PostMapping("/{jobId}/apply")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<String> applyToJob(
        @PathVariable String jobId,
        @Valid @RequestBody ApplicationCreateDTO applicationCreateDTO) {
    applicationService.applyToJob(jobId, applicationCreateDTO);
    return ResponseEntity.ok("Application submitted successfully");
}

    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<JobDTO>> getJobsByCompany(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "OPEN") JobStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(jobService.getJobsByCompany(companyId, status, page, size));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<JobDTO>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<String> jobTypes,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(jobService.searchJobs(keyword, jobTypes, page, size));
    }

    @GetMapping("/by-skills")
    public ResponseEntity<Page<JobDTO>> getJobsBySkills(
            @RequestParam List<String> skills,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(jobService.getJobsBySkills(skills, page, size));
    }

    @GetMapping("/my-jobs")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Page<JobDTO>> getCurrentEmployerJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(jobService.getCurrentEmployerJobs(page, size));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Void> changeJobStatus(
            @PathVariable Long id,
            @RequestParam JobStatus status) {
        jobService.changeJobStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/jobId/{jobId}/status")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Void> changeJobStatusByJobId(
            @PathVariable String jobId,
            @RequestParam JobStatus status) {
        jobService.changeJobStatusByJobId(jobId, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/featured")
    public ResponseEntity<List<JobDTO>> getFeaturedJobs() {
        return ResponseEntity.ok(jobService.getFeaturedJobs());
    }
}
