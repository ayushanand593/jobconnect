package com.DcoDe.jobconnect.services;


import com.DcoDe.jobconnect.Exceptions.ResourceNotFoundException;
import com.DcoDe.jobconnect.dto.ApplicationCreateDTO;
import com.DcoDe.jobconnect.dto.ApplicationDTO;
import com.DcoDe.jobconnect.entities.Application;
import com.DcoDe.jobconnect.entities.Candidate;
import com.DcoDe.jobconnect.entities.Job;
import com.DcoDe.jobconnect.entities.User;
import com.DcoDe.jobconnect.enums.ApplicationStatus;
import com.DcoDe.jobconnect.enums.JobStatus;
import com.DcoDe.jobconnect.repositories.ApplicationRepository;
import com.DcoDe.jobconnect.repositories.JobRepository;
import com.DcoDe.jobconnect.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    // @Override
    // public Page<ApplicationDTO> getApplicationsByJob(Long jobId, int page, int size) {
    //     User currentUser = SecurityUtils.getCurrentUser();
    //     if (currentUser == null) {
    //         throw new AccessDeniedException("Not authorized to view applications");
    //     }

    //     // Verify job belongs to user's company
    //     Job job = jobRepository.findById(jobId)
    //             .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

    //     if (!job.getCompany().getId().equals(currentUser.getCompany().getId())) {
    //         throw new AccessDeniedException("Not authorized to view applications for this job");
    //     }

    //     Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    //     Page<Application> applications = applicationRepository.findByJobId(jobId, pageable);

    //     return applications.map(this::mapToApplicationDTO);
    // }

    // @Override
    // public Page<ApplicationDTO> getApplicationsByJobAndStatus(Long jobId, ApplicationStatus status, int page, int size) {
    //     User currentUser = SecurityUtils.getCurrentUser();
    //     if (currentUser == null) {
    //         throw new AccessDeniedException("Not authorized to view applications");
    //     }

    //     // Verify job belongs to user's company
    //     Job job = jobRepository.findById(jobId)
    //             .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

    //     if (!job.getCompany().getId().equals(currentUser.getCompany().getId())) {
    //         throw new AccessDeniedException("Not authorized to view applications for this job");
    //     }

    //     Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    //     Page<Application> applications = applicationRepository.findByJobIdAndStatus(jobId, status, pageable);

    //     return applications.map(this::mapToApplicationDTO);
    // }

        @Override
        @Transactional
        public ApplicationDTO applyToJob(String jobId, ApplicationCreateDTO applicationCreateDTO) {
            // Fetch the job by jobId
            Job job = jobRepository.findByJobId(jobId)
                    .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + jobId));
    
            // Get the current authenticated candidate
            User currentUser = SecurityUtils.getCurrentUser();
            if (currentUser == null || currentUser.getCandidateProfile() == null) {
                throw new AccessDeniedException("Only candidates can apply for jobs");
            }
    
            Candidate candidate = currentUser.getCandidateProfile();
    
            // Create a new application
            Application application = new Application();
            application.setJob(job);
            application.setCandidate(candidate);
            application.setResumeUrl(applicationCreateDTO.getResumeUrl());
            application.setCoverLetter(applicationCreateDTO.getCoverLetter());
            application.setStatus(ApplicationStatus.SUBMITTED);
            application.setCreatedAt(LocalDateTime.now());
            application.setUpdatedAt(LocalDateTime.now());
    
            // Save the application
            application = applicationRepository.save(application);
    
            // Map the application to ApplicationDTO
            return mapToApplicationDTO(application);
        }


    // 3. Update ApplicationServiceImpl methods
@Override
public Page<ApplicationDTO> getApplicationsByJob(String jobId, int page, int size) {  // Changed from Long to String
    User currentUser = SecurityUtils.getCurrentUser();
    if (currentUser == null) {
        throw new AccessDeniedException("Not authorized to view applications");
    }

    // Verify job belongs to user's company
    Job job = jobRepository.findByJobId(jobId)  // Changed from findById
            .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

    if (!job.getCompany().getId().equals(currentUser.getCompany().getId())) {
        throw new AccessDeniedException("Not authorized to view applications for this job");
    }

    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<Application> applications = applicationRepository.findByJobId(job.getId().toString(), pageable);  // Use internal numeric ID

    return applications.map(this::mapToApplicationDTO);
}


    @Override
    @Transactional(readOnly = true)
    public List<ApplicationDTO> getApplicationsForJob(String jobId) {
        // Fetch the job by jobId
        Job job = jobRepository.findByJobId(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + jobId));

        // Get the current authenticated employer
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null || !job.getPostedBy().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not authorized to view applications for this job");
        }

        // Fetch applications for the job
        List<Application> applications = applicationRepository.findByJobId(jobId);

        // Map applications to ApplicationDTO
        return applications.stream()
                .map(this::mapToApplicationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationDTO getApplicationById(Long id) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Not authorized to view application");
        }

        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));

        // Check if the application belongs to user's company
        if (!application.getJob().getCompany().getId().equals(currentUser.getCompany().getId())) {
            throw new AccessDeniedException("Not authorized to view this application");
        }

        return mapToApplicationDTO(application);
    }

//     @Override
// public Page<ApplicationDTO> getApplicationsByJobAndStatus(String jobId, ApplicationStatus status, int page, int size) {  // Changed from Long to String
//     User currentUser = SecurityUtils.getCurrentUser();
//     if (currentUser == null) {
//         throw new AccessDeniedException("Not authorized to view applications");
//     }

//     // Verify job belongs to user's company
//     Job job = jobRepository.findByJobId(jobId)  // Changed from findById
//             .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

//     if (!job.getCompany().getId().equals(currentUser.getCompany().getId())) {
//         throw new AccessDeniedException("Not authorized to view applications for this job");
//     }

//     Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
//     Page<Application> applications = applicationRepository.findByJobIdAndStatus(job.getId().toString(), status, pageable);  // Use internal numeric ID

//     return applications.map(this::mapToApplicationDTO);
// }
@Override
public Page<ApplicationDTO> getApplicationsByJobAndStatus(String jobId, ApplicationStatus status, int page, int size) {
    User currentUser = SecurityUtils.getCurrentUser();
    if (currentUser == null) {
        throw new AccessDeniedException("Not authorized to view applications");
    }

    // Verify job belongs to user's company
    Job job = jobRepository.findByJobId(jobId)
            .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

    if (!job.getCompany().getId().equals(currentUser.getCompany().getId())) {
        throw new AccessDeniedException("Not authorized to view applications for this job");
    }

    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    // Use the string jobId directly in the repository call
    Page<Application> applications = applicationRepository.findByJobIdAndStatus(jobId, status, pageable);

    return applications.map(this::mapToApplicationDTO);
}

    // @Override
    // @Transactional
    // public void updateApplicationStatus(Long id, ApplicationStatus status) {
    //     User currentUser = SecurityUtils.getCurrentUser();
    //     if (currentUser == null) {
    //         throw new AccessDeniedException("Not authorized to update application status");
    //     }

    //     Application application = applicationRepository.findById(id)
    //             .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));

    //     // Check if the application belongs to user's company
    //     if (!application.getJob().getCompany().getId().equals(currentUser.getCompany().getId())) {
    //         throw new AccessDeniedException("Not authorized to update this application");
    //     }

    //     application.setStatus(status);
    //     applicationRepository.save(application);
    // }
    @Override
@Transactional
public void updateApplicationStatus(Long id, ApplicationStatus status) {
    User currentUser = SecurityUtils.getCurrentUser();
    if (currentUser == null) {
        throw new AccessDeniedException("Not authorized to update application status");
    }

    Application application = applicationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));

    // Check if the application belongs to user's company
    if (!application.getJob().getCompany().getId().equals(currentUser.getCompany().getId())) {
        throw new AccessDeniedException("Not authorized to update this application");
    }

    application.setStatus(status);
    applicationRepository.save(application);
}

    @Override
    public Page<ApplicationDTO> getCompanyApplications(int page, int size) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null || currentUser.getCompany() == null) {
            throw new AccessDeniedException("Not authorized to view company applications");
        }

        Long companyId = currentUser.getCompany().getId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // Custom query required - need to get applications for all jobs in the company
        // This could be implemented in the ApplicationRepository with a custom query
        // For now, we'll assume we have a method to get all applications for a company
        Page<Application> applications = applicationRepository.findByCompanyId(companyId, pageable);

        return applications.map(this::mapToApplicationDTO);
    }

    private ApplicationDTO mapToApplicationDTO(Application application) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(application.getId());
        dto.setJobId(application.getJob().getJobId());
        dto.setJobTitle(application.getJob().getTitle());
        dto.setCompanyName(application.getJob().getCompany().getCompanyName());
        dto.setCandidateName(application.getCandidate().getFirstName() + " " + application.getCandidate().getLastName());
        dto.setCandidateId(application.getCandidate().getId());
        dto.setResumeUrl(application.getResumeUrl());
        dto.setCoverLetter(application.getCoverLetter());
        dto.setStatus(application.getStatus().name());
        dto.setCreatedAt(application.getCreatedAt());
        dto.setUpdatedAt(application.getUpdatedAt());

        return dto;
    }
}
