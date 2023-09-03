package com.example.dprms.document;

import com.example.dprms.document.repository.DocumentRepository;
import com.example.dprms.document.service.DocumentService;
import com.example.dprms.project.Project;
import com.example.dprms.project.repository.ProjectRepository;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/documents")
public class DocumentController {

        @Autowired
        private DocumentService documentService;
        @Autowired
        private ProjectRepository projectRepository;
        @Autowired
        private DocumentRepository documentRepository;

        @Autowired
        private ModelMapper modelMapper;

        @GetMapping("/all")
        public ResponseEntity<List<Document>> getDocuments() {
                List<Document> documents = documentService.getDocuments();
                return new ResponseEntity<>(documents, HttpStatus.FOUND);
        }


        @GetMapping("/{projectId}")
        public Optional<Document> getByProjectId(@PathVariable("projectId") Long projectId){
            return  documentService.findByProjectId(projectId);
        }

        @PostMapping("/update/{id}")
        public ResponseEntity<Document> updateDocument(
                @PathVariable("id") Long id,
                @RequestBody Document updatedDocument) {
                Document updated = documentService.updateDocument(id, updatedDocument);
                return ResponseEntity.ok(updated);
        }



        @PostMapping("/create/concept")
        public ResponseEntity<?> handleConceptFileUpload(
                @RequestParam("file") MultipartFile file,
                @RequestParam("projectId") Long projectId
        ) {
                try {
                        // Check if the file is empty
                        if (file.isEmpty()) {
                                return ResponseEntity.badRequest().body("Please select a file to upload.");
                        }

                        // Check if the file size exceeds 3MB
                        if (file.getSize() > 3 * 1024 * 1024) {
                                return ResponseEntity.badRequest().body("File size exceeds the maximum allowed size (3MB).");
                        }

                        // Check if a document with the same project ID and filename already exists
                        Optional<Document> existingDocument = documentRepository.findByProjectIdAndDocumentName(projectId, file.getOriginalFilename() + " Concept Note");

                        if (existingDocument.isPresent()) {
                                return ResponseEntity.badRequest().body("A document with the same project ID and filename already exists.");
                        }

                        // Retrieve the Project entity from the database using the projectId
                        Optional<Project> projectOptional = projectRepository.findById(projectId);

                        // Check if the project exists
                        if (projectOptional.isEmpty()) {
                                return ResponseEntity.badRequest().body("Project with ID " + projectId + " does not exist.");
                        }

                        // Check if the file has an allowed extension (e.g., .pdf or .docx)
                        String originalFilename = file.getOriginalFilename();

                        if (originalFilename == null) throw new AssertionError();

                        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();

                        if (!".pdf".equals(fileExtension) && !".docx".equals(fileExtension)) {
                                return ResponseEntity.badRequest().body("Only PDF and Word documents are allowed.");
                        }

                        // Create a new Document entity and populate its fields
                        Document document = new Document();
                        document.setDocumentName(file.getOriginalFilename() + " Concept Note");
                        document.setProject(projectOptional.get()); // Set the Project entity

                        String fileName = file.getOriginalFilename() + " Concept Note";
                        // transfer file
                        try {
                                file.transferTo(new File("C:\\Users\\HP\\Desktop\\EGA-PROJECT\\development\\backend\\dprmsEGAZ\\src\\main\\java\\com\\example\\dprms\\upload\\" + fileName));
                        } catch (Exception e) {
                                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                        }

                        // Save the document entity to your database
                        documentRepository.save(document);

                        return ResponseEntity.ok("File uploaded and saved successfully.");
                } catch (IOException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload and save the file.");
                }
        }


        @PostMapping("/create/tor")
        public ResponseEntity<?> handleTORFileUpload(
                @RequestParam("file") MultipartFile file,
                @RequestParam("projectId") Long projectId
        ) {
                try {
                        // Check if the file is empty
                        if (file.isEmpty()) {
                                return ResponseEntity.badRequest().body("Please select a file to upload.");
                        }

                        // Check if the file size exceeds 3MB
                        if (file.getSize() > 3 * 1024 * 1024) {
                                return ResponseEntity.badRequest().body("File size exceeds the maximum allowed size (3MB).");
                        }

                        // Check if a document with the same project ID and filename already exists
                        Optional<Document> existingDocument = documentRepository.findByProjectIdAndDocumentName(projectId, file.getOriginalFilename() + " Concept Note");

                        if (existingDocument.isPresent()) {
                                return ResponseEntity.badRequest().body("A document with the same project ID and filename already exists.");
                        }

                        // Retrieve the Project entity from the database using the projectId
                        Optional<Project> projectOptional = projectRepository.findById(projectId);

                        // Check if the project exists
                        if (projectOptional.isEmpty()) {
                                return ResponseEntity.badRequest().body("Project with ID " + projectId + " does not exist.");
                        }

                        // Check if the file has an allowed extension (e.g., .pdf or .docx)
                        String originalFilename = file.getOriginalFilename();

                        if (originalFilename == null) throw new AssertionError();

                        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();

                        if (!".pdf".equals(fileExtension) && !".docx".equals(fileExtension)) {
                                return ResponseEntity.badRequest().body("Only PDF and Word documents are allowed.");
                        }

                        // Create a new Document entity and populate its fields
                        Document document = new Document();
                        document.setDocumentName(file.getOriginalFilename() + " Concept Note");
                        document.setProject(projectOptional.get()); // Set the Project entity

                        String fileName = file.getOriginalFilename() + " Concept Note";
                        // transfer file
                        try {
                                file.transferTo(new File("C:\\Users\\HP\\Desktop\\EGA-PROJECT\\development\\backend\\dprmsEGAZ\\src\\main\\java\\com\\example\\dprms\\upload\\" + fileName));
                        } catch (Exception e) {
                                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                        }

                        // Save the document entity to your database
                        documentRepository.save(document);

                        return ResponseEntity.ok("File uploaded and saved successfully.");
                } catch (IOException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload and save the file.");
                }
        }


        @DeleteMapping("/delete/{id}")
        public ResponseEntity<String> deleteDocument(@PathVariable("id") Long documentId) {
                boolean deletionSuccessful = documentService.delete(documentId);

                if (deletionSuccessful) {
                        return ResponseEntity.ok("Document deleted successfully.");
                } else {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete the document.");
                }
        }

}
