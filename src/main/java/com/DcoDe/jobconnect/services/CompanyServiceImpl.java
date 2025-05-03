package com.DcoDe.jobconnect.services;


import com.DcoDe.jobconnect.dto.*;
import com.DcoDe.jobconnect.entities.Company;
import com.DcoDe.jobconnect.entities.EmployerProfile;
import com.DcoDe.jobconnect.entities.User;
import com.DcoDe.jobconnect.enums.UserRole;
import com.DcoDe.jobconnect.repositories.CompanyRepository;
import com.DcoDe.jobconnect.repositories.EmployerProfileRepository;
import com.DcoDe.jobconnect.repositories.UserRepository;
import com.DcoDe.jobconnect.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import com.DcoDe.jobconnect.Exceptions.GlobalExceptionHandler.ApiError;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployerProfileRepository employerProfileRepository;

    @Override
    @Transactional
    public CompanyDetailDTO registerCompany(CompanyRegistrationDTO dto) {
        // Check if email already exists
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Check if company unique ID already exists
        if (companyRepository.findByCompanyUniqueId(dto.getCompanyUniqueId()).isPresent()) {
            throw new RuntimeException("Company ID already exists");
        }

        // Create company
        Company company = new Company();
        company.setCompanyName(dto.getCompanyName());
        company.setCompanyUniqueId(dto.getCompanyUniqueId());
        company.setIndustry(dto.getIndustry());
        company.setSize(dto.getSize());
        company.setWebsite(dto.getWebsite());
        company.setDescription(dto.getDescription());
        company.setAdmins(new HashSet<>());

        // Save company to get ID
        company = companyRepository.save(company);

        // Create user
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(UserRole.EMPLOYER);
        user.setCompany(company);

        // Save user
        user = userRepository.save(user);

        // Add user as admin
        company.getAdmins().add(user);
        companyRepository.save(company);

        // Map to DTO and return
        return mapToCompanyDetailDTO(company);
    }

    @Override
    public Optional<Company> findByCompanyUniqueId(String companyUniqueId) {
        return companyRepository.findByCompanyUniqueId(companyUniqueId);
    }

    @Override
    public CompanyDetailDTO getCompanyByUniqueId(String companyUniqueId) {
        Company company = companyRepository.findByCompanyUniqueId(companyUniqueId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Company not found with ID: " + companyUniqueId
                ));
        return mapToCompanyDetailDTO(company);
    }

    @Override
    public CompanyDetailDTO getCurrentCompanyProfile() {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null || currentUser.getCompany() == null) {
            throw new AccessDeniedException("User not associated with any company");
        }

        return mapToCompanyDetailDTO(currentUser.getCompany());
    }

    @Override
    @Transactional
    public CompanyDetailDTO updateCompanyProfile(CompanyProfileUpdateDTO profileDTO) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null || currentUser.getCompany() == null) {
            throw new AccessDeniedException("User not associated with any company");
        }

        Company company = currentUser.getCompany();

        // Check if user is admin
        if (!isCompanyAdmin(currentUser, company.getCompanyName())) {
            throw new AccessDeniedException("Only company admins can update the profile");
        }

        // Update company details
        company.setCompanyName(profileDTO.getCompanyName());
        company.setDescription(profileDTO.getDescription());
        company.setWebsite(profileDTO.getWebsite());
        company.setIndustry(profileDTO.getIndustry());
        company.setSize(profileDTO.getSize());

        if (profileDTO.getLogoUrl() != null) {
            company.setLogoUrl(profileDTO.getLogoUrl());
        }

        company = companyRepository.save(company);

        return mapToCompanyDetailDTO(company);
    }

    @Override
    public boolean isCompanyAdmin(User user, String companyName) {
        if (user == null || user.getCompany() == null) {
            return false;
        }

        Company company = user.getCompany();

        // Check if company names match and user is in admins list
        return company.getCompanyName().equals(companyName) &&
                company.getAdmins().stream().anyMatch(admin -> admin.getId().equals(user.getId()));
    }

    @Override
    public Page<CompanyDetailDTO> searchCompanies(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Company> companies;
        if (keyword != null && !keyword.trim().isEmpty()) {
            companies = companyRepository.findByCompanyNameContainingIgnoreCase(keyword, pageable);
        } else {
            companies = companyRepository.findAll(pageable);
        }

        return companies.map(this::mapToCompanyDetailDTO);
    }

    private CompanyDetailDTO mapToCompanyDetailDTO(Company company) {
        CompanyDetailDTO dto = new CompanyDetailDTO();
        dto.setId(company.getId());
        dto.setCompanyName(company.getCompanyName());
        dto.setCompanyUniqueId(company.getCompanyUniqueId());
        dto.setIndustry(company.getIndustry());
        dto.setSize(company.getSize());
        dto.setWebsite(company.getWebsite());
        dto.setDescription(company.getDescription());
        dto.setLogoUrl(company.getLogoUrl());
        dto.setCreatedAt(company.getCreatedAt());
        return dto;
    }
    @Override
    @Transactional
    public EmployerProfileDTO addEmployerToCompany(JoinCompanyDTO dto) {
        // Check if email already exists
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Find company by unique ID
        Company company = companyRepository.findByCompanyUniqueId(dto.getCompanyUniqueId())
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Company not found with ID: " + dto.getCompanyUniqueId()
                ));

        // Create user
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(UserRole.EMPLOYER);
        user.setCompany(company);

        // Save user
        user = userRepository.save(user);

        // Create employer profile
        EmployerProfile profile = new EmployerProfile();
        profile.setUser(user);
        profile.setFirstName(dto.getFirstName());
        profile.setLastName(dto.getLastName());

        // Save employer profile
        profile = employerProfileRepository.save(profile);

        // Map to DTO and return
        EmployerProfileDTO profileDTO = new EmployerProfileDTO();
        profileDTO.setId(profile.getId());
        profileDTO.setFirstName(profile.getFirstName());
        profileDTO.setLastName(profile.getLastName());
        profileDTO.setEmail(user.getEmail());
        profileDTO.setCompanyName(company.getCompanyName());
        profileDTO.setCompanyId(company.getId());

        return profileDTO;
    }

}