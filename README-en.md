# Java Video Generator

## Project Description
This project, developed in Java, enables the automatic creation of interactive videos based on a keyword provided via an API. The system combines generative AI technologies, natural language processing (NLP), and multimedia manipulation to deliver dynamic, subtitled videos.

## Features
- **Script Generation**: Uses a generative AI API (like OpenAI) to create a script based on the provided keyword.
- **Text Segmentation**: Employs Apache OpenNLP to split the script into sentences.
- **Keyword Extraction**: Integrates a Python microservice that uses spaCy and Named Entity Recognition (NER) to extract keywords from the generated sentences.
- **Image Search**: Leverages Google Custom Search API to fetch images related to the extracted keywords.
- **Video Generation**: Uses the JavaCV platform to create an interactive video with:
  - Images as the background.
  - Subtitles synchronized with the script sentences.
  - Visual effects to make the video more engaging.

## Technologies Used
### Backend
- **Java**: Main language for system orchestration.
- **Spring Framework**: For creating REST APIs.
- **Apache OpenNLP**: Natural language processing for sentence segmentation.
- **JavaCV**: Video generation and manipulation.

### Python Microservice
- **spaCy**: For keyword extraction using Named Entity Recognition (NER).

### Integrations
- **OpenAI API**: Automatic generation of creative text.
- **Google Custom Search API**: Fetches images based on the extracted keywords.

## How It Works
1. **Input**:
   - The user sends a keyword via the REST API.
2. **Script Generation**:
   - The keyword is sent to a generative AI that returns a creative text.
3. **Sentence Segmentation**:
   - The text is processed by OpenNLP to split the script into sentences.
4. **Keyword Extraction**:
   - A Python microservice uses spaCy to identify relevant keywords in each sentence.
5. **Image Search**:
   - The extracted keywords are sent to the Google Custom Search API, which returns matching images.
6. **Video Composition**:
   - The images are used as the background.
   - The script sentences appear as synchronized subtitles.
   - Visual effects make the video more attractive.
7. **Output**:
   - The final video is generated and made available for download or sharing.

## How to Run
### Prerequisites
- JDK 17 or higher.
- Python 3.8 or higher.
- Python libraries: `spacy`, `requests`.
- Maven dependencies for Java: `spring-boot-starter`, `opennlp-tools`, `javacv`.

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/java-video-generator.git
   ```
2. Configure API keys:
   - **OpenAI API**: Insert your key into the configuration file.
   - **Google Custom Search API**: Add the key and CX to the properties file.
3. Install dependencies:
   ```bash
   mvn install
   ```
4. Run the project:
   ```bash
   mvn spring-boot:run
   ```
5. Start the Python microservice:
   ```bash
   python microservice.py
   ```
6. Send a request to the API with the keyword:
   ```bash
   curl -X POST -H "Content-Type: application/json" -d '{"keyword": "example"}' http://localhost:8080/generate-video
   ```

## Contribution
Contributions are welcome! Feel free to open a pull request or report issues in the issues tab.

## License
This project is licensed under the [MIT License](LICENSE).

