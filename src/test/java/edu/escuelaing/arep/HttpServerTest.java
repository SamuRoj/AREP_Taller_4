package edu.escuelaing.arep;

import edu.escuelaing.arep.http.HttpRequest;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import edu.escuelaing.arep.http.HttpServer;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerTest {
    @Test
    public void shouldObtainFile() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            HttpRequest req = new HttpRequest("/wallpaper.jpeg", "");
            String response = HttpServer.obtainFile(req, outputStream);
            assertEquals("HTTP/1.1 200 OK\r\n" +
                    "Content-Type: image/jpeg\r\n" +
                    "\r\n", response);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void shouldNotObtainFile() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            HttpRequest req = new HttpRequest("/page.html", "");
            HttpServer.obtainFile(req, outputStream);

        } catch (IOException e) {
            assertTrue(true);
        }
    }

    @Test
    public void shouldObtainHtml() {
        String extension = HttpServer.obtainContentType("html");
        assertEquals("text/html", extension);
    }

    @Test
    public void shouldObtainCss() {
        String extension = HttpServer.obtainContentType("css");
        assertEquals("text/css", extension);
    }

    @Test
    public void shouldObtainJs() {
        String extension = HttpServer.obtainContentType("js");
        assertEquals("text/javascript", extension);
    }

    @Test
    public void shouldObtainImage() {
        String extension = HttpServer.obtainContentType("jpg");
        assertEquals("image/jpeg", extension);
    }
}