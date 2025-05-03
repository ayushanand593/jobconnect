package com.DcoDe.jobconnect.services;


import com.DcoDe.jobconnect.Exceptions.ResourceNotFoundException;
import com.DcoDe.jobconnect.dto.EmployerProfileDTO;
import com.DcoDe.jobconnect.dto.EmployerProfileUpdateDTO;
import com.DcoDe.jobconnect.entities.Company;
import com.DcoDe.jobconnect.entities.EmployerProfile;
import com.DcoDe.jobconnect.entities.User;
import com.DcoDe.jobconnect.enums.UserRole;
import com.DcoDe.jobconnect.repositories.CompanyRepository;
import com.DcoDe.jobconnect.repositories.EmployerProfileRepository;
import com.DcoDe.jobconnect.repositories.UserRepository;
import com.DcoDe.jobconnect.utils.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployerServiceImpl implements EmployerService {

    private final EmployerProfileRepository employerProfileRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final FileStorageService fileStorageService;

    @Override
    public EmployerProfileDTO getCurrentEmployerProfile() {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null || !currentUser.getRole().equals(UserRole.EMPLOYER)) {
            throw new AccessDeniedException("Not authorized to access employer profile");
        }

        EmployerProfile profile = employerProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employer profile not found"));

        return mapToEmployerProfileDTO(profile);
    }
    @Override
    public EmployerProfileDTO getEmployerProfileDTOByUserId(Long userId) {
        EmployerProfile profile = employerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Employer profile not found"));

        return mapToEmployerProfileDTO(profile);
    }

    @Override
    @Transactional
    public EmployerProfileDTO updateProfile(EmployerProfileUpdateDTO dto) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null || !currentUser.getRole().equals(UserRole.EMPLOYER)) {
            throw new AccessDeniedException("Not authorized to update employer profile");
        }

        EmployerProfile profile = employerProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employer profile not found"));

        // Update fields
        profile.setFirstName(dto.getFirstName());
        profile.setLastName(dto.getLastName());
        profile.setPhone(dto.getPhone());
        profile.setJobTitle(dto.getJobTitle());
        // profile.setProfilePictureUrl(dto.getProfilePictureUrl()); 

        // Save updated profile
        profile = employerProfileRepository.save(profile);

        return mapToEmployerProfileDTO(profile);
    }
     

    @Override
    @Transactional
    public EmployerProfileDTO updateProfilePicture(MultipartFile file) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null || !currentUser.getRole().equals(UserRole.EMPLOYER)) {
            throw new AccessDeniedException("Not authorized to update employer profile");
        }

        EmployerProfile profile = employerProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employer profile not found"));




        // Save updated profile
        profile = employerProfileRepository.save(profile);

        return mapToEmployerProfileDTO(profile);
    }

    @Override
    public Optional<EmployerProfile> findProfileByUserId(Long userId) {
        return employerProfileRepository.findByUserId(userId);
    }

    @Override
    public boolean isCompanyMember(User user, Long companyId) {
        if (user == null || user.getCompany() == null) {
            return false;
        }

        return user.getCompany().getId().equals(companyId);
    }

    @Override
    @Transactional
    public void promoteToAdmin(Long employerId) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null || currentUser.getCompany() == null) {
            throw new AccessDeniedException("Not authorized to promote employers");
        }

        // Check if current user is an admin
        Company company = companyRepository.findWithAdminsById(currentUser.getCompany().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        if (!company.getAdmins().contains(currentUser)) {
            throw new AccessDeniedException("Only company admins can promote other employers");
        }

        // Find the employer to promote
        User employer = userRepository.findById(employerId)
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found"));

        // Check if employer belongs to the same company
        if (!isCompanyMember(employer, company.getId())) {
            throw new AccessDeniedException("Can only promote employers from your company");
        }

        // Add employer to admins list
        company.getAdmins().add(employer);
        companyRepository.save(company);
    }

    @Override
    @Transactional
    public void demoteFromAdmin(Long employerId) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null || currentUser.getCompany() == null) {
            throw new AccessDeniedException("Not authorized to demote employers");
        }

        // Check if current user is an admin
        Company company = companyRepository.findWithAdminsById(currentUser.getCompany().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        if (!company.getAdmins().contains(currentUser)) {
            throw new AccessDeniedException("Only company admins can demote other employers");
        }

        // Find the employer to demote
        User employer = userRepository.findById(employerId)
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found"));

        // Check if employer belongs to the same company
        if (!isCompanyMember(employer, company.getId())) {
            throw new AccessDeniedException("Can only demote employers from your company");
        }

        // Don't allow demoting yourself if you're the last admin
        if (employer.getId().equals(currentUser.getId()) && company.getAdmins().size() <= 1) {
            throw new AccessDeniedException("Cannot demote yourself as the last admin");
        }

        // Remove employer from admins list
        company.getAdmins().remove(employer);
        companyRepository.save(company);
    }

    private EmployerProfileDTO mapToEmployerProfileDTO(EmployerProfile profile) {
        EmployerProfileDTO dto = new EmployerProfileDTO();
        dto.setId(profile.getId());
        dto.setFirstName(profile.getFirstName());
        dto.setLastName(profile.getLastName());
        dto.setEmail(profile.getUser().getEmail());
        dto.setPhone(profile.getPhone());
        dto.setJobTitle(profile.getJobTitle());
        dto.setProfilePictureUrl(profile.getProfilePictureUrl()); 

        Company company = profile.getUser().getCompany();
        if (company != null) {
            dto.setCompanyName(company.getCompanyName());
            dto.setCompanyId(company.getId());
        }

        return dto;
    }
}
