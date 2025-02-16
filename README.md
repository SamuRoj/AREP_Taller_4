# Web Framework Development with Reflection, IoC Pattern and Meta Protocol Objects

This project implements a framework designed to serve HTML pages and PNG images. It also offers an Inversion of Control 
(IoC) framework for building web applications from POJOs (Plain Old Java Objects). Additionally, it provides developer 
tools to define REST services with custom annotations, manage query parameters within requests, specify the location of 
static files for the server and includes a small web application for planning daily activities.

## Getting Started

### Prerequisites

### 1. **Java Development Kit (JDK)**

- To check if Java is installed, run:

```
java -version
```

- If Java is not installed, download it from the official Oracle [website](https://www.oracle.com/co/java/technologies/downloads/).

### 2. **Maven**

- To check if Maven is installed, run:

```
mvn --version
```

- If Maven is not installed, download it from the official Apache Maven [website](https://maven.apache.org/download.cgi).

### 3. Git

- To check if Git is installed, run:

```
git --version
```

- If Git is not installed, download it from the official Git [website](https://git-scm.com/downloads).

### Installing

1. Clone the repository to your local machine using Git.

```
git clone https://github.com/SamuRoj/AREP_Taller_3.git
```

2. Navigate to the project directory.

```
cd AREP_Taller_3
```

3. Build the project by running the following command:

```
mvn clean install
```

4. Execute the project with the following command:

```
java -cp target/taller-3-1.0-SNAPSHOT.jar edu.escuelaing.arep.WebApplication
```

5. Once the server is running, open your web browser and visit:

```
http://localhost:23727/
```

## Features of the application

- **Annotations:**

  - **GetMapping, PostMapping, DeleteMapping:** Loads a method into the services that will be offered in and 
  clarifies the HTTP method used in the request.
    - value: Defines the route of the endpoint. 

  - **RequestParam:** It's an annotation made for the parameters and contains two values:
    - value: The name of the parameter to be retrieved from the request.
    - defaultValue: The value assigned to the parameter in the method if it is not present in the request. 

  - **RestController:** This annotation enables reading files from disk and automatically loads all methods annotated 
  with any HTTP annotations, making them available as services.

- **Static files endpoint:** Allows developers to define the folder where the files would be queried, this
method can be requested through the following URL with the parameter folder `http://localhost:23727/app/folder?folder=<folder-name>`.

  - By default, this method has the route `static` defined and every request made will be searched within this
    folder

    ![Staticfiles.png](src/main/resources/img/Staticfiles.png)

  - The folder can be changed to newFolder, which contains a basic HTML file. To verify the change, a request 
  has to be made to the following URL: `http://localhost:23727/app/folder?folder=newFolder`.

    ![RouteChange.png](src/main/resources/img/RouteChange.png)
  
    ![NewWebpage.png](src/main/resources/img/NewWebpage.png)

  - **Note:** The REST services defined within the controllers will continue to be available.

- Query for static files located in the defined folder, to do this just add to the route of the page the name
  of the file like `http://localhost:23727/<filename>`, it can be made with any file that's located in the
  current folder.

  ![QueryFile.png](src/main/resources/img/QueryFile.png)

- The main page contains a small app that allows the user to add his daily activities through requests methods
  like GET, POST and DELETE, it's made with HTML, CSS and JavaScript to make asynchronous petitions to the server
  and add new activities to it, all the services mentioned before are found in the ApiController.java file through a
  REST service.

  ![Webpage.png](src/main/resources/img/Webpage.png)

## Architecture

### Deployment Diagram

  ![DeploymentDiagram.png](src/main/resources/img/DeploymentDiagram.png)

### Overview

This diagram summarizes the interactions between the client and the HttpServer implementation.

### Components

- **HTML:** It is served through the HTTP server and allows the browser to render the web page.
- **CSS:** It is also served from the server and is responsible for styling the page.
- **JS:** It is served from the server and handles GET, POST, and DELETE requests to refresh the activity list
  on the page, while also keeping the server updated on any changes made.
- **HTTP Server:** It is responsible for serving the files requested by the client, processes the GET, POST,
  and DELETE requests it receives, changes the directory of the file and saves the REST services that has been
  added by the developer.
- **Web Application:** Works as a middleware to connect the HTTP Server and the client, it also defines some
  REST services, loads the components (Controllers) and the route where the files will be searched.

## Running the tests

- Execute them by running the following command:

```
mvn test
```

### HttpRequestTest

The tests in this file check the functionality of the method getValues() implemented in the class.

- Example of test:

  ![HttpRequestTest.png](src/main/resources/img/HttpRequestTest.png)

- Image of the results:

  ![HttpRequestTestResults.png](src/main/resources/img/HttpRequestTestResults.png)

### HttpResponseTest

The tests in this file check the functionality of the method parseValues() implemented in the class.

- Example of test:

  ![HttpResponseTest.png](src/main/resources/img/HttpResponseTest.png)

- Image of the results:

  ![HttpResponseTestResults.png](src/main/resources/img/HttpResponseTestResults.png)

### HttpServerTest

The tests in this file just verifies if the mime type of the file being queried it's the proper one and returns
a valid or invalid answer in case it doesn't exist.

- Example of test:

  ![MimeType.png](src/main/resources/img/MimeType.png)

- Image of the results:

  ![HttpServerTestResults.png](src/main/resources/img/HttpServerTestResults.png)

### WebApplicationTest

The tests in this file runs the application as a thread to check its connectivity and answers to different
requests for a file like index.html, script.js, styles.css and wallpaper.jpeg. It also verifies the functionality of
the REST services defined within the controllers with custom annotations by querying endpoints like `/app/hello`, 
`/app/pi`, `/app/e`, `/app/greeting?name=Samuel`, etc.

- Example of request for a file:

    - Starts and finish the server before and after the execution of all tests.

      ![BeforeAfter.png](src/main/resources/img/BeforeAfter.png)

- Finds the file locally and makes a query to the server to compare the responses.

  ![WATestStructure.png](src/main/resources/img/WATestStructure.png)

- Example of request for a REST service defined within the controllers:

    - It makes a request to an endpoint and asserts the answer it's the same as the one already defined.

      ![RSTestStructure.png](src/main/resources/img/RSTestStructure.png)

- Image of the results:

  ![WebApplicationTestResults.png](src/main/resources/img/WebApplicationTestResults.png)

## Built With

* [Java Development Kit](https://www.oracle.com/co/java/technologies/downloads/) - Software Toolkit
* [Maven](https://maven.apache.org/) - Dependency Management
* [Git](https://git-scm.com/) - Distributed Version Control System

## Authors

* **Samuel Rojas** - [SamuRoj](https://github.com/SamuRoj)

## License

This project is licensed under the GNU License - see the [LICENSE.txt](LICENSE.txt) file for details.