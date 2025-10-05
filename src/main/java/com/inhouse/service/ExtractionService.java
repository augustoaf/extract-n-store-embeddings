package com.inhouse.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

import com.inhouse.config.ExtractionConfig;

public class ExtractionService {
    
    /**
     * Extract text from a file and split it into smaller chunks.
     * 
     * @param file the path to the input file
     * @return a list of text segments
     * @throws IOException if there is an error reading the file
     */
    public static List<TextSegment> extractFromFile(String file) throws IOException {

        // Load the text file
        Path filePath = Paths.get(file);
        Document document = FileSystemDocumentLoader.loadDocument(filePath);

        // Define the separators for chunking
        // LangChain's RecursiveCharacterTextSplitter is a great choice as it tries to split
        // by larger chunks first and falls back to smaller ones if the chunk is still too big.
        // We will prioritize sentences and then fall back to words.
        List<String> separators = List.of("\n\n", "\n", ". ", "? ", "! ", " ", "");

        // Create a Tokenizer for the ChromaDB default embedding model
        // This class acts as both an EmbeddingModel and a TokenCountEstimator
        //Tokenizer tokenizer = new OnnxEmbeddingModel();
        //TO DO: use custom separators for the text? or split by token instead of chars?
        //List<TextSegment> textSegments = DocumentSplitters.recursive(500, 50, separators).split(document);
        

        // Split the document into text chunks
        List<TextSegment> textSegments = DocumentSplitters.recursive(
            ExtractionConfig.MAX_SEGMENT_SIZE_CHARS, 
            ExtractionConfig.MAX_OVERLAP_SIZE_CHARS)
            .split(document);

        return textSegments;
    }
}
        