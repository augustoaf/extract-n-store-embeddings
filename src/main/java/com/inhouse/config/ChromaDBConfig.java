package com.inhouse.config;

import dev.langchain4j.store.embedding.chroma.ChromaApiVersion;

/**
 * Configuration settings for ChromaDB connection and usage.
 */
public class ChromaDBConfig {

    public static final String CHROMA_BASE_URL = "http://localhost:8000"; // ChromaDB server URL
    public static final ChromaApiVersion API_VERSION = ChromaApiVersion.V2; // ChromaDB API version
    public static final String DEFAULT_COLLECTION_NAME = "documents"; // default collection to use  
    public static final int DEFAULT_MAX_RESULTS = 5; // default max results for retrieval

}