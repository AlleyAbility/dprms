package com.example.dprms.Project.repository;

import com.example.dprms.Project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByProjectName(String projectName);
//    Optional<Project> findByInstitution(String institutionName);
    Optional<Project> findByProjectNameAndInstitutionName(String projectName, String institutionName);
}
