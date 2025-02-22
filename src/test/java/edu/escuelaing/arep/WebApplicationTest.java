package edu.escuelaing.arep;

import edu.escuelaing.arep.http.HttpServer;
import edu.escuelaing.arep.threads.ClientThread;
import org.junit.jupiter.api.*;

import java.io.*;
import java.net.URL;

import static edu.escuelaing.arep.http.HttpServer.deleteActivities;
import static org.junit.jupiter.api.Assertions.*;

public class WebApplicationTest {

    private static Thread webApp;

    @BeforeAll
    public static void setup() throws InterruptedException {
        // Starting the web server as a Thread
        webApp = new Thread(() -> {
            try {
                String[] args = {};
                WebApplication.main(args);
            } catch (Exception e) {
                fail();
            }
        });
        webApp.start();
        Thread.sleep(1000);
    }

    @AfterAll
    public static void endServer() throws InterruptedException, IOException {
        if (webApp != null && webApp.isAlive()) {
            HttpServer.stop();
            URL localhost = new URL("http://localhost:23727/");
            localhost.openStream();
            webApp.interrupt();
            webApp.join();
        }
    }

    public String getFile(String path){
        try {
            String inputLine;
            StringBuilder fileContent = new StringBuilder();
            BufferedReader in = new BufferedReader(new FileReader(path));
            while((inputLine = in.readLine()) != null){
                fileContent.append(inputLine);
                if (!in.ready()) break;
            }
            in.close();
            return fileContent.toString();
        } catch (IOException e) {
            fail();
        }
        return "";
    }

    public String getResponse(String route){
        try {
            String inputLine;
            URL localhost = new URL(route);
            BufferedReader reader = new BufferedReader(new InputStreamReader(localhost.openStream()));
            StringBuilder response = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
                if (!reader.ready()) break;
            }
            reader.close();
            return response.toString();
        } catch (IOException e) {
            fail();
        }
        return "";
    }

    public static void executeThreads(ClientThread[] clientThreads, String action){
        for(int i = 0; i < 10; i++) {
            clientThreads[i] = new ClientThread(action, i);
            clientThreads[i].start();
            try {
                clientThreads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void shouldGetIndexHtml() {
        // Reading the file
        String path = "target/classes/static/index.html";
        String fileContent = getFile(path);
        // Reading the response from the URL
        String route = "http://localhost:23727/";
        String response = getResponse(route);
        // Assertion
        assertEquals(response, fileContent);
    }

    @Test
    public void shouldGetCss() {
        String path = "target/classes/static/styles.css";
        String fileContent = getFile(path);
        String route = "http://localhost:23727/styles.css";
        String response = getResponse(route);
        assertEquals(response, fileContent);
    }

    @Test
    public void shouldGetJs() {
        String path = "target/classes/static/script.js";
        String fileContent = getFile(path);
        String route = "http://localhost:23727/script.js";
        String response = getResponse(route);
        assertEquals(response, fileContent);
    }

    @Test
    public void shouldGreetWithStaticGet() {
        String route = "http://localhost:23727/app/hello";
        String response = getResponse(route);
        assertEquals("{\"response\":\"Hello World!\"}", response);
    }

    @Test
    public void shouldGreetWithStaticGetAndQuery() {
        String route = "http://localhost:23727/app/greeting?name=Samuel";
        String response = getResponse(route);
        assertEquals("{\"response\":\"Hello Samuel\"}", response);
    }

    @Test
    public void shouldGetValueOfPi() {
        String route = "http://localhost:23727/app/pi";
        String response = getResponse(route);
        assertEquals("{\"response\":\"" + Math.PI + "\"}", response);
    }

    @Test
    public void shouldGetValueOfE() {
        String route = "http://localhost:23727/app/e";
        String response = getResponse(route);
        assertEquals("{\"response\":\"" + Math.E + "\"}", response);
    }

    @Test
    public void shouldChangeDirectory() {
        try {
            // Reading the file
            String path = "target/classes/newFolder/index.html";
            String fileContent = getFile(path);

            // Changing the folder
            URL localhost = new URL("http://localhost:23727/app/folder?folder=newFolder");
            localhost.openStream();

            // Reading the response from the URL
            String route = "http://localhost:23727/";
            String response = getResponse(route);

            // Assertion
            assertEquals(response, fileContent);

            // Changing to default folder
            localhost = new URL("http://localhost:23727/app/folder?folder=static");
            localhost.openStream();
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void shouldAddAndGetActivities() {
        try {
            // Deleting previous activities
            deleteActivities();

            // Adding the activity
            URL localhost = new URL("http://localhost:23727/app/post/activities?time=12:00%20AM&activity=Sleep");
            localhost.openStream();

            // Reading the response from the URL
            String route = "http://localhost:23727/app/get/activities";
            String response = getResponse(route);

            // Assertion
            assertEquals("[{\"time\": \"12:00 AM\", \"activity\": \"Sleep\"}]", response);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void shouldDeleteActivity() {
        try {
            // Deleting previous activities
            deleteActivities();

            // Adding the activities
            URL localhost = new URL("http://localhost:23727/app/post/activities?time=12:00%20AM&activity=Sleep");
            localhost.openStream();
            localhost = new URL("http://localhost:23727/app/post/activities?time=11:00%20AM&activity=Shower");
            localhost.openStream();

            // Reading the response from the URL
            String route = "http://localhost:23727/app/get/activities";
            String response = getResponse(route);

            assertEquals("[{\"time\": \"12:00 AM\", \"activity\": \"Sleep\"}," +
                                    "{\"time\": \"11:00 AM\", \"activity\": \"Shower\"}]", response);

            // Deleting activity
            localhost = new URL("http://localhost:23727/app/delete/activities?time=12:00%20AM");
            localhost.openStream();

            // Reading the response from the URL
            route = "http://localhost:23727/app/get/activities";
            response = getResponse(route);

            // Assertion
            assertEquals("[{\"time\": \"11:00 AM\", \"activity\": \"Shower\"}]", response);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void shouldGetActivitiesConcurrently(){
        deleteActivities();

        // Execution threads with the given action
        ClientThread[] threads = new ClientThread[10];
        executeThreads(threads, "get");

        // Reading the response from the URL
        String route = "http://localhost:23727/app/get/activities";
        String response = getResponse(route);

        // Assertion
        assertEquals("[]", response);
    }

    @Test
    public void shouldAddActivitiesConcurrently(){
        deleteActivities();

        ClientThread[] threads = new ClientThread[10];
        executeThreads(threads, "post");
        String route = "http://localhost:23727/app/get/activities";
        String response = getResponse(route);

        // Assertion
        assertEquals(10, response.split(",\\{").length);
    }

    @Test
    public void shouldDeleteActivitiesConcurrently(){
        deleteActivities();

        ClientThread[] threads = new ClientThread[10];
        executeThreads(threads, "post");
        String route = "http://localhost:23727/app/get/activities";
        String response = getResponse(route);
        assertEquals(10, response.split(",\\{").length);

        executeThreads(threads, "delete");
        route = "http://localhost:23727/app/get/activities";
        response = getResponse(route);
        assertEquals("[]", response);
    }
}
