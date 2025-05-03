package com.DcoDe.jobconnect.dto;




import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CandidateSearchDTO {
    private Long id;
    private String fullName;
    private String headline;
    private String summary;
    private Integer experienceYears;
    private String resumeUrl;
    private List<SkillDTO> skills;
    private LocalDateTime joinedDate;
    private Boolean hasApplied; // Whether the candidate has applied to any of the company's jobs
}
