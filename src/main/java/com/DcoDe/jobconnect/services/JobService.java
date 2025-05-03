package com.DcoDe.jobconnect.services;


import com.DcoDe.jobconnect.dto.JobCreateDTO;
import com.DcoDe.jobconnect.dto.JobDTO;
import com.DcoDe.jobconnect.enums.JobStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface JobService {
    JobDTO createJob(JobCreateDTO jobDto);
    JobDTO getJobById(Long id);
    JobDTO updateJob(Long id, JobCreateDTO jobDto);
    void deleteJob(Long id);
    Page<JobDTO> getJobsByCompany(Long companyId, JobStatus status, int page, int size);
    Page<JobDTO> searchJobs(String keyword, List<String> jobTypes, int page, int size);
    Page<JobDTO> getJobsBySkills(List<String> skills, int page, int size);
    Page<JobDTO> getCurrentEmployerJobs(int page, int size);
    void changeJobStatus(Long id, JobStatus status);
    List<JobDTO> getFeaturedJobs();
}
