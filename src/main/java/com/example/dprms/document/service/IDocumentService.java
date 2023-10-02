package com.example.dprms.document.service;

import com.example.dprms.document.Document;

import java.util.List;
import java.util.Optional;


public interface IDocumentService {
    List<Document> getDocuments();
    List<Document> findByProjectId(Long projectId);
    Optional<Document> findByProjectIdAndDocumentName(Long projectId, String s);
    boolean delete(Long id);
    Document updateDocument(Long id, Document updatedDocument);
    Document createDocument(Document document);

    Optional<Document> findByProjectIdAndDocumentTitle(Long projectId, String documentTitle);


}
