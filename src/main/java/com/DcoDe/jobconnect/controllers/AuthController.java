package com.DcoDe.jobconnect.controllers;



import com.DcoDe.jobconnect.dto.*;
import com.DcoDe.jobconnect.entities.Company;
import com.DcoDe.jobconnect.entities.User;
import com.DcoDe.jobconnect.services.AuthService;
import com.DcoDe.jobconnect.services.CandidateService;
import com.DcoDe.jobconnect.services.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final CompanyService companyService;
    private final CandidateService candidateService;
    private final AuthService authService;

    @PostMapping("/register/company")
    public ResponseEntity<?> registerCompany(@Valid @RequestBody CompanyRegistrationDTO dto) {
        return ResponseEntity.ok(companyService.registerCompany(dto));
    }

    @PostMapping("/register/employer")
    public ResponseEntity<?> joinCompany(@Valid @RequestBody JoinCompanyDTO dto) {
        Company company = companyService.findByCompanyUniqueId(dto.getCompanyUniqueId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Company not found with ID: " + dto.getCompanyUniqueId()
                ));
        return ResponseEntity.ok(companyService.addEmployerToCompany(dto));
    }
    // @PostMapping("/register/candidate")
    // public ResponseEntity<CandidateProfileDTO> registerCandidate(
    //         @Valid @RequestBody CandidateRegistrationDTO dto) {
    //     return ResponseEntity.status(HttpStatus.CREATED)
    //             .body(candidateService.registerCandidate(dto));
    // }
    @PostMapping("/register/candidate")
    public ResponseEntity<CandidateProfileDTO> registerCandidate(
            @Valid @RequestBody CandidateRegistrationDTO dto) {
        
        // Log the request to help diagnose issues
        System.out.println("Received candidate registration request for: " + dto.getEmail());
        
        try {
            CandidateProfileDTO result = candidateService.registerCandidate(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            // Log the error
            System.err.println("Error registering candidate: " + e.getMessage());
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        User user = authService.login(loginDTO.getEmail(), loginDTO.getPassword());
        UserDTO response = mapToDto(user);
        return ResponseEntity.ok(response);
    }
    private UserDTO mapToDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setEmail(user.getEmail());
        dto.setRole(String.valueOf(user.getRole()));

        // Assuming user has a getCompany() that returns a Company object or null
        if (user.getCompany() != null) {
            dto.setCompanyID(user.getCompany().getId());
        }

        return dto;
    }
}
