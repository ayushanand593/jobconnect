package com.DcoDe.jobconnect.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class CompanyProfileUpdateDTO {
    @NotBlank(message = "Company name is required")
    private String companyName;

    @Size(max = 1000,message = "Description must be less than 1000 characters")
    private String description;

    @URL(message = "Invalid website URL")
    private String website;

    private String industry;
    private String size;
    private String logoUrl;
}
