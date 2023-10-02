package com.example.dprms.document.service;

import com.example.dprms.document.Document;
import com.example.dprms.document.repository.DocumentRepository;
import com.example.dprms.exception.DocumentNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentService implements IDocumentService {

    @Autowired
    private final DocumentRepository documentRepository;

    @Override
    public List<Document> getDocuments() {
        return documentRepository.findAll(); // Assuming you have a repository for Document
    }

    @Override
    public List<Document> findByProjectId(Long projectId) {
        return documentRepository.findByProjectId(projectId);
    }

    @Override
    public Optional<Document> findByProjectIdAndDocumentName(Long projectId, String documentName) {
        return documentRepository.findByProjectIdAndDocumentName(projectId, documentName);
    }

    @Override
    public boolean delete(Long id) {
        Document document = getDocumentById(id);
        try {
            documentRepository.delete(document);
            return true; // Deletion was successful
        } catch (Exception e) {
            return false; // Deletion was not successful
        }
    }

    @Override
    public Document updateDocument(Long id, Document updatedDocument) {
        Optional<Document> existingDocument = documentRepository.findById(id);

        if (existingDocument.isPresent()) {
            Document documentToUpdate = existingDocument.get();

            // Update the fields you want to change
            if (updatedDocument.getDocumentName() != null) {
                documentToUpdate.setDocumentName(updatedDocument.getDocumentName());
            }

            // Save the updated document
            return documentRepository.save(documentToUpdate);
        } else {
            // Handle the case where the document with the given ID is not found
            throw new DocumentNotFoundException("Document not found with ID: " + id);
        }
    }

    // Create a new document
    public Document createDocument(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public  Optional<Document> findByProjectIdAndDocumentTitle(Long projectId, String documentTitle) {
        return documentRepository.findByProjectIdAndDocumentTitle(projectId, documentTitle);
    }

    // Retrieve a document by ID
    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found with ID: " + id));
    }
}

