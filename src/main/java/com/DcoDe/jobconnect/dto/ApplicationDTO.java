package com.DcoDe.jobconnect.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationDTO {
    private Long id;
    // private Long jobId;
    private String jobId;
    private String jobTitle;
    private String companyName;
    private String candidateName;
    private String resumeUrl;
    private String coverLetter;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
