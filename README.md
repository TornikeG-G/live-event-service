# Live Events Service

## Overview

Live Events Service is a Java-based Spring Boot microservice that tracks live sports events.  
For each event marked as **LIVE**, the service periodically (every 10 seconds):

- Calls an external REST API
- Transforms the response into a message
- Publishes the message to a Kafka topic

This project is implemented as a **working prototype**, focusing on correctness, clarity, and observability.

---

## Tech Stack & Versions

- **Java:** 21
- **Spring Boot:** 4.0.2
- **Build Tool:** Maven
- **Messaging:** Apache Kafka
- **HTTP Client:** RestTemplate / WebClient
- **API Documentation:** Swagger (springdoc-openapi)
- **Logging:** SLF4J + Logback

---

## Retry Logic

The Kafka message publisher includes manual retry logic. When a message fails to send, the publisher will:

- Retry sending the message up to a fixed number of attempts.
- Wait for a short backoff period between attempts.
- Log each retry and the final failure if all retries fail.

This approach keeps the retry behavior **explicit, simple, and easy to understand**.  

An alternative could be to use **Spring Retry with AOP**, which provides declarative annotations like `@Retryable` and `@Recover`. This approach was not used here to avoid extra dependency complexity and potential versioning issues.

---

## Kafka Setup

The service requires Kafka to be running on **port 9092** before starting the application.  

Two options are available:

1. **Docker Compose**  
   Kafka can be started as a container using Docker Compose. This method is fast and isolated, requiring no manual installation. Docker handles all broker and controller configuration automatically.

2. **Manual Installation**  
   Kafka can also be downloaded and run manually on the host machine. This provides full control over Kafka configuration but requires manual setup and ensures the broker is running on the expected port.

The application always connects to Kafka at `localhost:9092`.

---

## External API Integration

In this project, the external API is **mocked** to provide sample live event data. The scheduler calls this API periodically to simulate fetching live event updates.  

Optionally, the service can integrate with **OpenAI** to generate event data dynamically. This requires:

- An OpenAI API token
- Secure storage of the token (e.g., environment variables)

The OpenAI integration is **optional** and not enabled by default to keep the project self-contained and easy to run.

---

## Scheduler

The scheduler is implemented using Spring’s `TaskScheduler`. For each live event:

- A scheduled task is created that executes every 10 seconds.
- The task calls the external API to fetch event data.
- The result is published to Kafka.

This thread-based dynamic scheduling allows each event to have its own independent schedule. Cron jobs were considered, but thread scheduling provides more flexibility for per-event execution.

## How to Run the Application

Follow these steps to start and use the Live Events Service:

### 1. Build the Project
Compile and package the application with Maven:

mvn clean install

### 2. Start Kafka

Kafka must be running on port 9092 before starting the application. You have two options:

1. **Docker Compose: Start Kafka as a container using Docker Compose. This method is quick and isolated. 

docker-compose up -d

2. **Manual Installation: Download Kafka from the official site and run it locally. Ensure the broker is running on port 9092.

### 3. Start the Spring Boot Application

mvn spring-boot:run


### 4. Access the API Documentation

http://localhost:8080/swagger-ui.html



