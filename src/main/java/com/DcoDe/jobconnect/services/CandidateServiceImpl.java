package com.DcoDe.jobconnect.services;


import com.DcoDe.jobconnect.Exceptions.ResourceNotFoundException;
import com.DcoDe.jobconnect.dto.CandidateProfileDTO;
import com.DcoDe.jobconnect.dto.CandidateProfileUpdateDTO;
import com.DcoDe.jobconnect.dto.CandidateRegistrationDTO;
import com.DcoDe.jobconnect.dto.SkillDTO;
import com.DcoDe.jobconnect.entities.Candidate;
import com.DcoDe.jobconnect.entities.Skill;
import com.DcoDe.jobconnect.entities.User;
import com.DcoDe.jobconnect.enums.UserRole;
import com.DcoDe.jobconnect.repositories.CandidateRepository;
import com.DcoDe.jobconnect.repositories.SkillRepository;
import com.DcoDe.jobconnect.repositories.UserRepository;
import com.DcoDe.jobconnect.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final PasswordEncoder passwordEncoder;
    // private final FileStorageService fileStorageService;
    private final S3FileStorageService fileStorageService;    

    @Override
    @Transactional
    public CandidateProfileDTO registerCandidate(CandidateRegistrationDTO dto) {
        // Check if email already exists
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create user
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(UserRole.CANDIDATE);

        // Save user to get ID
        user = userRepository.save(user);

        // Create candidate profile
        Candidate candidate = new Candidate();
        candidate.setUser(user);
        candidate.setFirstName(dto.getFirstName());
        candidate.setLastName(dto.getLastName());
        candidate.setPhone(dto.getPhone());
        candidate.setHeadline(dto.getHeadline());
        candidate.setSummary(dto.getSummary());
        candidate.setExperienceYears(dto.getExperienceYears());
        candidate.setSkills(new HashSet<>());

        // Save candidate profile
        candidate = candidateRepository.save(candidate);

        // Associate user with candidate profile
        user.setCandidateProfile(candidate);
        userRepository.save(user);

        // Add skills if provided
        if (dto.getSkills() != null && !dto.getSkills().isEmpty()) {
            Set<Skill> skills = getOrCreateSkills(dto.getSkills());
            candidate.setSkills(skills);
            candidateRepository.save(candidate);
        }

        // Map to DTO and return
        return mapToCandidateProfileDTO(candidate);
    }

    @Override
    public CandidateProfileDTO getCandidateById(Long id) {
        Candidate candidate = candidateRepository.findByIdWithSkills(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with ID: " + id));
        return mapToCandidateProfileDTO(candidate);
    }

    @Override
    public CandidateProfileDTO getCurrentCandidateProfile() {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Not authenticated");
        }

        Candidate candidate = candidateRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found"));

        return mapToCandidateProfileDTO(candidate);
    }

    @Override
    @Transactional
    public CandidateProfileDTO updateCandidateProfile(CandidateProfileUpdateDTO profileDTO) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Not authenticated");
        }

        Candidate candidate = candidateRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found"));

        // Update candidate details
        candidate.setFirstName(profileDTO.getFirstName());
        candidate.setLastName(profileDTO.getLastName());
        candidate.setPhone(profileDTO.getPhone());
        candidate.setHeadline(profileDTO.getHeadline());
        candidate.setSummary(profileDTO.getSummary());
        candidate.setExperienceYears(profileDTO.getExperienceYears());

        if (profileDTO.getResumeUrl() != null && !profileDTO.getResumeUrl().isEmpty()) {
            candidate.setResumeUrl(profileDTO.getResumeUrl());
        }

        // Update skills if provided
        if (profileDTO.getSkills() != null) {
            Set<Skill> skills = getOrCreateSkills(profileDTO.getSkills());
            candidate.setSkills(skills);
        }

        // Save updated candidate
        candidate = candidateRepository.save(candidate);

        return mapToCandidateProfileDTO(candidate);
    }

    @Override
    @Transactional
    public CandidateProfileDTO uploadResume(MultipartFile file) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Not authenticated");
        }

        Candidate candidate = candidateRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found"));

        // Upload the file
        String resumeUrl = fileStorageService.uploadFile(file);

        // Update candidate resume URL
        candidate.setResumeUrl(resumeUrl);
        candidate = candidateRepository.save(candidate);

        return mapToCandidateProfileDTO(candidate);
    }

    @Override
    public Page<CandidateProfileDTO> searchCandidates(String keyword, List<String> skillNames, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // If skills are provided, search by skills
        // Note: This is a simplified implementation. In a real app, you would want to use
        // a more sophisticated query that combines keyword and skills search
        if (skillNames != null && !skillNames.isEmpty()) {
            // Find candidates with the specified skills
            // This would require a custom query in the repository
            Page<Candidate> candidates = candidateRepository.findAll(pageable);
            return candidates.map(this::mapToCandidateProfileDTO);
        }

        // Otherwise, search by keyword in name, headline, etc.
        Page<Candidate> candidates;
        if (keyword != null && !keyword.trim().isEmpty()) {
            // This would require a custom query method in your repository
            candidates = candidateRepository.findAll(pageable);
        } else {
            candidates = candidateRepository.findAll(pageable);
        }

        return candidates.map(this::mapToCandidateProfileDTO);
    }

    @Override
    @Transactional
    public List<SkillDTO> addSkillsToCandidate(List<String> skillNames) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Not authenticated");
        }

        Candidate candidate = candidateRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found"));

        Set<Skill> skills = getOrCreateSkills(skillNames);
        candidate.getSkills().addAll(skills);
        candidate = candidateRepository.save(candidate);

        return mapToSkillDTOs(candidate.getSkills());
    }

    @Override
    public List<SkillDTO> getCandidateSkills() {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Not authenticated");
        }

        Candidate candidate = candidateRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found"));

        return mapToSkillDTOs(candidate.getSkills());
    }

    @Override
    @Transactional
    public void removeSkillFromCandidate(Long skillId) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Not authenticated");
        }

        Candidate candidate = candidateRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found"));

        candidate.getSkills().removeIf(skill -> skill.getId().equals(skillId));
        candidateRepository.save(candidate);
    }

    @Override
    public Optional<Candidate> findCandidateByUserId(Long userId) {
        return candidateRepository.findByUserId(userId);
    }

    private Set<Skill> getOrCreateSkills(List<String> skillNames) {
        Set<Skill> skills = new HashSet<>();

        for (String name : skillNames) {
            if (!StringUtils.hasText(name)) {
                continue;
            }

            Optional<Skill> existingSkill = skillRepository.findByNameIgnoreCase(name.trim());

            if (existingSkill.isPresent()) {
                skills.add(existingSkill.get());
            } else {
                Skill newSkill = new Skill();
                newSkill.setName(name.trim());
                newSkill = skillRepository.save(newSkill);
                skills.add(newSkill);
            }
        }

        return skills;
    }

    private CandidateProfileDTO mapToCandidateProfileDTO(Candidate candidate) {
        CandidateProfileDTO dto = new CandidateProfileDTO();
        dto.setId(candidate.getId());
        dto.setFirstName(candidate.getFirstName());
        dto.setLastName(candidate.getLastName());
        dto.setEmail(candidate.getUser().getEmail());
        dto.setPhone(candidate.getPhone());
        dto.setHeadline(candidate.getHeadline());
        dto.setSummary(candidate.getSummary());
        dto.setExperienceYears(candidate.getExperienceYears());
        dto.setResumeUrl(candidate.getResumeUrl());
        dto.setCreatedAt(candidate.getUser().getCreatedAt());

        List<SkillDTO> skillDTOs = mapToSkillDTOs(candidate.getSkills());
        dto.setSkills(skillDTOs);

        return dto;
    }

    private List<SkillDTO> mapToSkillDTOs(Set<Skill> skills) {
        return skills.stream().map(skill -> {
            SkillDTO dto = new SkillDTO();
            dto.setId(skill.getId());
            dto.setName(skill.getName());
            return dto;
        }).collect(Collectors.toList());
    }
}
