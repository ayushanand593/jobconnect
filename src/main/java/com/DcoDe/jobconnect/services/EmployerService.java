package com.DcoDe.jobconnect.services;



import com.DcoDe.jobconnect.dto.EmployerProfileDTO;
import com.DcoDe.jobconnect.dto.EmployerProfileUpdateDTO;
import com.DcoDe.jobconnect.entities.EmployerProfile;
import com.DcoDe.jobconnect.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface EmployerService {
    EmployerProfileDTO getCurrentEmployerProfile();
    EmployerProfileDTO updateProfile(EmployerProfileUpdateDTO dto);
    EmployerProfileDTO updateProfilePicture(MultipartFile file);
    Optional<EmployerProfile> findProfileByUserId(Long userId);
    EmployerProfileDTO getEmployerProfileDTOByUserId(Long userId);
    boolean isCompanyMember(User user, Long companyId);
    void promoteToAdmin(Long employerId);
    void demoteFromAdmin(Long employerId);

}
