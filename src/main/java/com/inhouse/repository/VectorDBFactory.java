package com.inhouse.repository;

import com.inhouse.config.ChromaDBConfig;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;

/**
 * Factory class to create and manage instances of vector database repositories.
 */
public class VectorDBFactory {

    private static ChromaDBRepository chromaDBRepositoryInstance = null;

    public static VectorDBInterface getChromaDBRepository() {
        
        if (chromaDBRepositoryInstance == null) {
            // Load AI model - will be used for both storing and retrieving (conversion to embeddings) 
            EmbeddingModel model = new AllMiniLmL6V2EmbeddingModel();
            
            chromaDBRepositoryInstance = new ChromaDBRepository(
                ChromaDBConfig.CHROMA_BASE_URL, 
                ChromaDBConfig.DEFAULT_COLLECTION_NAME, 
                model, 
                ChromaDBConfig.API_VERSION);
        }

        return chromaDBRepositoryInstance;
    }
}
