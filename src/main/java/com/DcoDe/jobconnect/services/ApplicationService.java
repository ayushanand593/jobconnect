package com.DcoDe.jobconnect.services;



import com.DcoDe.jobconnect.dto.ApplicationCreateDTO;
import com.DcoDe.jobconnect.dto.ApplicationDTO;
import com.DcoDe.jobconnect.enums.ApplicationStatus;

import java.util.List;

import org.springframework.data.domain.Page;

public interface ApplicationService {
    // Page<ApplicationDTO> getApplicationsByJob(Long jobId, int page, int size);
    // Page<ApplicationDTO> getApplicationsByJobAndStatus(Long jobId, ApplicationStatus status, int page, int size);
    List<ApplicationDTO> getApplicationsForJob(String jobId);
    ApplicationDTO applyToJob(String jobId, ApplicationCreateDTO applicationCreateDTO);
    Page<ApplicationDTO> getApplicationsByJob(String jobId, int page, int size);  // Changed from Long to String
    Page<ApplicationDTO> getApplicationsByJobAndStatus(String jobId, ApplicationStatus status, int page, int size);  // Changed from Long to String 
    ApplicationDTO getApplicationById(Long id);
    void updateApplicationStatus(Long id, ApplicationStatus status);
    Page<ApplicationDTO> getCompanyApplications(int page, int size);
}
