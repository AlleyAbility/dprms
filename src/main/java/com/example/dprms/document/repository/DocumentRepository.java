package com.example.dprms.document.repository;

import com.example.dprms.document.Document;
import com.example.dprms.document.DocumentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByProjectId(Long id);

    Optional<Document> findByProjectIdAndDocumentName(Long projectId, String s);
}
