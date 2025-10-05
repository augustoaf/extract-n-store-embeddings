package com.inhouse.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.data.document.Metadata;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inhouse.config.ExtractionConfig;

public class ExtractionService {
    
    /**
     * Extract text from a file and split it into smaller chunks 
     * considering max segment size and overlap.
     * 
     * @param file the path to the input file
     * @return a list of text segments
     * @throws IOException if there is an error reading the file
     */
    public static List<TextSegment> extractFromFile(String file) throws IOException {

        // Load the text file
        Path filePath = Paths.get(file);
        Document document = FileSystemDocumentLoader.loadDocument(filePath);

        // Split the document into text chunks
        List<TextSegment> textSegments = DocumentSplitters.recursive(
            ExtractionConfig.MAX_SEGMENT_SIZE_CHARS, 
            ExtractionConfig.MAX_OVERLAP_SIZE_CHARS)
            .split(document);

        // Customize metadata - adding a "source" key with the file path as value
        List<TextSegment> textSegmentsWithSource = cutomizeSourceMetadata(textSegments, "source", file);

        return textSegmentsWithSource;
    }

    // Example of customizing metadata - adding a "source" key with the file path as value
    // example of current metadata without "source" key: 
    // Metadata { metadata = {absolute_directory_path=D:\repos\AI-embeddings\extract-store\input, index=8, file_name=sample.txt}
    // example of custom metadata with "source" key:
    // Metadata { metadata = {absolute_directory_path=D:\repos\AI-embeddings\extract-store\input, index=8, file_name=sample.txt, source=D:\repos\AI-embeddings\extract-store\input\sample.txt}
    private static List<TextSegment> cutomizeSourceMetadata(List<TextSegment> textSegments, String key, String value) {
        
        List<TextSegment> segmentsWithSource = new ArrayList<>();
        
        for (TextSegment segment : textSegments) {
            Map<String, Object> existingMetadata = segment.metadata().toMap();
            Map<String, String> newMetadataMap = new HashMap<>();
                
            // Iterate and cast each value to String
            for (Map.Entry<String, Object> entry : existingMetadata.entrySet()) {
                if (entry.getValue() instanceof String) {
                    newMetadataMap.put(entry.getKey(), (String) entry.getValue());
                } else {
                    // Handle non-string values if necessary, e.g., convert to a string representation
                    newMetadataMap.put(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
                
            // Add the new "source" key
            newMetadataMap.put("source", value);
            
            Metadata newMetadata = Metadata.from(newMetadataMap);
            segmentsWithSource.add(new TextSegment(segment.text(), newMetadata));
        }
        
        return segmentsWithSource;
    }

    //TO DO: fix
    /* 
    public static List<TextSegment> extractFromFileWithSeparators(String file) throws IOException {

        // Load the text file
        Path filePath = Paths.get(file);
        Document document = FileSystemDocumentLoader.loadDocument(filePath);

        // Define the separators for chunking
        // LangChain's RecursiveCharacterTextSplitter is a great choice as it tries to split
        // by larger chunks first and falls back to smaller ones if the chunk is still too big.
        // We will prioritize sentences and then fall back to words.
        List<String> separators = List.of("\n\n", "\n", ". ", "? ", "! ", " ", "");

        // 2. Create an instance of the specific embedding model
        // This class acts as both an EmbeddingModel and a Tokenizer/TokenCountEstimator
        AllMiniLmL6V2EmbeddingModel tokenizer = new AllMiniLmL6V2EmbeddingModel();

        // 3. Split the document into text chunks using the Tokenizer
        // The recursive method with a Tokenizer will automatically use an intelligent
        // splitting strategy that respects the model's token limits.
        List<TextSegment> textSegments = DocumentSplitters.recursive(
            ExtractionConfig.MAX_SEGMENT_SIZE_CHARS, 
            ExtractionConfig.MAX_OVERLAP_SIZE_CHARS,
            tokenizer)
            .split(document);

        return textSegments;
    }
    */
}
        