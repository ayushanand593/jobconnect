package com.DcoDe.jobconnect.services;



import com.DcoDe.jobconnect.dto.EmployerProfileDTO;
import com.DcoDe.jobconnect.dto.EmployerProfileUpdateDTO;
import com.DcoDe.jobconnect.dto.JobDTO;
import com.DcoDe.jobconnect.entities.EmployerProfile;
import com.DcoDe.jobconnect.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface EmployerService {
    EmployerProfileDTO getCurrentEmployerProfile();
    EmployerProfileDTO updateProfile(EmployerProfileUpdateDTO dto);
    EmployerProfileDTO updateProfilePicture(MultipartFile file);
    Optional<EmployerProfile> findProfileByUserId(Long userId);
    EmployerProfileDTO getEmployerProfileDTOByUserId(Long userId);
    List<JobDTO> getJobsByEmployerId(Long employerId);
    boolean isCompanyMember(User user, Long companyId);
    void promoteToAdmin(Long employerId);
    void demoteFromAdmin(Long employerId);

}
