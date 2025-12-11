# Payment_Application

### Project Summary

Payment Application allows clients to create payments and register webhooks to receive asynchronous notifications after payment creation. The system includes retry logic for failed webhook calls and securely stores card numbers in encrypted format. The application can be run locally or via Docker.

### Features

	- Create payments and register webhooks for clients
	- Asynchronous webhook processing
	- Retry logic for failed webhook calls
	- Card number encryption
	- Local run and Docker support
	- Swagger/OpenAPI documentation included

### Tech Stack

	- Java 17, Spring Boot
	- PostgreSQL, pgAdmin
	- Docker, Docker Compose
	- Swagger UI, Postman
	- Gradle

### RUNNING THE PROJECT USING DOCKER (RECOMMENDED)

- Prerequisites:
	 - Docker: https://www.docker.com/get-started

## Steps to Set Up and Run the Application:

1. Clone the Repository
	- Clone the repository to your local machine or Download it as zip.

2. Build Docker Containers
	- Build the necessary Docker containers using Docker Compose:
		- docker-compose build

	- This command will pull the required images and build the payment-app container.

3. Start the Application
	- Start the Docker containers for PostgreSQL, pgAdmin, and the Payment Application:
		- docker-compose up -d

	- This will run the following services:
      	- PostgreSQL: Running on port 5432
      	- pgAdmin: Running on localhost:5050
      	- Payment App: Running on localhost:8080

4. Access pgAdmin (Database GUI)
	- Open pgAdmin by navigating to http://localhost:5050 in your web browser.

	- Log in with:
		- Email: admin@admin.com
		- Password: admin

	- Register PostgreSQL Server:
     - Right-click on **Servers → Register → Server...**
     - A pop-up will open. Enter the following details:
       - Name: **Payment Database** (any name)
       - Host: **postgres**
       - Username: **postgres**
       - Password: **postgres**
     - Click **Save**


	- Access the Database:
		- Expand paymentdb → Schemas → public → Tables
		- You should see the tables created by the payment-app

5. Check Logs for Payment App
	- View logs:
		- docker-compose logs payment-app

	- View logs continuously:
		- docker-compose logs -f payment-app
	
	- Press Ctrl + C to stop following logs

6. Access the Application
	- Payment App: http://localhost:8080
	- Payment Swagger: http://localhost:8080/swagger-ui/index.html#/
	- pgAdmin: http://localhost:5050

		- Login again with:
			- Email: admin@admin.com
			- Password: admin

7. Stop the Application
	- To stop the application and shut down containers:
		- docker-compose down

	- Data persists in the following volumes:
		- postgres_data
		- pgadmin_data

	- Optional: Remove All Containers and Volumes
	- To remove containers and delete PostgreSQL data:
		 - docker-compose down -v

### RUNNING THE PROJECT LOCALLY (WITHOUT DOCKER)

1. Install Prerequisites

   - Java 17
		 - Download and install from your preferred JDK provider:
		 - https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
		 - Or for Windows:
		 - https://download.oracle.com/java/17/archive/jdk-17.0.11_windows-x64_bin.exe

   - PostgreSQL with pgAdmin
		 - Download and install from:
		 - https://www.postgresql.org/download/
		 - Or for Windows:
		 - https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
		 - During setup, create a user 'postgres' with password 'postgres'
		 - (or use your custom credentials and update application.yaml accordingly)
			- Make sure to check pgAdmin during installation

2. Create the Database

   - Open pgAdmin or psql command line and run:

         - CREATE DATABASE paymentdb;

3. Configure the Project

   - Open src/main/resources/application.yaml and update database settings if needed:

	   - spring.datasource.url=jdbc:postgresql://localhost:5432/paymentdb
	   - spring.datasource.username=postgres
	   - spring.datasource.password=postgres
	   - spring.jpa.hibernate.ddl-auto=update
	   - spring.profiles.active=dev

4. Run the Project

   - Open a terminal in the project root folder (where build.gradle is located)
   - Run the project using Gradle wrapper:

	   - Windows: gradlew.bat bootRun
	   - Linux/Mac: ./gradlew bootRun

   - Wait for Spring Boot to start
   - The application will be running at http://localhost:8080
   - Access Swagger UI at http://localhost:8080/swagger-ui/index.html#/

5. Verify Database Connection

   - Register PostgreSQL Server in pgAdmin
     - Right-click on **Servers → Register → Server...**
     - A pop-up will open. Enter the following details:
       - Name: **Payment Database** (any name)
       - Host: **localhost**
       - Username: **postgres**
       - Password: **postgres**
     - Click **Save**
  
     
   - Check terminal logs for messages like:
	   - Hibernate: create table ...

   - If tables are not created automatically, run the following SQL commands:

		   - CREATE TABLE IF NOT EXISTS payment (
			   id SERIAL PRIMARY KEY,
			   first_name VARCHAR(100) NOT NULL,
			   last_name VARCHAR(100) NOT NULL,
			   zip VARCHAR(20),
			   encrypted_card TEXT NOT NULL,
			   client_id VARCHAR(100) NOT NULL,
			   "timestamp" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
			   amount NUMERIC
		   );

		   - CREATE TABLE IF NOT EXISTS webhook (
			   id SERIAL PRIMARY KEY,
			   client_id VARCHAR(255) NOT NULL,
			   url VARCHAR(500) NOT NULL,
			   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
		   );

		   - CREATE TABLE IF NOT EXISTS retry_webhook (
			   id SERIAL PRIMARY KEY,
			   webhook_id INTEGER NOT NULL,
			   payment_id INTEGER NOT NULL,
			   retry_count INTEGER DEFAULT 0,
			   last_attempt TIMESTAMP DEFAULT NOW(),
			   status VARCHAR NOT NULL
		   );

6. Architectural Diagram

   - An architectural diagram illustrating the project flow and feature refinement
   is provided as a Draw.io file (attached). 
   - You can open it with Draw.io or the online editor at https://app.diagrams.net/
   to understand the system design and component interactions.

7. Testing Artifacts

   - Sample requests, instructions and Testing scenarios are provided in the 'testing-artifacts' folder.
   - Use the JSON files with Postman or Swagger UI to test the application endpoints.
   
8. Stop the Application

   - Press Ctrl + C in the terminal to stop Spring Boot

### How I Worked

1. Created a simple architectural diagram in Draw.io for adding webhook URLs and creating payments
2. Designed API request/response and database structure
3. Created Spring project from initializer and added it to GitHub
4. Created basic project structure and configured DB connections
5. Added openapi.yaml file and respective configurations in build.gradle
6. Implemented createPayment API and registerWebhook API
7. Implemented POST calls to external URLs
8. Implemented card number encryption logic
9. Implemented retry logic for external URLs
10. Added Mock WebController API to simulate successful responses
11. Added Dockerfile and docker-compose.yaml

### Testing: Performed after completing each feature.


### If there is any issue while setting up the application, please feel free to contact me 

	- Phone +61 435365686
	- mail priyakrish816@gmail.com
