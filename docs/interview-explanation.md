# Interview Explanation

This project is an enterprise payment operations platform built with Java 17, Spring Boot 3, Spring Cloud microservices, Kafka, MySQL, MongoDB, and React.

## Core Workflow

1. A user registers or logs in through `auth-service`.
2. `auth-service` validates credentials, encodes passwords with BCrypt, and issues a JWT.
3. The React frontend stores the JWT and sends it to the `api-gateway`.
4. `api-gateway` validates the token, applies role-based route rules, and forwards requests through Eureka service discovery.
5. `transaction-service` creates and manages payment transactions in MySQL.
6. Transaction events are published to Kafka.
7. `fraud-detection-service` consumes transaction events, applies rule-based checks, stores alerts in MongoDB, and publishes fraud alert events.
8. `reconciliation-service` uploads settlement CSV files, compares settlement rows with internal records, stores summaries, and publishes reconciliation events.
9. `notification-service` consumes important events and stores mock notification logs.
10. `reporting-service` exposes dashboard and chart APIs for finance and audit users.

## Why Microservices

The system is split by business capability: authentication, transactions, fraud detection, reconciliation, notifications, reporting, and gateway routing. This keeps each service focused and lets teams scale or change high-volume areas, such as transaction processing and fraud checks, without redeploying the whole platform.

## Why Kafka

Kafka decouples the write path from downstream processing. Creating a transaction does not need to wait for fraud checks or notifications to complete. Services can consume events independently, which improves resilience and keeps the payment workflow responsive.

## Database Choices

MySQL is used for structured transactional domains such as users, payments, reconciliation records, and notifications. MongoDB is used for fraud alerts because alert payloads and rule evidence can evolve over time and may be more flexible than core payment records.

## Security

Security is centralized at the gateway. Public routes are limited to login and registration. Protected routes require a valid JWT, and route groups enforce role access for `ADMIN`, `FINANCE_USER`, and `AUDITOR`. Downstream services receive authenticated user headers from the gateway.

## Testing Story

The codebase includes unit and slice tests for controllers, services, JWT handling, transaction mapping, fraud rules, reconciliation processing, notifications, and reporting. Test profiles use lightweight configuration so the automated suite can run without local MySQL, MongoDB, Kafka, or Eureka.

## Production Enhancements

Strong next steps would be Testcontainers integration tests, Kafka retry and dead-letter topics, refresh tokens, centralized tracing, structured logging, real notification providers, and infrastructure as code for cloud deployment.
