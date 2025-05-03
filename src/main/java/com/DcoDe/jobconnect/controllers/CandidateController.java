package com.DcoDe.jobconnect.controllers;


import com.DcoDe.jobconnect.dto.CandidateProfileDTO;
import com.DcoDe.jobconnect.dto.CandidateProfileUpdateDTO;
import com.DcoDe.jobconnect.dto.SkillDTO;
import com.DcoDe.jobconnect.services.CandidateService;
import com.DcoDe.jobconnect.services.FileStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateService candidateService;
    private final FileStorageService fileStorageService;

    @GetMapping("/{id}")
    public ResponseEntity<CandidateProfileDTO> getCandidateById(@PathVariable Long id) {
        return ResponseEntity.ok(candidateService.getCandidateById(id));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateProfileDTO> getCurrentProfile() {
        return ResponseEntity.ok(candidateService.getCurrentCandidateProfile());
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateProfileDTO> updateProfile(
            @Valid @RequestBody CandidateProfileUpdateDTO profileDTO) {
        return ResponseEntity.ok(candidateService.updateCandidateProfile(profileDTO));
    }

    @PostMapping("/resume")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateProfileDTO> uploadResume(
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(candidateService.uploadResume(file));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CandidateProfileDTO>> searchCandidates(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<String> skills,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(candidateService.searchCandidates(keyword, skills, page, size));
    }

    @PostMapping("/skills")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<SkillDTO>> addSkills(@RequestBody List<String> skills) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(candidateService.addSkillsToCandidate(skills));
    }

    @GetMapping("/skills")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<SkillDTO>> getCandidateSkills() {
        return ResponseEntity.ok(candidateService.getCandidateSkills());
    }

    @DeleteMapping("/skills/{skillId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Void> removeSkill(@PathVariable Long skillId) {
        candidateService.removeSkillFromCandidate(skillId);
        return ResponseEntity.noContent().build();
    }
}
