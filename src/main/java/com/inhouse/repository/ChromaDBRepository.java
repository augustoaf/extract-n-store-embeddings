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

public class ChromaDBRepository implements VectorDBInterface {

    private final EmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingModel embeddingModel;
    private final ContentRetriever contentRetriever;
    private final int maxResults = 5; // Default max results for retrieval

    public ChromaDBRepository(String chromaBaseUrl, String collectionName, EmbeddingModel model) {
        this.embeddingModel = model;
        try {
            
            this.embeddingStore = ChromaEmbeddingStore.builder()
                    .baseUrl(chromaBaseUrl)
                    .collectionName(collectionName)
                    .apiVersion(ChromaApiVersion.V2) // must be compatible with your ChromaDB server version
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize ChromaEmbeddingStore: " + e.getMessage());
        }
        
        this.contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(this.embeddingStore)
                .embeddingModel(this.embeddingModel)
                .maxResults(maxResults) // max number of results to retrieve
                .build();
    }

    public void storeText(String text) {
        TextSegment segment = TextSegment.from(text);
        embeddingStore.add(embeddingModel.embed(segment).content(), segment);
    }

    public List<Content> retrieveData(String query) {

        Query ragQuery = Query.from(query);
        List<Content> retrievedContent = contentRetriever.retrieve(ragQuery);

        return retrievedContent;
    }

    public void deleteAll() {
        //embeddingStore.removeAll(List.of(ChromaDBConfig.COLLECTION_NAME));
        embeddingStore.removeAll();
    }
}