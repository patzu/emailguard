# EmailGuard

**EmailGuard** is a Spring Boot application designed to process and validate email content based on predefined semantic rules. The application checks the provided email text against a set of rules (e.g., grammatical accuracy, semantic clarity, potential risks, etc.), and returns the text with feedback on which parts of the content pass or breach the rules. Sections that breach the rules are highlighted in different colors (e.g., red for critical breaches, yellow for minor issues, and green for valid sections). Suggestions for corrections are also provided for problematic parts of the email.

## Features
- Accepts email content as text via a RESTful API.
- Processes email content based on a predefined set of rules.
- Highlights sections of the email content that breach the rules using HTML tags (e.g., red for breaches, yellow for risky, and green for safe).
- Provides alternative suggestions for breached content in parentheses.
- Flexible rule configuration via API, allowing dynamic updates to the rules set.
- Integration with OpenAI API for advanced semantic analysis and text processing.

## Technologies Used
- **Spring Boot**: For building the RESTful API and managing application logic.
- **OpenAI API**: For advanced semantic analysis and text processing.
- **JUnit/Mockito**: For testing and mocking external dependencies.

## Getting Started

### Prerequisites
- Java 21 or higher
- Maven 
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

### Running the Application
To run the application, use the following command:
```bash
mvn spring-boot:run
```

The application will start on port 8080 by default. You can access the API at `http://localhost:8080`.

### API Endpoints
#### POST `/process-email`
- **Description**: Accepts an email's text content and processes it against a set of rules to identify breaches, determine severity (color coding), and provide suggestions for corrections.
- **Request Body**:
  ```json
  {
    "content": "Your email text here",
    "rules": [
      "Ensure professional language is used",
      "Check for risky or ambiguous language that could cause issues",
      "Make sure the instructions are clear and actionable"
    ]
  }
  ```

```Example email and rules
{
  "content": "Dear Asset Manager,\n\nI hope this email finds you well. We need you to look into our current asset portfolio and make necessary adjustments. Please review the following and manage the assets accordingly:\n\n1. The asset performance seems low this quarter, could you review it and suggest some corrective actions?\n2. I want you to make sure our current investments are optimally aligned with market conditions, but we should also maintain the current investment in tech stocks.\n3. Kindly diversify into sectors that could yield high returns, particularly in the energy sector, where we have not invested much.\n4. You should also evaluate any risky moves made in the last quarter.\n5. Let’s be cautious about potential losses in volatile sectors.\n6. Also, ensure that any new strategies are well documented, and potential risks are communicated.\n7. Review current deadlines and make sure we’re on track.\n\nThank you, and I await your feedback and suggestions.\n\nBest regards,\n[Your Name]",
  "rules": [
    "Ensure that all instructions are clear and precise. Avoid ambiguity or vague statements that could be misinterpreted.",
    "Use consistent terminology throughout the email. Avoid using different terms for the same concept, as it may lead to confusion.",
    "Every request or action in the email should be specific and actionable, with clear timelines or conditions attached.",
    "The language should remain formal and professional, avoiding casual or overly friendly phrases that may undermine the seriousness of the communication.",
    "Any decisions involving risk (e.g., financial or legal) should be backed by a clear assessment or recommendation. Avoid making high-risk suggestions without proper analysis or safeguards.",
    "Ensure that all data and figures provided in the email are accurate and up-to-date. Double-check numerical values, dates, and financial information to avoid errors.",
    "Any actions proposed should be compliant with legal or regulatory requirements. Ensure that all necessary legal checks have been considered and documented.",
    "Clearly outline the next steps, responsibilities, and deadlines. The recipient should know exactly what is expected of them after reading the email.",
    "Structure the email in a way that separates different topics or instructions. Use headings, bullet points, or numbered lists to make the content more readable and organized.",
    "Ensure the email is free from spelling, grammatical, or typographical errors. Always proofread before sending to ensure the professionalism and accuracy of the communication."
  ]
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
      "processedEmail": "Your email content with breached rules highlighted in red and yellow.",
      "suggestions": [
        "Suggested correction for the red-flagged sentence.",
        "Suggested correction for the yellow-flagged sentence."
      ]
    }
    ```

### Testing
Unit tests are available for the project, and you can run them using:

For Maven:
```bash
mvn test
```

### Additional Configuration
You may modify the set of rules by updating the configuration files or using the API to add/update rules dynamically.

### License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
