package com.DcoDe.jobconnect.services;


import com.DcoDe.jobconnect.Exceptions.ResourceNotFoundException;
import com.DcoDe.jobconnect.dto.CandidateSearchDTO;
import com.DcoDe.jobconnect.dto.CandidateSearchRequestDTO;
import com.DcoDe.jobconnect.dto.SkillDTO;
import com.DcoDe.jobconnect.entities.Candidate;
import com.DcoDe.jobconnect.entities.User;
import com.DcoDe.jobconnect.repositories.ApplicationRepository;
import com.DcoDe.jobconnect.repositories.CandidateRepository;
import com.DcoDe.jobconnect.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateSearchServiceImpl implements CandidateSearchService {

    private final CandidateRepository candidateRepository;
    private final ApplicationRepository applicationRepository;

    // @Override
    // public Page<CandidateSearchDTO> searchCandidates(String keyword, List<String> skills, Integer minExperience,
    //                                                  Integer maxExperience, int page, int size) {
    //     User currentUser = SecurityUtils.getCurrentUser();
    //     if (currentUser == null || currentUser.getCompany() == null) {
    //         throw new AccessDeniedException("Not authorized to search candidates");
    //     }

    //     Long companyId = currentUser.getCompany().getId();
    //     Pageable pageable = PageRequest.of(page, size);

    //     // Use repository methods to search for candidates based on criteria
    //     Page<Candidate> candidates = candidateRepository.searchCandidates(keyword, skills, minExperience,
    //             maxExperience, pageable);

    //     // Get list of candidate IDs who have applied to company jobs
    //     Set<Long> appliedCandidateIds = applicationRepository.findCandidateIdsByCompanyId(companyId);

    //     return candidates.map(candidate -> mapToCandidateSearchDTO(candidate, appliedCandidateIds));
    // }
    @Override
public Page<CandidateSearchDTO> searchCandidates(CandidateSearchRequestDTO request, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Long skillsCount = request.getSkills() != null ? Long.valueOf(request.getSkills().size()) : null;
    
    return candidateRepository.searchCandidates(
            request.getKeyword(),
            request.getSkills(),
            skillsCount,
            request.getMinExperience(),  // This will filter candidates with experience >= 8
            request.getMaxExperience(),
            pageable
    ).map(this::mapToCandidateSearchDTO);
}

    @Override
    public CandidateSearchDTO getCandidateDetails(Long candidateId) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null || currentUser.getCompany() == null) {
            throw new AccessDeniedException("Not authorized to view candidate details");
        }

        Long companyId = currentUser.getCompany().getId();

        Candidate candidate = candidateRepository.findByIdWithSkills(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + candidateId));

        // Check if the candidate has applied to any of the company's jobs
        boolean hasApplied = applicationRepository.existsByCandidateIdAndCompanyId(candidateId, companyId);

        CandidateSearchDTO dto = new CandidateSearchDTO();
        dto.setId(candidate.getId());
        dto.setFullName(candidate.getFirstName() + " " + candidate.getLastName());
        dto.setHeadline(candidate.getHeadline());
        dto.setSummary(candidate.getSummary());
        dto.setExperienceYears(candidate.getExperienceYears());
        dto.setResumeUrl(candidate.getResumeUrl());
        dto.setJoinedDate(candidate.getUser().getCreatedAt());
        dto.setHasApplied(hasApplied);

        // Map skills
        List<SkillDTO> skillDTOs = candidate.getSkills().stream()
                .map(skill -> {
                    SkillDTO skillDTO = new SkillDTO();
                    skillDTO.setId(skill.getId());
                    skillDTO.setName(skill.getName());
                    return skillDTO;
                })
                .collect(Collectors.toList());
        dto.setSkills(skillDTOs);

        return dto;
    }

    private CandidateSearchDTO mapToCandidateSearchDTO(Candidate candidate, Set<Long> appliedCandidateIds) {
        CandidateSearchDTO dto = new CandidateSearchDTO();
        dto.setId(candidate.getId());
        dto.setFullName(candidate.getFirstName() + " " + candidate.getLastName());
        dto.setHeadline(candidate.getHeadline());
        dto.setSummary(candidate.getSummary());
        dto.setExperienceYears(candidate.getExperienceYears());
        dto.setJoinedDate(candidate.getUser().getCreatedAt());
        dto.setHasApplied(appliedCandidateIds.contains(candidate.getId()));

        // Map skills
        List<SkillDTO> skillDTOs = candidate.getSkills().stream()
                .map(skill -> {
                    SkillDTO skillDTO = new SkillDTO();
                    skillDTO.setId(skill.getId());
                    skillDTO.setName(skill.getName());
                    return skillDTO;
                })
                .collect(Collectors.toList());
        dto.setSkills(skillDTOs);

        return dto;
    }

    private CandidateSearchDTO mapToCandidateSearchDTO(Candidate candidate) {
        CandidateSearchDTO dto = new CandidateSearchDTO();
        dto.setId(candidate.getId());
        dto.setFullName(candidate.getFirstName() + " " + candidate.getLastName());
        dto.setHeadline(candidate.getHeadline());
        dto.setSummary(candidate.getSummary());
        dto.setExperienceYears(candidate.getExperienceYears());
        dto.setJoinedDate(candidate.getUser().getCreatedAt());
        dto.setHasApplied(false); // Default value when appliedCandidateIds is not provided

        // Map skills
        List<SkillDTO> skillDTOs = candidate.getSkills().stream()
                .map(skill -> {
                    SkillDTO skillDTO = new SkillDTO();
                    skillDTO.setId(skill.getId());
                    skillDTO.setName(skill.getName());
                    return skillDTO;
                })
                .collect(Collectors.toList());
        dto.setSkills(skillDTOs);

        return dto;
    }
}
