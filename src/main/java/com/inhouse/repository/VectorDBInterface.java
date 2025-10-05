package com.inhouse.repository;

import java.util.List;

import dev.langchain4j.rag.content.Content;

/**
 * Interface for vector database operations.
 */
public interface VectorDBInterface {

    public void storeText(String text);
    public List<Content> retrieveData(String query);
    public void deleteAll(); 
}
