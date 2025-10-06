package com.inhouse;

import java.io.IOException;
import java.util.List;

import com.inhouse.repository.VectorDBFactory;
import com.inhouse.repository.VectorDBInterface;
import com.inhouse.service.ExtractionService;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.rag.content.Content;

/**
 * Main application class for extracting text from files and storing it in a vector database.
 */
public class ExtractStoreApplication {

    public static void main(String[] args) {

        /* 
        extract text from a file and split it into smaller chunks 
        */
        String filePath = "D:\\repos\\AI-embeddings\\extract-store\\input\\sample.txt";
        String source = "https://pt.wikipedia.org/wiki/Peter_Pan";
        
        List<TextSegment> textSegments;

        try {
            textSegments = ExtractionService.extractFromFile(filePath, source);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return;
        }

        // Print the resulting chunks and their metadata
        System.out.println("Document split into " + textSegments.size() + " chunks.");
        System.out.println("----------------------------------------");
        for (TextSegment segment : textSegments) {
            System.out.println("Chunk:");
            System.out.println(segment.text());
            System.out.println("\n Metadata: " + segment.metadata());
            System.out.println("----------------------------------------");
        }

        /* 
        store the text chunks in a vector database (Chroma DB) 
        */
        VectorDBInterface chromaRepo = VectorDBFactory.getChromaDBRepository();
        
        // clear existing data in the default collection
        chromaRepo.deleteAll(); 

        // Store text chunks in Chroma DB
        for (TextSegment segment : textSegments) {
            chromaRepo.storeText(segment);
        }

        /* 
        test - retrieve data from the vector database based on a query 
        */
        String query = "Who act as Peter Pan?";
        System.out.println("\n ################ Query: " + query + " ################ \n");
        List<Content> retrievedContent = chromaRepo.retrieveData(query);
         
        for (Content content : retrievedContent) {

            // Print the text segment
            System.out.println("----------------------------------------");
            System.out.println("Retrieved content: " + content.textSegment().text());
            //print text segment metadata
            System.out.println("\n Metadata:" + content.textSegment().metadata());
            
            // Print each content metadata key-value pair (embedding_ID and score)
            System.out.println("\n Content metadata:");
            content.metadata().forEach((key, value) -> {
                System.out.println(key + ": " + value);
            });
        }
            
    }
}