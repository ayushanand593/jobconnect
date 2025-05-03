package com.DcoDe.jobconnect.services;

import com.DcoDe.jobconnect.dto.CandidateProfileDTO;
import com.DcoDe.jobconnect.dto.CandidateProfileUpdateDTO;
import com.DcoDe.jobconnect.dto.CandidateRegistrationDTO;
import com.DcoDe.jobconnect.dto.SkillDTO;
import com.DcoDe.jobconnect.entities.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CandidateService {
    CandidateProfileDTO registerCandidate(CandidateRegistrationDTO dto);
    CandidateProfileDTO getCandidateById(Long id);
    CandidateProfileDTO getCurrentCandidateProfile();
    CandidateProfileDTO updateCandidateProfile(CandidateProfileUpdateDTO profileDTO);
    CandidateProfileDTO uploadResume(MultipartFile file);
    Page<CandidateProfileDTO> searchCandidates(String keyword, List<String> skills, int page, int size);
    List<SkillDTO> addSkillsToCandidate(List<String> skills);
    List<SkillDTO> getCandidateSkills();
    void removeSkillFromCandidate(Long skillId);
    Optional<Candidate> findCandidateByUserId(Long userId);
}
