package com.example.dprms.project;

import com.example.dprms.project.services.IProjectService;
import com.example.dprms.user.User;
import com.example.dprms.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

//@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProjectController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    private final IProjectService projectService;

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getAllProjects(){
        return new ResponseEntity<>(projectService.getAllProjects(), OK);
    }
//    @PostMapping("/projects")
//    public ResponseEntity<Project> createProject(@RequestBody Project project){
//        return new ResponseEntity<>(projectService.createProject(project), OK);
//    }

    @PostMapping("/projects")
    public ResponseEntity<Project> createProject(@RequestBody Map<String, Object> projectData) {
        // Check if the userId is present in the request body
        if (!projectData.containsKey("userId")) {
            return ResponseEntity.status(BAD_REQUEST).body(null); // Return a bad request response
        }

        Long userId = Long.parseLong(projectData.get("userId").toString());

        String projectName = projectData.get("projectName").toString();
        String institutionName = projectData.get("institutionName").toString();

        // Check if a project with the same name exists in the same institution
        Optional<Project> existingProject = projectService.findByProjectNameAndInstitutionName(projectName, institutionName);

        if (existingProject.isPresent()) {
            // A project with the same name already exists in the institution
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        // Create a new Project object and populate its fields using the data from projectData
        Project project = new Project();
        project.setProjectName(projectData.get("projectName").toString());
        project.setProjectManager(projectData.get("projectManager").toString());
        project.setProjectVendor(projectData.get("projectVendor").toString());
        project.setInstitutionName(projectData.get("institutionName").toString());
        project.setProjectSponsor(projectData.get("projectSponsor").toString());
        project.setStatus(projectData.get("status").toString());
        // Set other properties accordingly

        // Create the project
        Project createdProject = projectService.createProject(project);

        // Find the user by ID
        User user = userService.getUserById(userId);

//        projectService.assignUserToProject(userId, createdProject.getId());

        if (user != null) {
            // Associate the user with the project
            user.getProjects().add(createdProject);

        } else {
            // Handle the case where the user with the given ID doesn't exist
            return ResponseEntity.status(NOT_FOUND).build();
        }

        return new ResponseEntity<>(createdProject, OK);
    }


    @GetMapping("/projects/{id}")
    public Project getById(@PathVariable("id") Long projectId){
        return  projectService.findById(projectId);
    }

    @DeleteMapping("/projects/delete/{id}")
    public void deleteProject(@PathVariable("id") Long projectId){
        projectService.deleteProject(projectId);
    }
    @PostMapping("/projects/remove-all-users-from-project/{id}")
    public Project removeAllUsersFromProject(@PathVariable("id") Long projectId){
        return projectService.removeAllUserFromProject(projectId);
    }
    @PostMapping("/projects/remove-user-from-project")
    public User removeUserFromProject(@RequestParam("userId")Long userId,
                                   @RequestParam("projectId") Long projectId){
        return projectService.removeUserFromProject(userId, projectId);
    }

    @PostMapping("/projects/assign-user-to-project")
    public User assignUserToProject(@RequestParam("userId")Long userId,
                                 @RequestParam("projectId") Long projectId){
        return projectService.assignUserToProject(userId, projectId);
    }
}
