package com.DcoDe.jobconnect.services;


import com.DcoDe.jobconnect.dto.*;
import com.DcoDe.jobconnect.entities.Company;
import com.DcoDe.jobconnect.entities.User;
import org.springframework.data.domain.Page;


import java.util.Map;
import java.util.Optional;

public interface CompanyService {
    CompanyDetailDTO registerCompany(CompanyRegistrationDTO dto);
    Optional<Company> findByCompanyUniqueId(String companyUniqueId);
    CompanyDetailDTO getCompanyByUniqueId(String companyUniqueId);
    CompanyDetailDTO getCurrentCompanyProfile();
    CompanyDetailDTO updateCompanyProfile(CompanyProfileUpdateDTO profileDTO);
    EmployerProfileDTO addEmployerToCompany(JoinCompanyDTO dto);
    boolean isCompanyAdmin(User user, String companyName);
    Page<CompanyDetailDTO> searchCompanies(String keyword, int page, int size);

}

