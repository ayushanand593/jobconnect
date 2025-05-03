package com.DcoDe.jobconnect.services;


import com.DcoDe.jobconnect.dto.DashboardStatsDTO;
import com.DcoDe.jobconnect.entities.Application;
import com.DcoDe.jobconnect.entities.Job;
import com.DcoDe.jobconnect.entities.User;
import com.DcoDe.jobconnect.enums.ApplicationStatus;
import com.DcoDe.jobconnect.enums.JobStatus;
import com.DcoDe.jobconnect.repositories.ApplicationRepository;
import com.DcoDe.jobconnect.repositories.JobRepository;
import com.DcoDe.jobconnect.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    public DashboardStatsDTO getEmployerDashboardStats(LocalDate startDate, LocalDate endDate) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null || currentUser.getCompany() == null) {
            throw new AccessDeniedException("Not authorized to view dashboard statistics");
        }

        Long employerId = currentUser.getId();
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        // Get all jobs posted by the employer
        List<Job> employerJobs = jobRepository.findAllByPostedById(employerId);

        if (employerJobs.isEmpty()) {
            return createEmptyStats();
        }

        Set<Long> jobIds = employerJobs.stream()
                .map(Job::getId)
                .collect(Collectors.toSet());

        // Get all applications for those jobs
        List<Application> allApplications = applicationRepository.findAllByJobIdIn(jobIds);

        // Get applications within the date range
        List<Application> periodApplications = allApplications.stream()
                .filter(app -> !app.getCreatedAt().isBefore(startDateTime) && !app.getCreatedAt().isAfter(endDateTime))
                .collect(Collectors.toList());

        return calculateStats(employerJobs, allApplications, periodApplications);
    }

    @Override
    public DashboardStatsDTO getCompanyDashboardStats(Long companyId, LocalDate startDate, LocalDate endDate) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null || currentUser.getCompany() == null ||
                !currentUser.getCompany().getId().equals(companyId)) {
            throw new AccessDeniedException("Not authorized to view company dashboard statistics");
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        // Get all jobs posted by the company
        List<Job> companyJobs = jobRepository.findAllByCompanyId(companyId);

        if (companyJobs.isEmpty()) {
            return createEmptyStats();
        }

        Set<Long> jobIds = companyJobs.stream()
                .map(Job::getId)
                .collect(Collectors.toSet());

        // Get all applications for those jobs
        List<Application> allApplications = applicationRepository.findAllByJobIdIn(jobIds);

        // Get applications within the date range
        List<Application> periodApplications = allApplications.stream()
                .filter(app -> !app.getCreatedAt().isBefore(startDateTime) && !app.getCreatedAt().isAfter(endDateTime))
                .collect(Collectors.toList());

        return calculateStats(companyJobs, allApplications, periodApplications);
    }

    private DashboardStatsDTO calculateStats(List<Job> jobs, List<Application> allApplications,
                                             List<Application> periodApplications) {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        // Calculate job stats
        stats.setTotalJobs((long) jobs.size());
        stats.setOpenJobs(jobs.stream()
                .filter(job -> job.getStatus() == JobStatus.OPEN)
                .count());
        stats.setClosedJobs(jobs.stream()
                .filter(job -> job.getStatus() == JobStatus.CLOSED || job.getStatus() == JobStatus.CLOSED)
                .count());

        // Calculate application stats
        stats.setTotalApplications((long) allApplications.size());
        stats.setNewApplications((long) periodApplications.size());
        stats.setShortlistedApplications(allApplications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.SHORTLISTED)
                .count());
        stats.setRejectedApplications(allApplications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.REJECTED)
                .count());

        // Application trend by date
        Map<String, Long> applicationTrend = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        periodApplications.stream()
                .collect(Collectors.groupingBy(
                        app -> app.getCreatedAt().format(formatter),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> applicationTrend.put(entry.getKey(), entry.getValue()));

        stats.setApplicationTrend(applicationTrend);

        // Top performing jobs
        Map<Long, Long> applicationCountByJob = allApplications.stream()
                .collect(Collectors.groupingBy(
                        app -> app.getJob().getId(),
                        Collectors.counting()
                ));

        List<DashboardStatsDTO.JobStatsDTO> topJobs = jobs.stream()
                .filter(job -> applicationCountByJob.containsKey(job.getId()))
                .map(job -> {
                    DashboardStatsDTO.JobStatsDTO jobStats = new DashboardStatsDTO.JobStatsDTO();
                    jobStats.setJobId(job.getId());
                    jobStats.setTitle(job.getTitle());
                    jobStats.setApplicationCount(applicationCountByJob.get(job.getId()));
                    return jobStats;
                })
                .sorted(Comparator.comparing(DashboardStatsDTO.JobStatsDTO::getApplicationCount).reversed())
                .limit(5)
                .collect(Collectors.toList());

        stats.setTopJobs(topJobs);

        // Application status distribution
        Map<String, Long> statusDistribution = allApplications.stream()
                .collect(Collectors.groupingBy(
                        app -> app.getStatus().name(),
                        Collectors.counting()
                ));

        stats.setApplicationStatusDistribution(statusDistribution);

        // Job type distribution
        Map<String, Long> jobTypeDistribution = jobs.stream()
                .collect(Collectors.groupingBy(
                        job -> job.getJobType().name(),
                        Collectors.counting()
                ));

        stats.setJobTypeDistribution(jobTypeDistribution);

        return stats;
    }

    private DashboardStatsDTO createEmptyStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setTotalJobs(0L);
        stats.setOpenJobs(0L);
        stats.setClosedJobs(0L);
        stats.setTotalApplications(0L);
        stats.setNewApplications(0L);
        stats.setShortlistedApplications(0L);
        stats.setRejectedApplications(0L);
        stats.setApplicationTrend(new HashMap<>());
        stats.setTopJobs(new ArrayList<>());
        stats.setApplicationStatusDistribution(new HashMap<>());
        stats.setJobTypeDistribution(new HashMap<>());
        return stats;
    }
}