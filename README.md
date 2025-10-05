# extract-store

## Overview
The `extract-store` project is a Spring Boot application designed to extract text from files and persist the extracted data into Chroma DB. This application leverages LangChain for text extraction and provides a RESTful API for interaction.

## Features
- Extract text from various file formats.
- Persist extracted text into Chroma DB.
- RESTful API for text extraction and retrieval.

## Project Structure
```
extract-store
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── inhouse
│   │   │           ├── ExtractStoreApplication.java
│   │   │           ├── config
│   │   │           │   └── ChromaDbConfig.java
│   │   │           ├── controller
│   │   │           │   └── ExtractController.java
│   │   │           ├── service
│   │   │           │   ├── ExtractionService.java
│   │   │           │   └── ChromaDbService.java
│   │   │           └── model
│   │   │               └── ExtractedText.java
│   │   └── resources
│   │       └── application.properties
│   └── test
│       └── java
│           └── com
│               └── inhouse
│                   └── ExtractStoreApplicationTests.java
├── pom.xml
└── README.md
```

## Setup Instructions
1. **Clone the repository**:
   ```
   git clone <repository-url>
   cd extract-store
   ```

2. **Build the project**:
   ```
   mvn clean install
   ```

3. **Run the application**:
   ```
   mvn spring-boot:run
   ```

## Usage
- **Extract Text**: Send a POST request to `/extract` with the file to extract text from.
- **Get Extracted Texts**: Send a GET request to `/extracted-texts` to retrieve all extracted texts.

## Dependencies
- Spring Boot
- LangChain
- Chroma DB

## License
This project is licensed under the MIT License.