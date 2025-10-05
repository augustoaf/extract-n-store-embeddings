# extract-store

## Overview
Firsts steps in a RAG solution - this is a Java application that extracts text from a file (based on segment size, also considering overlap between segments) and persists the extracted data + metadata into a Vector DB (ChromaDB). All using LangChain4j framework for extract from file, store text as embedding and retrieval using semantic search.

## Features
- Extract chunk texts from files.
- Generate embeddings from the extracted text.
- Persist embeddings and text segments into ChromaDB (also the text segment metadata).
- Retrieve relevant content from ChromaDB via semantic search.

## Project Structure
```
extract-store
├── src
│   └── main
│       ├── java
│       │   └── com
│       │       └── inhouse
│       │           ├── ExtractStoreApplication.java
│       │           ├── config
│       │           │   └── ChromaDBConfig.java
│       │           │   └── ExtractionConfig.java
│       │           ├── repository
│       │           │   └── ChromaDBRepository.java
│       │           │   └── VectorDBFactory.java
│       │           │   └── VectorDBInterface.java
│       │           ├── service
│       │           │   └── ExtractionService.java
│       └── resources
│           └── application.properties
├── pom.xml
└── README.md
```

## Setup Instructions

1. **Prerequisites**
   - Java 17+
   - Maven
   - Running ChromaDB instance (with V2 API) 
   
2. **Configure ChromaDB**
   
   - Using Docker, run the following command which will download the image and run the container exposing port 8000
   ```
   docker run -p 8000:8000 --name chromadb-server chromadb/chroma
   ```
   
   - Run the curl heartbeat command to check chorma db connectivity
   ```
   curl http://localhost:8000/api/v2/heartbeat
   ```

3. **Build the project**
   ```sh
   mvn clean install
   ```

4. **Run the application**
   ```sh
   mvn spring-boot:run
   ```
## App Dependencies

- Spring Boot
- LangChain4j
- ChromaDB (V2 API compatible)
- Embedding model: all-minilm-l6-v2 
- (Other dependencies as defined in `pom.xml`)

## Notes

- Config java package has classes with essential and default configurations for Extraction and Segment storage / search