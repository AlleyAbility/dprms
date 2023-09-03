package com.example.dprms.project.repository;

import com.example.dprms.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByProjectName(String projectName);
//    Optional<project> findByInstitution(String institutionName);
    Optional<Project> findByProjectNameAndInstitutionName(String projectName, String institutionName);
}
