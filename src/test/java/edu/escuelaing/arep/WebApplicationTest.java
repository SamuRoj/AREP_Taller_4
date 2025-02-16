package edu.escuelaing.arep;

import edu.escuelaing.arep.http.HttpServer;
import org.junit.jupiter.api.*;

import java.io.*;
import java.net.URL;

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

    @Test
    public void shouldGetIndexHtml() {
        try {
            String inputLine = "";

            // Reading the file
            StringBuilder fileContent = new StringBuilder();
            try {
                BufferedReader in = new BufferedReader(new FileReader(("target/classes/static/index.html")));
                while((inputLine = in.readLine()) != null){
                    fileContent.append(inputLine);
                    if (!in.ready()) break;
                }
                in.close();
            } catch (FileNotFoundException e) {
                fail();
            }

            // Reading the response from the URL
            URL localhost = new URL("http://localhost:23727/");
            BufferedReader reader = new BufferedReader(new InputStreamReader(localhost.openStream()));
            StringBuilder response = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
                if (!reader.ready()) break;
            }
            reader.close();

            // Assertion
            assertEquals(response.toString(), fileContent.toString());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void shouldGetCss() {
        try {
            String inputLine = "";

            // Reading the file
            StringBuilder fileContent = new StringBuilder();
            try {
                BufferedReader in = new BufferedReader(new FileReader(("target/classes/static/styles.css")));
                while((inputLine = in.readLine()) != null){
                    fileContent.append(inputLine);
                    if (!in.ready()) break;
                }
                in.close();
            } catch (FileNotFoundException e) {
                fail();
            }

            // Reading the response from the URL
            URL localhost = new URL("http://localhost:23727/styles.css");
            BufferedReader reader = new BufferedReader(new InputStreamReader(localhost.openStream()));
            StringBuilder response = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
                if (!reader.ready()) break;
            }
            reader.close();

            // Assertion
            assertEquals(response.toString(), fileContent.toString());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void shouldGetJs() {
        try {
            String inputLine = "";

            // Reading the file
            StringBuilder fileContent = new StringBuilder();
            try {
                BufferedReader in = new BufferedReader(new FileReader(("target/classes/static/script.js")));
                while((inputLine = in.readLine()) != null){
                    fileContent.append(inputLine);
                    if (!in.ready()) break;
                }
                in.close();
            } catch (FileNotFoundException e) {
                fail();
            }

            // Reading the response from the URL
            URL localhost = new URL("http://localhost:23727/script.js");
            BufferedReader reader = new BufferedReader(new InputStreamReader(localhost.openStream()));
            StringBuilder response = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
                if (!reader.ready()) break;
            }
            reader.close();

            // Assertion
            assertEquals(response.toString(), fileContent.toString());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void shouldGreetWithStaticGet() {
        try {
            String inputLine;
            // Reading the response from the URL
            URL localhost = new URL("http://localhost:23727/app/hello");
            BufferedReader reader = new BufferedReader(new InputStreamReader(localhost.openStream()));
            StringBuilder response = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
                if (!reader.ready()) break;
            }
            reader.close();
            // Assertion
            assertEquals("{\"response\":\"Hello World!\"}", response.toString());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void shouldGreetWithStaticGetAndQuery() {
        try {
            String inputLine;
            // Reading the response from the URL
            URL localhost = new URL("http://localhost:23727/app/greeting?name=Samuel");
            BufferedReader reader = new BufferedReader(new InputStreamReader(localhost.openStream()));
            StringBuilder response = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
                if (!reader.ready()) break;
            }
            reader.close();
            // Assertion
            assertEquals("{\"response\":\"Hello Samuel\"}", response.toString());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void shouldGetValueOfPi() {
        try {
            String inputLine;

            // Reading the response from the URL
            URL localhost = new URL("http://localhost:23727/app/pi");
            BufferedReader reader = new BufferedReader(new InputStreamReader(localhost.openStream()));
            StringBuilder response = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
                if (!reader.ready()) break;
            }
            reader.close();
            // Assertion
            assertEquals("{\"response\":\"" + Math.PI + "\"}", response.toString());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void shouldGetValueOfE() {
        try {
            String inputLine;
            // Reading the response from the URL
            URL localhost = new URL("http://localhost:23727/app/e");
            BufferedReader reader = new BufferedReader(new InputStreamReader(localhost.openStream()));
            StringBuilder response = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
                if (!reader.ready()) break;
            }
            reader.close();
            // Assertion
            assertEquals("{\"response\":\"" + Math.E + "\"}", response.toString());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void shouldChangeDirectory() {
        try {
            String inputLine;

            // Reading the file
            StringBuilder fileContent = new StringBuilder();
            try {
                BufferedReader in = new BufferedReader(new FileReader(("target/classes/newFolder/index.html")));
                while((inputLine = in.readLine()) != null){
                    fileContent.append(inputLine);
                    if (!in.ready()) break;
                }
                in.close();
            } catch (FileNotFoundException e) {
                fail();
            }

            // Changing the folder
            URL localhost = new URL("http://localhost:23727/app/folder?folder=newFolder");
            localhost.openStream();

            // Reading the response from the URL
            localhost = new URL("http://localhost:23727/");
            BufferedReader reader = new BufferedReader(new InputStreamReader(localhost.openStream()));
            StringBuilder response = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
                if (!reader.ready()) break;
            }
            reader.close();
            // Assertion
            assertEquals(fileContent.toString(), response.toString());

            // Changing the folder
            localhost = new URL("http://localhost:23727/app/folder?folder=static");
            localhost.openStream();
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void shouldAddAndGetActivities() {
        try {
            String inputLine;

            // Deleting previous activities
            URL localhost = new URL("http://localhost:23727/app/delete/activities?time=12:00%20AM");
            localhost.openStream();
            localhost = new URL("http://localhost:23727/app/delete/activities?time=11:00%20AM");
            localhost.openStream();

            // Adding the activity
            localhost = new URL("http://localhost:23727/app/post/activities?time=12:00%20AM&activity=Sleep");
            localhost.openStream();

            // Reading the response from the URL
            localhost = new URL("http://localhost:23727/app/get/activities");
            BufferedReader reader = new BufferedReader(new InputStreamReader(localhost.openStream()));
            StringBuilder response = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
                if (!reader.ready()) break;
            }
            reader.close();

            // Assertion
            assertEquals("[{\"time\": \"12:00 AM\", \"activity\": \"Sleep\"}]", response.toString());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void shouldDeleteActivity() {
        try {
            String inputLine;

            // Deleting previous activities
            URL localhost = new URL("http://localhost:23727/app/delete/activities?time=12:00%20AM");
            localhost.openStream();
            localhost = new URL("http://localhost:23727/app/delete/activities?time=11:00%20AM");
            localhost.openStream();

            // Adding the activities
            localhost = new URL("http://localhost:23727/app/post/activities?time=12:00%20AM&activity=Sleep");
            localhost.openStream();
            localhost = new URL("http://localhost:23727/app/post/activities?time=11:00%20AM&activity=Shower");
            localhost.openStream();

            // Reading the response from the URL
            localhost = new URL("http://localhost:23727/app/get/activities");

            BufferedReader reader = new BufferedReader(new InputStreamReader(localhost.openStream()));
            StringBuilder response = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
                if (!reader.ready()) break;
            }
            reader.close();

            assertEquals("[{\"time\": \"12:00 AM\", \"activity\": \"Sleep\"}," +
                                    "{\"time\": \"11:00 AM\", \"activity\": \"Shower\"}]", response.toString());

            // Deleting activity
            localhost = new URL("http://localhost:23727/app/delete/activities?time=12:00%20AM");
            localhost.openStream();

            // Reading the response from the URL
            localhost = new URL("http://localhost:23727/app/get/activities");


            reader = new BufferedReader(new InputStreamReader(localhost.openStream()));
            response = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
                if (!reader.ready()) break;
            }
            reader.close();
            // Assertion
            assertEquals("[{\"time\": \"11:00 AM\", \"activity\": \"Shower\"}]", response.toString());
        } catch (IOException e) {
            fail();
        }
    }
}


