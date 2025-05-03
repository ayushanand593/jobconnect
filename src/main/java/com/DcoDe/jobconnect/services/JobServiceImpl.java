package com.DcoDe.jobconnect.services;



import com.DcoDe.jobconnect.Exceptions.ResourceNotFoundException;
import com.DcoDe.jobconnect.dto.JobCreateDTO;
import com.DcoDe.jobconnect.dto.JobDTO;
import com.DcoDe.jobconnect.dto.SkillDTO;
import com.DcoDe.jobconnect.entities.Job;
import com.DcoDe.jobconnect.entities.Skill;
import com.DcoDe.jobconnect.entities.User;
import com.DcoDe.jobconnect.enums.JobStatus;
import com.DcoDe.jobconnect.enums.JobType;
import com.DcoDe.jobconnect.repositories.JobRepository;
import com.DcoDe.jobconnect.repositories.SkillRepository;
import com.DcoDe.jobconnect.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    @Override
    @Transactional
    public JobDTO createJob(JobCreateDTO jobDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null || currentUser.getCompany() == null) {
            throw new AccessDeniedException("Not authorized to create jobs");
        }

        // Create new job entity
        Job job = new Job();
        job.setTitle(jobDto.getTitle());
        job.setLocation(jobDto.getLocation());
        job.setJobType(JobType.valueOf(jobDto.getJobType()));
        job.setExperienceRequired(jobDto.getExperienceRequired());
        job.setDescription(jobDto.getDescription());
        job.setRequirements(jobDto.getRequirements());
        job.setResponsibilities(jobDto.getResponsibilities());
        job.setSalaryRange(jobDto.getSalaryRange());
        job.setStatus(JobStatus.OPEN);
        job.setCompany(currentUser.getCompany());
        job.setPostedBy(currentUser);

        // Generate a unique job ID
        job.setJobId(generateJobId(currentUser.getCompany().getCompanyName(), jobDto.getTitle()));

        // Handle skills
        if (jobDto.getSkills() != null && !jobDto.getSkills().isEmpty()) {
            Set<Skill> skills = new HashSet<>();
            for (String skillName : jobDto.getSkills()) {
                Skill skill = skillRepository.findByNameIgnoreCase(skillName)
                        .orElseGet(() -> {
                            Skill newSkill = new Skill();
                            newSkill.setName(skillName);
                            return skillRepository.save(newSkill);
                        });
                skills.add(skill);
            }
            job.setSkills(skills);
        }

        // Save job
        job = jobRepository.save(job);

        return mapToJobDTO(job);
    }

    @Override
    public JobDTO getJobById(Long id) {
        Job job = jobRepository.findByIdWithSkills(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
        return mapToJobDTO(job);
    }

    @Override
    @Transactional
    public JobDTO updateJob(Long id, JobCreateDTO jobDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Not authorized to update jobs");
        }

        Job job = jobRepository.findByIdWithSkills(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));

        // Check if user has permission to update this job
        if (!job.getCompany().getId().equals(currentUser.getCompany().getId())) {
            throw new AccessDeniedException("Not authorized to update this job");
        }

        // Update job details
        job.setTitle(jobDto.getTitle());
        job.setLocation(jobDto.getLocation());
        job.setJobType(JobType.valueOf(jobDto.getJobType()));
        job.setExperienceRequired(jobDto.getExperienceRequired());
        job.setDescription(jobDto.getDescription());
        job.setRequirements(jobDto.getRequirements());
        job.setResponsibilities(jobDto.getResponsibilities());
        job.setSalaryRange(jobDto.getSalaryRange());

        // Update skills
        if (jobDto.getSkills() != null) {
            job.getSkills().clear();
            for (String skillName : jobDto.getSkills()) {
                Skill skill = skillRepository.findByNameIgnoreCase(skillName)
                        .orElseGet(() -> {
                            Skill newSkill = new Skill();
                            newSkill.setName(skillName);
                            return skillRepository.save(newSkill);
                        });
                job.getSkills().add(skill);
            }
        }

        // Save updated job
        job = jobRepository.save(job);

        return mapToJobDTO(job);
    }

    @Override
    @Transactional
    public void deleteJob(Long id) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Not authorized to delete jobs");
        }

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));

        // Check if user has permission to delete this job
        if (!job.getCompany().getId().equals(currentUser.getCompany().getId())) {
            throw new AccessDeniedException("Not authorized to delete this job");
        }

        jobRepository.delete(job);
    }

    @Override
    public Page<JobDTO> getJobsByCompany(Long companyId, JobStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Job> jobs = jobRepository.findByCompanyIdAndStatus(companyId, status, pageable);
        return jobs.map(this::mapToJobDTO);
    }

    @Override
    public Page<JobDTO> searchJobs(String keyword, List<String> jobTypeStrings, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        List<JobType> jobTypes = null;
        if (jobTypeStrings != null && !jobTypeStrings.isEmpty()) {
            jobTypes = jobTypeStrings.stream()
                    .map(JobType::valueOf)
                    .collect(Collectors.toList());
        }

        Page<Job> jobs = jobRepository.searchJobs(keyword, jobTypes, pageable);
        return jobs.map(this::mapToJobDTO);
    }

    @Override
    public Page<JobDTO> getJobsBySkills(List<String> skills, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Job> jobs = jobRepository.findBySkillNames(skills, pageable);
        return jobs.map(this::mapToJobDTO);
    }

    @Override
    public Page<JobDTO> getCurrentEmployerJobs(int page, int size) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Not authorized");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Job> jobs = jobRepository.findByPostedBy(currentUser.getId(), pageable);
        return jobs.map(this::mapToJobDTO);
    }

    @Override
    @Transactional
    public void changeJobStatus(Long id, JobStatus status) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new AccessDeniedException("Not authorized to update job status");
        }

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));

        // Check if user has permission to update this job
        if (!job.getCompany().getId().equals(currentUser.getCompany().getId())) {
            throw new AccessDeniedException("Not authorized to update this job status");
        }

        job.setStatus(status);
        jobRepository.save(job);
    }

    @Override
    public List<JobDTO> getFeaturedJobs() {
        List<Job> featuredJobs = jobRepository.findFeaturedJobs();
        return featuredJobs.stream()
                .map(this::mapToJobDTO)
                .collect(Collectors.toList());
    }

    private String generateJobId(String companyName, String jobTitle) {
        String baseId = companyName.replaceAll("\\s+", "-").toLowerCase() + "-" +
                jobTitle.replaceAll("\\s+", "-").toLowerCase();
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(6);
        return baseId + "-" + timestamp;
    }

    private JobDTO mapToJobDTO(Job job) {
        JobDTO dto = new JobDTO();
        dto.setId(job.getId());
        dto.setJobId(job.getJobId());
        dto.setTitle(job.getTitle());
        dto.setCompanyName(job.getCompany().getCompanyName());
        dto.setCompanyId(job.getCompany().getId());
        dto.setLocation(job.getLocation());
        dto.setJobType(job.getJobType().name());
        dto.setExperienceRequired(job.getExperienceRequired());
        dto.setDescription(job.getDescription());
        dto.setRequirements(job.getRequirements());
        dto.setResponsibilities(job.getResponsibilities());
        dto.setSalaryRange(job.getSalaryRange());
        dto.setStatus(job.getStatus().name());
        dto.setCreatedAt(job.getCreatedAt());
        dto.setUpdatedAt(job.getUpdatedAt());

        // Map skills
        if (job.getSkills() != null) {
            List<SkillDTO> skillDTOs = job.getSkills().stream()
                    .map(skill -> {
                        SkillDTO skillDTO = new SkillDTO();
                        skillDTO.setId(skill.getId());
                        skillDTO.setName(skill.getName());
                        return skillDTO;
                    })
                    .collect(Collectors.toList());
            dto.setSkills(skillDTOs);
        }

        return dto;
    }
}
