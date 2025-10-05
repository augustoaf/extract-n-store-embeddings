package com.inhouse;

import java.io.IOException;
import java.util.List;

import com.inhouse.repository.VectorDBFactory;
import com.inhouse.repository.VectorDBInterface;
import com.inhouse.service.ExtractionService;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.rag.content.Content;

public class ExtractStoreApplication {

    public static void main(String[] args) {

        String filePath = "D:\\repos\\AI-embeddings\\extract-store\\input\\sample.txt";
        
        List<TextSegment> textSegments;

        try {
            textSegments = ExtractionService.extractFromFile(filePath);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return;
        }

        // Print the resulting chunks
        System.out.println("Document split into " + textSegments.size() + " chunks.");
        System.out.println("----------------------------------------");

        for (int i = 0; i < textSegments.size(); i++) {
            System.out.println("Chunk " + (i + 1) + ":");
            System.out.println(textSegments.get(i).text());
            System.out.println("----------------------------------------");
        }

        VectorDBInterface chromaRepo = VectorDBFactory.getChromaDBRepository();
        
        // clear existing data in the default collection
        chromaRepo.deleteAll(); 

        // Store text chunks in Chroma DB
        for (TextSegment segment : textSegments) {
            chromaRepo.storeText(segment.text());
        }

        // query from Chroma DB
        String query = "Who is Peter?";
        System.out.println("\n ################ Query: " + query + " ################ \n");
        List<Content> retrievedContent = chromaRepo.retrieveData(query);
         
        for (Content content : retrievedContent) {

            System.out.println("Retrieved content: " + content.textSegment().text());
            System.out.println("Metadata:");
            
            content.metadata().forEach((key, value) -> {
                System.out.println(key + ": " + value);
            });
        }
            
    }
}