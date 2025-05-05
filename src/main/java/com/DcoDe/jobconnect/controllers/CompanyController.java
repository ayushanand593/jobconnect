package com.DcoDe.jobconnect.controllers;


import com.DcoDe.jobconnect.dto.CompanyDetailDTO;
import com.DcoDe.jobconnect.dto.CompanyProfileUpdateDTO;
import com.DcoDe.jobconnect.dto.CompanyRegistrationDTO;
import com.DcoDe.jobconnect.entities.User;
import com.DcoDe.jobconnect.services.CompanyService;
import com.DcoDe.jobconnect.services.FileStorageService;
import com.DcoDe.jobconnect.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final FileStorageService fileStorageService;

    @PostMapping("/register")
    public ResponseEntity<CompanyDetailDTO> registerCompany(@Valid @RequestBody CompanyRegistrationDTO dto) {
        CompanyDetailDTO registeredCompany = companyService.registerCompany(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredCompany);
    }

    @GetMapping("/profile")
    public ResponseEntity<CompanyDetailDTO> getCurrentProfile() {
        return ResponseEntity.ok(companyService.getCurrentCompanyProfile());
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<CompanyDetailDTO> updateProfile(
            @Valid @RequestPart("profile") CompanyProfileUpdateDTO profileDTO,
            @RequestPart(value = "logo", required = false) MultipartFile logoFile) {

        User currentUser = SecurityUtils.getCurrentUser();
        if (!companyService.isCompanyAdmin(currentUser, profileDTO.getCompanyName())) {
            throw new AccessDeniedException("Only company admins can update the profile");
        }

        if (logoFile != null) {
            String logoUrl = fileStorageService.uploadFile(logoFile);
            profileDTO.setLogoUrl(logoUrl);
        }

        return ResponseEntity.ok(companyService.updateCompanyProfile(profileDTO));
    }

    @GetMapping("/{companyUniqueId}")
    public ResponseEntity<CompanyDetailDTO> getCompanyProfile(@PathVariable String companyUniqueId) {
        return ResponseEntity.ok(companyService.getCompanyByUniqueId(companyUniqueId));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CompanyDetailDTO>> searchCompanies(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(companyService.searchCompanies(keyword, page, size));
    }
}
