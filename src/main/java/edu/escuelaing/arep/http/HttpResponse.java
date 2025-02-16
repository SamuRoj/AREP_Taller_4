package edu.escuelaing.arep.http;

import java.io.PrintWriter;

public class HttpResponse {

    private String header;
    private String contentType;
    private String body;
    private final PrintWriter out;

    public HttpResponse(PrintWriter out) {
        this.out = out;
    }

    public HttpResponse(String values) {
        parseValues(values);
        this.out = null;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public String getContentType() {
        return contentType;
    }

    public String getBody() {
        return body;
    }

    public void parseValues(String values){
        String[] params = values.split(",", 3);
        setHeader(params[0]);
        setContentType(params[1]);
        setBody(params[2]);
    }

    public void send() {
        out.println(header + contentType + body);
    }
}
