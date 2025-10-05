package com.inhouse.repository;

import com.inhouse.config.ChromaDBConfig;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;

public class VectorDBFactory {

    private static ChromaDBRepository chromaDBRepositoryInstance = null;

    public static VectorDBInterface getChromaDBRepository() {
        
        if (chromaDBRepositoryInstance == null) {
            // Load AI model 
            EmbeddingModel model = new AllMiniLmL6V2EmbeddingModel();
            
            chromaDBRepositoryInstance = new ChromaDBRepository(ChromaDBConfig.CHROMA_BASE_URL, ChromaDBConfig.COLLECTION_NAME, model);
        }

        return chromaDBRepositoryInstance;
    }
}
