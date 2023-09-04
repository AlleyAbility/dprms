package com.example.dprms.project;

import com.example.dprms.project.services.IProjectService;
import com.example.dprms.user.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ModelMapper modelMapper;

    private final IProjectService projectService;

    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects(){
        return new ResponseEntity<>(projectService.getAllProjects(), FOUND);
    }
    @PostMapping("/create")
    public ResponseEntity<Project> createProject(@RequestBody Project project){
        return new ResponseEntity<>(projectService.createProject(project), CREATED);
    }

    @GetMapping("/{id}")
    public Project getById(@PathVariable("id") Long projectId){
        return  projectService.findById(projectId);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProject(@PathVariable("id") Long projectId){
        projectService.deleteProject(projectId);
    }
    @PostMapping("/remove-all-users-from-project/{id}")
    public Project removeAllUsersFromProject(@PathVariable("id") Long projectId){
        return projectService.removeAllUserFromProject(projectId);
    }
    @PostMapping("/remove-user-from-project")
    public User removeUserFromProject(@RequestParam("userId")Long userId,
                                   @RequestParam("projectId") Long projectId){
        return projectService.removeUserFromProject(userId, projectId);
    }

    @PostMapping("/assign-user-to-project")
    public User assignUserToProject(@RequestParam("userId")Long userId,
                                 @RequestParam("projectId") Long projectId){
        return projectService.assignUserToProject(userId, projectId);
    }
}
