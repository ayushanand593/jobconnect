package com.DcoDe.jobconnect.repositories;

import com.DcoDe.jobconnect.entities.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByUserId(Long userId);

    @Query("SELECT c FROM Candidate c JOIN FETCH c.skills WHERE c.id = :id")
    Optional<Candidate> findByIdWithSkills(Long id);
    @Query("SELECT c FROM Candidate c LEFT JOIN c.skills s WHERE " +
            "(:keyword IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.headline) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.summary) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:skills IS NULL OR s.name IN :skills) AND " +
            "(:minExperience IS NULL OR c.experienceYears >= :minExperience) AND " +
            "(:maxExperience IS NULL OR c.experienceYears <= :maxExperience)")
    Page<Candidate> searchCandidates(String keyword, List<String> skills, Integer minExperience,
                                     Integer maxExperience, Pageable pageable);

    @Query("SELECT DISTINCT c FROM Candidate c JOIN c.skills s " +
            "WHERE s.name IN :skillNames")
    Page<Candidate> findByCandidateSkills(@Param("skillNames") List<String> skillNames, Pageable pageable);

    @Query("SELECT DISTINCT c FROM Candidate c JOIN c.skills s " +
            "WHERE (:keyword IS NULL OR " +
            "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.headline) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.summary) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND s.name IN :skillNames")
    Page<Candidate> searchCandidatesWithSkills(
            @Param("keyword") String keyword,
            @Param("skillNames") List<String> skillNames,
            Pageable pageable);
}

