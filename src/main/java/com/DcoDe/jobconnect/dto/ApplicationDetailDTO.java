package com.DcoDe.jobconnect.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationDetailDTO {
    private Long id;
    private String jobId;
    private String jobTitle;
    private String companyName;
    private String resumeUrl;
    private String coverLetter;
    private String status;
    private LocalDateTime appliedDate;
    private LocalDateTime lastUpdated;
}