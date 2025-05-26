# Authentication Service - AI Customer Support Agents

## 1. Project Overview

### **Project Name:** AI Customer Support Agents (ACSA) - authentication-service
### **Description:**
In the AI Customer Support Agents microservices design,
Authentication Service is responsible for managing user identity, including customer registration, login, email verification, and API key generation. Upon receiving registration details from the frontend, it forwards data to the downstream Customer Service and triggers an email-based activation workflow.
Once the user activates their account through the email link, a secure API key is generated, stored, and shared with both the Customer Service and frontend. 
For secure and scalable session management, the service uses JWT authentication and integrates with Redis for efficient token caching and revocation. 
To enhance data security, BCrypt is used for password hashing. The service also supports secure document uploads (such as KYC or banking files) via AWS S3, ensuring durable and scalable cloud storage. 
Spring Security is employed to enforce access control over API endpoints. 
Monitoring and observability are enabled through Spring Boot Actuator, offering insights into authentication metrics, token usage, and system health.

## 2. Prerequisites

- **Java Version:** Java 21
- **Spring Boot Version:** 3.0.4
- **Redis:** For token management and caching
- **Build Tool:** Gradle 8.4
- **Database:** MongoDB (for storing user data, API keys, etc.)
- **Email Service:** SMTP server or mail service for sending activation emails
- **Cloud Storage:** AWS S3 (for secure document uploads)
- **Other Tools:**
    - IntelliJ IDEA Community Edition 2023.1.3 (or any preferred IDE)
    - Spring Security (for authentication and authorization)

## 3. Project Setup

### Clone the Repository:
```
git clone https://zapcomai@dev.azure.com/zapcomai/ACSA%20-%20AI%20Customer%20Support%20Agents/_git/OptimusApi_Auth
```

### Build the Project:
```bash
./gradlew clean build
```

### Run the Application:
```bash
./gradlew bootRun
```

## 4. Configuration

### Environment Configurations
The application is configured via the `application.yml` file.
The Key configurations include:
- **Server Port**: `8081`
- **Active Spring Profile**: `dev`
- **Route Configurations** for downstream services:
    - `CUSTOMER_SERVICE_URL=http://localhost:8082`
    - `GATEWAY_SERVICE_URL=http://localhost:8080`
- **Service URLs:**
    - `CUSTOMER_SERVICE_URL = http://localhost:8082`
- **JWT Settings:**
    - Secret key and token expiration configurations for secure JWT generation and validation
- **Redis Settings:**
    - Host and port configurations for token caching and management
- **AWS S3 Configuration:**
    - Bucket name and AWS credentials for secure document uploads
- **Mail Settings:**
    - SMTP host, port, username, and password for sending activation emails
- **CORS Settings:**
    - Allowed Methods: `GET, POST, PUT, DELETE, OPTIONS`
    - Allowed Headers: `*`
    - Allow Credentials: `true`


## 5. Directory Structure

```
Authentication Service Project Directory Structure
--------------------------------------------------
├── .gradle/
├── .idea/
├── build/
├── gradle/
├── scripts/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── zapcom/
│   │   │           ├── AuthenticationServiceApplication.java
│   │   │           ├── advice/
│   │   │           │   └── AuthServiceExceptionHandler.java
│   │   │           ├── configuration/
│   │   │           │   ├── RedisConfiguration.java
│   │   │           │   ├── SecurityConfig.java
│   │   │           │   ├── UserDetails.java
│   │   │           │   └── UserDetailsService.java
│   │   │           ├── constants/
│   │   │           │   └── MessageConstants.java
│   │   │           ├── controller/
│   │   │           │   └── CustomerAuthenticationController.java
│   │   │           ├── entity/
│   │   │           │   ├── BusinessCustomer.java
│   │   │           │   ├── BusinessCustomerApiKey.java
│   │   │           │   └── BusinessCustomerCredentials.java
│   │   │           ├── exceptions/
│   │   │           │   ├── AccountAlreadyActivated.java
│   │   │           │   ├── AuthenticationException.java
│   │   │           │   ├── CustomerAlreadyExist.java
│   │   │           │   ├── CustomerNotFoundException.java
│   │   │           │   ├── GlobalExceptionHandler.java
│   │   │           │   ├── InvalidCredentialsException.java
│   │   │           │   └── TokenValidationException.java
│   │   │           ├── model/
│   │   │           │   ├── CustomerFields/
│   │   │           │   │   ├── AdminDetails.java
│   │   │           │   │   ├── Agreements.java
│   │   │           │   │   ├── ApiConfiguration.java
│   │   │           │   │   ├── BankingDetails.java
│   │   │           │   │   ├── BankingDocument.java
│   │   │           │   │   ├── BotPurpose.java
│   │   │           │   │   ├── Branding.java
│   │   │           │   │   ├── BusinessDetails.java
│   │   │           │   │   ├── ChatbotConfig.java
│   │   │           │   │   ├── CustomerProfile.java
│   │   │           │   │   ├── LegalAndTaxCompliance.java
│   │   │           │   │   ├── OperatingHours.java
│   │   │           │   │   ├── Operations.java
│   │   │           │   │   ├── Primary.java
│   │   │           │   │   ├── RegisteredAddress.java
│   │   │           │   │   ├── Staff.java
│   │   │           │   │   ├── Technical.java
│   │   │           │   │   └── Theme.java
│   │   │           │   ├── CustomerServiceDto/
│   │   │           │   │   ├── AdminDetailsWithoutPassword.java
│   │   │           │   │   ├── CustomerDto.java
│   │   │           │   │   ├── CustomerServiceApiKeyDto.java
│   │   │           │   │   └── Primary.java
│   │   │           │   ├── request/
│   │   │           │   │   ├── LoginRequest.java
│   │   │           │   │   └── UiBusinessEmailRequest.java
│   │   │           │   └── response/
│   │   │           │       ├── CustomerServiceRequestDto.java
│   │   │           │       ├── CustomerServiceTokenDto.java
│   │   │           │       ├── ErrorMessages.java
│   │   │           │       └── UiApiKeyResponse.java
│   │   │           ├── openfeign/
│   │   │           │   └── CustomerFeignClient.java
│   │   │           ├── repository/
│   │   │           │   ├── BusinessCustomerApiKeyRepository.java
│   │   │           │   ├── BusinessCustomerCredentialsRepository.java
│   │   │           │   ├── BusinessCustomerRepository.java
│   │   │           │   └── TokenRepository.java
│   │   │           └── service/
│   │   │               ├── CustomerAuthenticationService.java
│   │   │               ├── CustomerCredentialsService.java
│   │   │               ├── JwtFilter.java
│   │   │               ├── JwtUtilService.java
│   │   │               └── implementation/
│   │   │                   ├── CustomerAuthenticationServiceImpl.java
│   │   │                   └── CustomerCredentialsServiceImpl.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       └── application-qa.yml
├── test/
│   └── java/
│       └── com/
│           └── zapcom/
│               ├── AuthControllerTest.java
│               └── AuthenticationServiceApplicationTests.java
├── .gitattributes
├── .gitignore
├── build.gradle
├── gradlew
├── gradlew.bat
├── README.md
└── settings.gradle

```

### Project Structure Description

The `AuthenticationService` is a Spring Boot application focused on customer authentication and authorization, including user registration, login, token validation, and customer management.
- **`src/main/java/com/zapcom/`**  
  Contains the Java source code, including the main application class, controllers, services, repositories, configurations, exception handlers, and data models.

- **`advice/`**
  Contains global exception handling classes like AuthServiceExceptionHandler.java to provide consistent error responses across the service.

- **`configuration/`**  
  Configuration classes for security (SecurityConfig.java), Redis caching (RedisConfiguration.java), and user details service implementations (UserDetailsService.java and UserDetails.java).

- **`controller/`**  
  Defines REST controllers such as CustomerAuthenticationController.java responsible for handling authentication-related HTTP endpoints.

- **`constants/`**
  Stores application-wide constants such as MessageConstants.java.

- **`entity/`**
  JPA entity classes representing database tables, including BusinessCustomer.java, BusinessCustomerApiKey.java, and BusinessCustomerCredentials.java.

- **`exception/`**  
  Custom exception classes for error scenarios like AccountAlreadyActivated.java, CustomerNotFoundException.java, InvalidCredentialsException.java, and a GlobalExceptionHandler.java to handle exceptions globally.

- **`openfeign/`**
  Contains Feign client interfaces like CustomerFeignClient.java used for declarative REST client communication

- **`model/request` and `model/response`**  
  This directory contains the core data structures and DTOs used throughout the Authentication Service for representing customer data and request/response payloads.

- **`repository/`**  
  Spring Data JPA repositories for database interactions, such as BusinessCustomerRepository.java, TokenRepository.java, and others.

- **`service/`**
  Service interfaces and implementations for business logic, including authentication, credential management, JWT utilities, and filters.

- **`resources/application.yml`**  
  This directory holds the configuration files for the Authentication Service, enabling environment-specific settings and centralized configuration management.-  **`resources/application-dev.yml`**  
   Configuration overrides and specific settings for the development environment/profile.

- **`resources/application-qa.yml`**  
   Configuration overrides tailored for the QA/testing environment/profile.

- **`scripts/pre-commit`**  
  Git pre-commit hook to ensure code quality checks before committing and pushing code to main.

- **`build.gradle`, `settings.gradle`, `gradlew`, `gradlew.bat`**  
  Gradle build configuration and wrapper scripts for cross-platform build support.

- **`test/java/com.zapcom/`**  
  Contains unit and integration tests for verifying the authentication functionalities used.

## 6. API Documentation

Swagger or OpenAPI documentation is typically provided to document authentication endpoints such as registration, login, token validation, and profile retrieval. 


## 7. API Endpoints

The Authentication Service routes to the following services:
### Authentication Service Routes

- `POST /api/auth/register` – Register a new customer profile. Sends an activation email upon successful registration.
- `POST /auth/login` – Authenticates a user with email and password. Returns a token upon successful activation.
- `GET /auth/logout` – Logs out the current authenticated user.
- `POST /auth/api-key-to-ui` - Validates and retrieves the API key for a given business email.
- `POST /auth/reset-api-key` - Resets and sends a new API key to the specified business email.

### Customer Service Routes
- `GET /api/customer` - List all customers.
- `POST /api/customer` - Receives the customer details for authentication-service and stores them in the database.
- `GET /api/api-key` - Gets api-key from authentication-service and stores it to database.

## 8. Error Handling

### Common Error Responses Used:

- **503 Service Unavailable**  
  This status is returned when the authentication service is temporarily unavailable, which may be caused by server overload, maintenance, or network issues.

- **429 Too Many Requests**  
  Returned when the client exceeds the allowed rate limits, such as multiple login attempts or registration requests in a short period.

- **401 Unauthorized**  
  Indicates authentication failure due to reasons such as:
    - Missing or invalid JWT token
    - Invalid or expired API key  
      Clients must provide valid credentials to access protected endpoints.

- **403 Forbidden**  
  Returned when the client is authenticated but lacks the necessary permissions to perform the requested action, e.g., accessing restricted resources.

- **400 Bad Request**  
  This status indicates the server cannot process the request due to client-side errors such as:
    - Missing required fields in the request body (e.g., email or password not provided)
    - Invalid data types or formats (e.g., improperly formatted email address)
    - Malformed JSON or incorrect request structure
    - Violation of input validation rules (e.g., password too short)
    - Inclusion of unsupported parameters or query strings not handled by the server
## 9. Testing

### Run Tests:
```bash
./gradlew test
```

### Test Categories:
- **Unit Tests:**
  Unit tests focus on testing individual components or services in isolation. For authentication-service, unit tests often target controllers, filters, or configuration classes.
    - Steps to run unit tests:
        - 1. Write Unit Tests (JUnit 5)
        - 2. Run Unit Tests using Gradle:
          ```bash
          ./gradlew test
          ```
        - 3. Verify Test Results: Check the output in your terminal to verify that all unit tests passed. You can also look at the build folder (Gradle) for test reports.
- **Integration Tests:**
  Integration tests, test the interaction between multiple components of the system, including the actual Spring Boot context, databases, and external services.

Integration tests for authentication-service cover routing, security filters, file uploads, authentication flows, and integration with customer-service.
    - Steps to run integration tests:
        - 1. Write Integration Tests (JUnit 5 + Spring Boot Test)
             Integration tests can use @SpringBootTest to load the full Spring context.
        - 2. Run Integration Tests using Gradle (Gradle 8.4 is to be used)

## 10. Deployment Instructions

### Local Deployment
1. Ensure Redis is running locally
2. Run with development profile:
   ```
   ./gradlew bootRun --args='--spring.profiles.active=dev'
   ```
### QA Environment
1. Build the application:
   ```
   ./gradlew build
   ```
2. Deploy the JAR file to the QA server
3. Run with QA profile

### Production Environment
1. Build the application:
   ```
   ./gradlew build
   ```
2. Deploy the JAR file to production servers
3. Run with production profile:
   ```
   cd build
   ```
   ```
   cd libs
   ```
   ```
   java -jar build/libs/OptimusApi-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
   ```

### Docker Setup:

1. Create a `Dockerfile` within the root directory:
    ```dockerfile
    FROM openjdk:21
    COPY build/libs/OptimusApi-0.0.1-SNAPSHOT.jar
    ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=prod"]
    ```
2. Build the project 'JAR' file:
    ```bash
    ./gradlew build
    ```
   After running this the JAR file appears in build/libs

3. Build the Docker image:
    ```
    docker build -t optimus-authentication-service
    ```

4. Run the container formed:

    ```
    docker run -p 8080:8081 optimus-authentication-service
    ```
   within the above running of the docker container 8080 port number comes into picture.


