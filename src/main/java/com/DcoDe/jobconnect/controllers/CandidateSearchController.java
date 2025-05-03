package com.DcoDe.jobconnect.controllers;




import com.DcoDe.jobconnect.dto.CandidateSearchDTO;
import com.DcoDe.jobconnect.services.CandidateSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling candidate search operations by employers
 */
@RestController
@RequestMapping("/api/candidate-search")
@RequiredArgsConstructor
public class CandidateSearchController {

    private final CandidateSearchService candidateSearchService;

    /**
     * Search for candidates based on various criteria
     *
     * @param keyword Keyword to search in candidate profile (name, headline, summary)
     * @param skills List of skills to match
     * @param minExperience Minimum years of experience
     * @param maxExperience Maximum years of experience
     * @param page Page number (zero-based)
     * @param size Page size
     * @return Page of candidate search results
     */
    @GetMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Page<CandidateSearchDTO>> searchCandidates(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<String> skills,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<CandidateSearchDTO> candidates = candidateSearchService.searchCandidates(
                keyword, skills, minExperience, maxExperience, page, size);
        return ResponseEntity.ok(candidates);
    }

    /**
     * Get detailed information about a specific candidate
     *
     * @param candidateId ID of the candidate
     * @return Candidate details including applied status for this employer
     */
    @GetMapping("/{candidateId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<CandidateSearchDTO> getCandidateDetails(@PathVariable Long candidateId) {
        CandidateSearchDTO candidate = candidateSearchService.getCandidateDetails(candidateId);
        return ResponseEntity.ok(candidate);
    }
}