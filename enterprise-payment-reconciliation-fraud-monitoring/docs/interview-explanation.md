# Interview Explanation

## Project Overview
I built an Enterprise Payment Reconciliation and Fraud Monitoring System as a Java full stack microservices project. The platform handles payment transaction creation, event-driven fraud checks, bank settlement file reconciliation, notifications, and dashboard reporting.

## My Role and Responsibilities
My role covered backend microservice design, secure APIs, Kafka event integration, database modeling, React dashboard development, Docker setup, and CI/CD pipeline structure. I designed the modules using controller, service, repository, DTO, and entity layers so each responsibility stays clear.

## Architecture Explanation
The frontend talks to the API Gateway. The gateway validates JWT tokens and routes requests to registered services through Eureka. Business services own their data stores. Transaction, fraud, reconciliation, and notification workflows communicate asynchronously through Kafka.

## Why Microservices Were Used
Payments, fraud, reconciliation, reporting, and notifications have different scaling and ownership needs. Fraud detection can scale independently based on Kafka traffic, while reconciliation can process heavy file uploads without affecting login or transaction APIs.

## Where Kafka Was Used
Kafka is used for transaction-created, transaction-status-updated, fraud-alert, and reconciliation-completed events. This decouples services and improves resilience. For example, transaction-service does not directly call fraud-service; it publishes an event, and fraud-service consumes it.

## How JWT Security Works
Auth-service validates credentials with BCrypt and creates a signed JWT containing user identity and roles. The gateway checks the token before forwarding protected requests. Role checks are defined by API category: finance users work with transactions and reconciliation, auditors work with fraud and reports, and admins can access management APIs.

## How Reconciliation Works
Finance users upload CSV or Excel settlement files. The reconciliation service parses the file using Java I/O or Apache POI, creates reconciliation records, identifies matched, mismatched, missing, and duplicate records, stores a summary, and publishes a completion event.

## How Fraud Detection Works
Fraud-service consumes transaction-created events and applies rule-based checks such as high amount, failed transactions, and risky transaction patterns. Alerts are stored in MongoDB because alert data can evolve and is naturally document-oriented.

## Database Design Explanation
Auth, transaction, reconciliation, and notification services use PostgreSQL for relational integrity. Fraud alerts use MongoDB. Audit fields are part of shared base entities where JPA is used.

## Challenges Faced
The main challenge is keeping services loosely coupled while still making the dashboard useful. Kafka solves the real-time workflow problem, and common event DTOs keep contracts consistent.

## Performance Optimization
Pagination is used for transaction listings. Kafka allows async processing. Database indexes are added on common transaction lookup fields. Redis is included in the deployment for caching dashboard summaries or token/session extensions.

## Deployment Explanation
Each backend service has a Dockerfile. Docker Compose runs PostgreSQL, MongoDB, Redis, Kafka, Eureka, gateway, backend services, and React frontend. The Jenkinsfile includes build, test, SonarQube placeholder, Docker image build, push placeholder, and deployment placeholder for AWS ECS/EKS.

## Possible Interview Questions and Answers

**Q: Why did you use Kafka instead of REST between transaction and fraud services?**  
A: Fraud checks should not block transaction creation. Kafka gives async processing, replay capability, and better scaling under payment spikes.

**Q: Why MongoDB for fraud alerts?**  
A: Fraud alert payloads can change as rules evolve. MongoDB handles flexible alert documents well.

**Q: How do you secure APIs?**  
A: Auth-service issues JWTs, the gateway validates tokens, and services expose APIs according to role-based access rules.

**Q: How would you improve this for production?**  
A: Add distributed tracing, schema registry, dead-letter topics, Testcontainers, centralized logs, AWS Secrets Manager, and autoscaling.
