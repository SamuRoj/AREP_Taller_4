package edu.escuelaing.arep;

import edu.escuelaing.arep.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HttpResponseTest {
    private HttpResponse httpResponse;

    @BeforeEach
    public void setup() {
        String values = "HTTP/1.1 200 OK\r\n,"
                + "Content-Type: application/json\r\n\r\n,";
        httpResponse = new HttpResponse(values);
    }

    @Test
    public void shouldParseValues() {
        assertNotNull(httpResponse.getHeader());
        assertNotNull(httpResponse.getContentType());
        assertTrue(httpResponse.getBody().isEmpty());
    }
}
