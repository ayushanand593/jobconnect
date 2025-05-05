package com.DcoDe.jobconnect.repositories;

import com.DcoDe.jobconnect.entities.Job;
import com.DcoDe.jobconnect.enums.JobStatus;
import com.DcoDe.jobconnect.enums.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findByCompanyIdAndStatus(Long companyId, JobStatus status, Pageable pageable);

    Optional<Job> findByJobId(String jobId);

    @Query("SELECT j FROM Job j WHERE j.status = 'OPEN' AND " +
            "(:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:jobTypes IS NULL OR j.jobType IN :jobTypes)")
    Page<Job> searchJobs(String keyword, List<JobType> jobTypes, Pageable pageable);

    @Query("SELECT j FROM Job j JOIN j.skills s WHERE j.status = 'OPEN' AND s.name IN :skillNames")
    Page<Job> findBySkillNames(List<String> skillNames, Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.status = 'OPEN' AND j.postedBy.id = :userId ORDER BY j.createdAt DESC")
    Page<Job> findByPostedBy(Long userId, Pageable pageable);

    @Query(value = "SELECT * FROM jobs WHERE status = 'OPEN' ORDER BY created_at DESC LIMIT 5", nativeQuery = true)
    List<Job> findFeaturedJobs();

    @Query("SELECT j FROM Job j JOIN FETCH j.skills WHERE j.id = :id")
    Optional<Job> findByIdWithSkills(Long id);

    List<Job> findAllByPostedById(Long postedById);
    List<Job> findAllByCompanyId(Long companyId);

    
}

