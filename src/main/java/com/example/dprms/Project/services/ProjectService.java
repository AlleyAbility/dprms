package com.example.dprms.Project.services;

import com.example.dprms.Project.Project;
import com.example.dprms.Project.repository.ProjectRepository;
import com.example.dprms.exception.ProjectAlreadyExistsException;
import com.example.dprms.exception.UserAlreadyExistsException;
import com.example.dprms.exception.UserNotFoundException;
import com.example.dprms.user.User;
import com.example.dprms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService implements IProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }



    @Override
    public Project createProject(Project theProject) {
        // Check if a project with the same name exists in the same institution
        Optional<Project> existingProject = projectRepository.findByProjectNameAndInstitutionName(
                theProject.getProjectName(), theProject.getInstitutionName()
        );

        if (existingProject.isPresent()) {
            throw new ProjectAlreadyExistsException("Project with the same name already exists in the institution.");
        }

        //assigning the user to project

        return projectRepository.save(theProject);
    }

//
//    @Override
//    public Project createProject(Project theProject) {
//        Optional<Project> checkProject = projectRepository.findByName(theProject.getProjectName());
//        Optional<Project> checkInstitution = projectRepository.findByInstitution(theProject.getInstitutionName());
//        if (checkProject.isPresent() && checkInstitution.isPresent()){
//            throw new RoleAlreadyExistException(checkProject.get().getProjectManager()+ " Project already exist");
//        }
//        return projectRepository.save(theProject);
//    }

    @Override
    public void deleteProject(Long projectId) {
        this.removeAllUserFromProject(projectId);
        projectRepository.deleteById(projectId);
    }

    @Override
    public Project findByProjectName(String projectName) {
        return projectRepository.findByProjectName(projectName).get();
    }

    @Override
    public Optional<Project> findByProjectNameAndInstitutionName(String projectName, String institutionName) {
        return projectRepository.findByProjectNameAndInstitutionName(projectName, institutionName);
    }

    @Override
    public Project findById(Long projectId) {
        return projectRepository.findById(projectId).get();
    }

    @Override
    public User removeUserFromProject(Long userId, Long projectId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isPresent() && project.get().getUsers().contains(user.get())) {
            project.get().removeUserFromProject(user.get());
            projectRepository.save(project.get());
            return user.get();
        }
        throw new UserNotFoundException("User not found!");
    }

    @Override
    public User assignUserToProject(Long userId, Long projectId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Project> project = projectRepository.findById(projectId);
        if (user.isPresent() && user.get().getProjects().contains(project.get())){
            throw new UserAlreadyExistsException(
                    user.get().getFirstName()+ " is already assigned to the " + project.get().getProjectName() +" project");
        }
        project.ifPresent(theProject -> theProject.assignUserToRole(user.get()));
        projectRepository.save(project.get());
        return user.get();
    }

    @Override
    public Project removeAllUserFromProject(Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        project.ifPresent(Project::removeAllUsersFromProject);
        return projectRepository.save(project.get());
    }

}
