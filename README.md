# EmailGuard

**EmailGuard** is a Spring Boot application designed to process and validate email content based on predefined semantic rules. The application checks the provided email text against a set of rules (e.g., grammatical accuracy, semantic clarity, market impact, etc.), and returns the text with feedback on which parts of the content pass or breach the rules. Breached sections are highlighted in red, and suggestions for corrections are provided.

## Features
- Accepts email content as text via a RESTful API.
- Processes the email content based on a predefined set of rules.
- Highlights sections of the email content that breach the rules using HTML tags (e.g., red for breaches, green for valid sections).
- Provides alternative suggestions for breached content.
- Flexible rule configuration via API, allowing dynamic updates to the rules set.

## Technologies Used
- **Spring Boot**: For building the RESTful API and managing application logic.
- **Spring Data JPA**: For database interactions, allowing persistence of rules and logs.
- **H2 Database**: A lightweight in-memory database for development purposes.
- **OpenAI API**: For advanced semantic analysis and text processing.
- **JavaMailSender (optional)**: For sending processed emails (if needed).
- **JUnit/Mockito**: For testing and mocking external dependencies.

## Getting Started

### Prerequisites
- Java 11 or higher
- Maven or Gradle (for dependency management)
- IntelliJ IDEA or any preferred Java IDE

### Clone the Repository
```bash
git clone https://github.com/your-username/emailguard.git
cd emailguard
```

### Install Dependencies
For Maven:
```bash
mvn clean install
```
For Gradle:
```bash
./gradlew build
```

### Running the Application
To run the application, use the following command:
```bash
mvn spring-boot:run
```
or if using Gradle:
```bash
./gradlew bootRun
```

The application will start on port 8080 by default. You can access the API at `http://localhost:8080`.

### API Endpoints
#### POST `/process-email`
- **Description**: Accepts an email's text content and processes it against a set of rules.
- **Request Body**:
  ```json
  {
    "emailContent": "Your email text here",
    "rules": ["rule1", "rule2", "rule3"]
  }
  ```
- **Response**:
  - If the email passes all the rules:
    ```json
    {
      "status": "success",
      "processedEmail": "Your email content with passed rules highlighted in green."
    }
    ```
  - If there are rule breaches:
    ```json
    {
      "status": "error",
      "processedEmail": "Your email content with breached rules highlighted in red.",
      "suggestions": [
        "Suggested correction for breached part #1",
        "Suggested correction for breached part #2"
      ]
    }
    ```

### Testing
Unit tests are available for the project, and you can run them using:

For Maven:
```bash
mvn test
```

For Gradle:
```bash
./gradlew test
```

### Additional Configuration
You may modify the set of rules by updating the configuration files or using the API to add/update rules dynamically.

### License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
