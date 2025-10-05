package com.inhouse.repository;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.chroma.ChromaApiVersion;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import java.util.List;

import com.inhouse.config.ChromaDBConfig;

/**
 * Implementation of VectorDBInterface using ChromaDB as the vector database.
 */
public class ChromaDBRepository implements VectorDBInterface {

    private final EmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingModel embeddingModel;
    private final ContentRetriever contentRetriever;

    public ChromaDBRepository(String chromaBaseUrl, String collectionName, EmbeddingModel model, ChromaApiVersion apiVersion) {
        this.embeddingModel = model;
        try {
            
            this.embeddingStore = ChromaEmbeddingStore.builder()
                    .baseUrl(chromaBaseUrl)
                    .collectionName(collectionName)
                    .apiVersion(apiVersion) 
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize ChromaEmbeddingStore: " + e.getMessage());
        }
        
        this.contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(this.embeddingStore)
                .embeddingModel(this.embeddingModel)
                .maxResults(ChromaDBConfig.DEFAULT_MAX_RESULTS) // max number of results to retrieve
                .build();
    }

    public void storeText(TextSegment textSegment) {
        embeddingStore.add(embeddingModel.embed(textSegment).content(), textSegment);
    }

    public List<Content> retrieveData(String query) {

        Query ragQuery = Query.from(query);
        List<Content> retrievedContent = contentRetriever.retrieve(ragQuery);

        return retrievedContent;
    }

    public void deleteAll() {
        // TO DO: remove only from the specified collection
        //embeddingStore.removeAll(List.of(ChromaDBConfig.COLLECTION_NAME));
        embeddingStore.removeAll();
    }
}