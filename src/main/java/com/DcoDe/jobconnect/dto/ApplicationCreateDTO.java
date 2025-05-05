package com.DcoDe.jobconnect.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class ApplicationCreateDTO {
    // @NotNull(message = "Job ID is required")
    // private Long jobId;
    // @NotBlank(message = "Job ID is required")  // Changed from @NotNull
    // private String jobId;

    @NotBlank(message = "Resume URL is required")
    private String resumeUrl;

    private String coverLetter;
    private Map<String, Object> voluntaryDisclosures;
}