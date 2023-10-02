package com.example.dprms.document;

import com.example.dprms.document.repository.DocumentRepository;
import com.example.dprms.document.service.DocumentService;
import com.example.dprms.project.Project;
import com.example.dprms.project.repository.ProjectRepository;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


//@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class DocumentController {

        @Autowired
        private DocumentService documentService;
        @Autowired
        private ProjectRepository projectRepository;
        @Autowired
        private DocumentRepository documentRepository;

        @Autowired
        private ModelMapper modelMapper;


        @GetMapping("/documents/all")
        public ResponseEntity<List<Document>> getDocuments() {
                List<Document> documents = documentService.getDocuments();
                return new ResponseEntity<>(documents, HttpStatus.FOUND);
        }


        @GetMapping("/documents/{projectId}")
        public ResponseEntity<List<Document>> getByProjectId(@PathVariable("projectId") Long projectId){
                List<Document> documents = documentService.findByProjectId(projectId);

                if (documents.isEmpty()) {
                        return ResponseEntity.notFound().build(); // No documents found for the given project ID
                } else {
                        return ResponseEntity.ok(documents);
                }
        }



        @GetMapping("/documents/{projectId}/{documentTitle}")
        public ResponseEntity<Document> getDocument(
                @PathVariable Long projectId,
                @PathVariable String documentTitle) throws IOException {

                // Retrieve the document from the database based on projectId and documentTitle
                Optional<Document> optionalDocument = documentService.findByProjectIdAndDocumentTitle(projectId, documentTitle);

                if (optionalDocument.isPresent()) {
                        Document document = optionalDocument.get();
//                        String filePath = document.getFilePath(); // Get the file path from the database
//                        Path path = Paths.get(filePath);
//                        Resource resource = new UrlResource(path.toUri());

                    return ResponseEntity.ok().body(document);
                }

                return ResponseEntity.badRequest().build();
        }


        @GetMapping("/documents/upload/{filename:.+}")
        public ResponseEntity<Resource> downloadDocument(@PathVariable String filename) {
                // Assuming your documents are stored in the specified directory
                Path filePath = Paths.get("C:\\Users\\HP\\Desktop\\EGA-PROJECT\\development\\backend\\dprmsEGAZ\\src\\main\\java\\com\\example\\dprms\\upload\\" + filename);

                // Check if the file exists
                if (!Files.exists(filePath)) {
                        return ResponseEntity.notFound().build();
                }

                // Load the file as a resource
                Resource resource = new FileSystemResource(filePath.toFile());

                // Create a response with the file contents
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF) // Set the appropriate content type
                        .body(resource);
        }




        @PostMapping("/documents/update/{id}")
        public ResponseEntity<Document> updateDocument(
                @PathVariable("id") Long id,
                @RequestBody Document updatedDocument) {
                Document updated = documentService.updateDocument(id, updatedDocument);
                return ResponseEntity.ok(updated);
        }



        @PostMapping("/documents/upload")
        public ResponseEntity<?> handleFileUpload(
                @RequestParam("file") MultipartFile file,
                @RequestParam("projectId") Long projectId,
                @RequestParam("documentTitle") String documentTitle
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
                        Optional<Document> existingDocument = documentRepository.findByProjectIdAndDocumentName(projectId, file.getOriginalFilename());

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

                        if (!".pdf".equals(fileExtension)) {
                                return ResponseEntity.badRequest().body("Only PDF documents are allowed.");
                        }

                        // Create a new Document entity and populate its fields
                        Document document = new Document();
                        document.setDocumentName(file.getOriginalFilename());
                        document.setProject(projectOptional.get()); // Set the Project entity
                        document.setDocumentTitle(documentTitle);
                        // Construct the file path
//                        String fileName = file.getOriginalFilename();
                        String uploadDir = "C:\\Users\\HP\\Desktop\\EGA-PROJECT\\development\\backend\\dprmsEGAZ\\src\\main\\java\\com\\example\\dprms\\";
//
//                        // Transfer the file to the destination
//                        try {
//                                file.transferTo(new File(filePath));

                        // Generate a unique filename (e.g., UUID) and construct the relative file path
                        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
                        String relativeFilePath = "upload/" + uniqueFileName;

                        // Get the absolute file path
                        String absoluteFilePath = Paths.get(uploadDir).resolve(relativeFilePath).normalize().toString();

                        // Transfer the file to the destination
                        try {
                                file.transferTo(new File(absoluteFilePath));
                        } catch (Exception e) {

                                e.printStackTrace(); // Log the exception details
                                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the file.");
                        }

                        // Save the document entity to your database
//                        document.setFilePath(filePath); // Set the full file path
//                        documentRepository.save(document);
//
//                        return ResponseEntity.ok("File uploaded and saved successfully.");

                        // Set the relative file path in the Document entity
                        document.setFilePath(relativeFilePath);

                        // Save the document entity to your database
                        documentRepository.save(document);

                        // Return the URL with the relative file path
//                        String fileUrl = "/api/v1/documents/upload/" + uniqueFileName;
                        return ResponseEntity.ok("File uploaded and saved successfully.");

                } catch (IOException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload and save the file.");
                }
        }


        @DeleteMapping("/documents/delete/{id}")
        public ResponseEntity<String> deleteDocument(@PathVariable("id") Long documentId) {
                boolean deletionSuccessful = documentService.delete(documentId);

                if (deletionSuccessful) {
                        return ResponseEntity.ok("Document deleted successfully.");
                } else {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete the document.");
                }
        }

}
