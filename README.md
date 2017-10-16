# URLShorteningService
This repository shortens a normal URL and also redirects to the normal URL given the short URL

## Getting Started
To download the source code:
    git clone https://github.com/cgswtsu78/URLShorteningService.git

To build the application:
    mvn clean install

This command will compile, execute the unit tests and create a WAR file.


To run the application using embedded Jetty:
 
    cd ~/URLShorteningService
    mvn jetty:run
    
This application uses an in-memory H2 databse for simplicity and to reduce external depedencies for running the solution.
    
To access the application: 
  http://localhost:8080 (if your default Jetty port is something else you will need to use that)
  
  ## Assumptions
  You must enter a fully qualified URL (i.e. http://apple.com) on index.html or you will get a friendly error
  
  Based on the example provided (i.e. http://localhost:9000/1), I assumed that it was fine to use a auto generated DB ID in the URL         instead of converting the DB ID to a base 62 alphanumeric. 
  
  Open Source frameworks (i.e. spring) were off limits
  
  Since I was the sole developer, I worked directly off master and did not create a feature branch. 
  
  
  
