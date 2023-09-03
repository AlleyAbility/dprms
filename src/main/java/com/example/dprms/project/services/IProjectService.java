package com.example.dprms.project.services;

import com.example.dprms.project.Project;
import com.example.dprms.user.User;

import java.util.List;
import java.util.Optional;


public interface IProjectService {
    List<Project> getAllProjects();
    Project createProject(Project theProject);
    void deleteProject(Long projectId);
    Project findByProjectName(String projectName);
//    project findByInstitution(String institutionName);
    Optional<Project> findByProjectNameAndInstitutionName(String projectName, String institutionName);
    Project findById(Long projectId);
    User removeUserFromProject(Long userId, Long projectId);
    User assignUserToProject(Long userId, Long projectId);
    Project removeAllUserFromProject(Long projectId);

}
