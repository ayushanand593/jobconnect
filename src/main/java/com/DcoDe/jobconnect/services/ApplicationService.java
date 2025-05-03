package com.DcoDe.jobconnect.services;



import com.DcoDe.jobconnect.dto.ApplicationDTO;
import com.DcoDe.jobconnect.enums.ApplicationStatus;
import org.springframework.data.domain.Page;

public interface ApplicationService {
    Page<ApplicationDTO> getApplicationsByJob(Long jobId, int page, int size);
    Page<ApplicationDTO> getApplicationsByJobAndStatus(Long jobId, ApplicationStatus status, int page, int size);
    ApplicationDTO getApplicationById(Long id);
    void updateApplicationStatus(Long id, ApplicationStatus status);
    Page<ApplicationDTO> getCompanyApplications(int page, int size);
}
