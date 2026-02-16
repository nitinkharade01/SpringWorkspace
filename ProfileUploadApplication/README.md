# Profile Upload App

Spring Boot REST API for profile picture upload.

## Setup
1. Install MySQL, create `profiledb` database
2. Update `application.properties` credentials
3. `mvn spring-boot:run`
4. Test with Postman

## Test Upload
POST `http://localhost:8080/api/users/1/profile-image`
- form-data: `file` = image.jpg (JPG/PNG <5MB)

## Test Fetch
GET `http://localhost:8080/api/users/1/profile-image`
Image: `http://localhost:8080/uploads/profile/uuid.jpg`
