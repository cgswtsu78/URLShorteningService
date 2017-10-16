# URLShorteningService
This repository contains a maven based Java Web Application which shortens a normal URL and also redirects to the normal URL given a short URL

## Getting Started
To download the source code:
    git clone https://github.com/cgswtsu78/URLShorteningService.git

To build the application:
    mvn clean install

This command will compile, execute the unit tests and create a WAR file.


To run the application using embedded Jetty:
    cd ~/URLShorteningService
    mvn jetty:run

Java Version:
    1.8.0_121-b13
 
Database:
This application uses an in-memory H2 databse for simplicity and to reduce external depedencies for running the solution.
    
Browser Access: 
  http://localhost:8080 will load the index.html content automatically
  
  if your default Jetty port is something else you will need to use that
  
  ## Application Structure
  com.listeners.AppContextListener.java:
  
  Initializes H2 in-memory DB with each reload of the Jetty container
  
  com.service.*:
  
  Interfaces for the CompressURL and DecompressURL services
  
  com.service.impl*:
  
  service implemenation for the business and persistence logic
  
  These services could be further decoupled by moving the persistence logic into its own layer (DAO)
  
  com.servlets.Compress.java:
  Java Servlet which accepts the long URL input from the index.html page and utilizes the CompressURL service to do some validation and     persist the entry. 
  
  com.servlet.Decompress.java:
  Java Servlet which parses the ID from the context path and then uses DecompressURL.java to do a lookup and subsequent redirect if         possible.
  
  src/main/resources/log4j.properties:
  Basic logging
  
  src/main/test/java
  Single JUnit test suite
  
  pom.xml:
  maven parent pom which outlines all depedencies
  
  ## Assumptions
  You must enter a fully qualified URL (i.e. http://www.apple.com) on index.html or you will get a friendly error
  
  Based on the example provided (i.e. http://localhost:9000/1), I assumed that it was fine to use a auto generated DB ID in the URL         instead of converting the DB ID to a base 62 alphanumeric. 
  
  Open Source frameworks (i.e. spring) were off limits as I tried to stay as native to Java as I could.
  
  Since I was the sole developer, I worked directly off master and did not create a feature branch. 
  
  I avoided hashing to ensure uniqueness and to avoid any sort of query bottlenecks as I would need to query the database by URL instead     of DB ID.
  
  The use of an in-memory H2 DB was primarily for convience, ease of testing the solution and ease of verifying the solution. If this were   a production application, I would using Cassandra or Couchbase (if active-active, multi-region is a requirment) or DynamoDB if AWS is     an option and active-active, multi-region is a not a requirment
  
  I could increase the listener and servlet package unit test coverage by using mockito to mock the ServletContext 
