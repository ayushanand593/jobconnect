package com.DcoDe.jobconnect.repositories;

import com.DcoDe.jobconnect.entities.Application;
import com.DcoDe.jobconnect.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ApplicationRepository extends JpaRepository <Application,Long> {

    Page<Application> findByCandidateId(Long candidateId, Pageable pageable);
    Page<Application> findByJobId(Long jobId, Pageable pageable);
    Page<Application> findByJobIdAndStatus(Long jobId, ApplicationStatus status, Pageable pageable);

    @Query("SELECT a FROM Application a WHERE a.job.id = :jobId AND a.candidate.id = :candidateId")
    Optional<Application> findByJobIdAndCandidateId(Long jobId, Long candidateId);

    @Query("SELECT COUNT(a) > 0 FROM Application a WHERE a.job.id = :jobId AND a.candidate.id = :candidateId")
    boolean existsByJobIdAndCandidateId(Long jobId, Long candidateId);

    @Query("SELECT a FROM Application a WHERE a.job.company.id = :companyId")
    Page<Application> findByCompanyId(Long companyId, Pageable pageable);

    List<Application> findAllByJobIdIn(Collection<Long> jobIds);

    @Query("SELECT DISTINCT a.candidate.id FROM Application a WHERE a.job.company.id = :companyId")
    Set<Long> findCandidateIdsByCompanyId(Long companyId);

    @Query("SELECT COUNT(a) > 0 FROM Application a WHERE a.candidate.id = :candidateId AND a.job.company.id = :companyId")
    boolean existsByCandidateIdAndCompanyId(
            @Param("candidateId") Long candidateId,
            @Param("companyId") Long companyId);

//    @Query("SELECT COUNT(a) > 0 FROM Application a WHERE a.candidate.id = :candidateId AND a.job.company.id = :companyId")
//    boolean existsByCandidateIdAndCompanyId(Long candidateId, Long companyId);


}
