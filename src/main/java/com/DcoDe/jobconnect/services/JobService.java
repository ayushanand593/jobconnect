package com.DcoDe.jobconnect.services;


import com.DcoDe.jobconnect.dto.JobCreateDTO;
import com.DcoDe.jobconnect.dto.JobDTO;
import com.DcoDe.jobconnect.enums.JobStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface JobService {
    JobDTO createJob(JobCreateDTO jobDto);
    JobDTO getJobById(Long id);
    JobDTO getJobByJobId(String jobId);
    JobDTO updateJob(Long id, JobCreateDTO jobDto);
    // Add method to update job by string jobId
    JobDTO updateJobByJobId(String jobId, JobCreateDTO jobDto);
    void deleteJob(Long id);
    // Add method to delete job by string jobId
    void deleteJobByJobId(String jobId);
    Page<JobDTO> getJobsByCompany(Long companyId, JobStatus status, int page, int size);
    Page<JobDTO> searchJobs(String keyword, List<String> jobTypes, int page, int size);
    Page<JobDTO> getJobsBySkills(List<String> skills, int page, int size);
    Page<JobDTO> getCurrentEmployerJobs(int page, int size);
    void changeJobStatus(Long id, JobStatus status);
    void changeJobStatusByJobId(String jobId, JobStatus status);
    List<JobDTO> getFeaturedJobs();
    
}
