package com.devahn.document.repository;

import com.devahn.document.domain.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository {

    Long save(Document document);

    Optional<Document> findById(Long id);

    List<Document> findAll();

    int update(Document document);

    int deleteById(Long id);

    List<Document> searchByVector(float[] queryVector, int limit);
}
