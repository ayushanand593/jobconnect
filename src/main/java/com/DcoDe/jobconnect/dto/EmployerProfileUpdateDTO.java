package com.DcoDe.jobconnect.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmployerProfileUpdateDTO {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String phone;
    private String jobTitle;
    // private String profilePictureUrl; 
}
