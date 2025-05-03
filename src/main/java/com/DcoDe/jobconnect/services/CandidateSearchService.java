package com.DcoDe.jobconnect.services;


import com.DcoDe.jobconnect.dto.CandidateSearchDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CandidateSearchService {
    Page<CandidateSearchDTO> searchCandidates(String keyword, List<String> skills, Integer minExperience,
                                              Integer maxExperience, int page, int size);
    CandidateSearchDTO getCandidateDetails(Long candidateId);
}
