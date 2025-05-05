package com.DcoDe.jobconnect.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CandidateProfileUpdateDTO {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;
    private String phone;
    private String headline;
    private String summary;
    private Integer experienceYears;
    private String resumeUrl;
    private List<String> skills;
}