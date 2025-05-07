package com.DcoDe.jobconnect.dto;

import java.util.List;

import lombok.Data;

@Data
public class CandidateSearchRequestDTO {
    private String keyword;
    private List<String> skills;
    private Integer minExperience;
    private Integer maxExperience;
}
