![Book_Store](https://github.com/user-attachments/assets/1df8d4d0-dea8-4930-b716-1fc385aa1383)




Welcome to my Book Store App, a Java-based web application for managing book catalogs and online purchases. Designed with Spring Boot, it follows a service-oriented architecture and SOLID principles for scalability and maintainability.

The app features secure authentication, RESTful APIs, full CRUD operations, search, pagination, sorting, and soft delete. It integrates Swagger for API documentation and Liquibase for database versioning, ensuring a robust and efficient system with strong exception handling and data validation.

# ⚙️ Core Functionalities
### 📖 Book Management

Retrieve all books or search by title, author, or category  
View book details by ID  
Add, update, or delete books (Admins only)  

### 👤 User & Authentication

User registration and login with JWT authentication  
Secure role-based access (User/Admin)  

### 🛍️ Shopping Cart & Orders

Add, update, or remove books from the cart  
Place orders and track order history  
Admins can manage order statuses  

### 📂 Category Management

List all categories and their books  
Add, update, or delete categories (Admins only)  

### 🔧 Additional Features

Pagination & sorting for efficient data retrieval  
Global exception handling for better error management  
Swagger documentation for API exploration  

# 📁 Project Structure
The application follows a 3-layer architecture:

Controller Layer – Manages HTTP requests and responses.  
Service Layer – Contains business logic and interacts with repositories.  
Repository Layer – Handles database operations using JPA.  

### Main Entities:  
User – Stores user details and authentication info.  
Role – Defines user roles (e.g., USER, ADMIN).  
Book – Represents books in the store.  
Category – Groups books into categories.  
ShoppingCart – Manages items before purchase.  
CartItem – Represents individual items in the shopping cart.  
Order – Tracks completed purchases.  
OrderItem – Represents books in an order.  


![bookstorediagram](https://github.com/user-attachments/assets/cb89be48-9ea5-42d1-b6da-dcf567ebe640)
 
# 🛠️Technologies Used
### Core Frameworks & Libraries
- Spring Boot
- Spring Data JPA
- Spring Security
- Spring MVC

### Database & Persistence
- MySQL
- Liquibase

### Security & Authentication
- JWT (JSON Web Token)

### Data Transformation & Utilities
- MapStruct
- Lombok

### API Documentation & Testing
- SpringDoc OpenAPI & Swagger UI
- JUnit & Testcontainers

### Build & Deployment
- Maven
- Docker (Optional)


# 🚀 How to Set Up and Run the Project

### Requirements
Ensure you have the following installed on your system:

- Java 17 (JDK)
- IntelliJ IDEA or another Java IDE
- MySQL (or Docker if using containerized MySQL)
- Maven (for dependency management)
- Docker (optional, for easier database setup)

### Clone the Repository
git clone https://github.com/your-username/bookstore-app.git  
cd bookstore-app
### Configure the Database
1. Open MySQL Workbench or your preferred SQL tool.  
2. Create a database:  
    CREATE DATABASE bookstore;  
3. Update application.properties (located in src/main/resources/) with your MySQL credentials:  
    spring.datasource.url=jdbc:mysql://localhost:3306/bookstore  
    spring.datasource.username=YOUR_USERNAME  
    spring.datasource.password=YOUR_PASSWORD  
### Build and Run the Project
Run with Maven  
    mvn clean install  
    mvn spring-boot:run  
    
  Access the application at http://localhost:8080  

### Run with Docker (Optional)

docker-compose up
If running with Docker, the app will be accessible at http://localhost:8088
