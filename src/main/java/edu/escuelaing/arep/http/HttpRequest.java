package edu.escuelaing.arep.http;

public class HttpRequest {
    private final String path;
    private final String query;

    public HttpRequest(String path, String query) {
        this.path = path;
        this.query = query;
    }

    public String getPath() {
        return path;
    }

    public String getQuery() {
        return query;
    }

    public String getValues(String value){
        if(this.query != null){
            String[] params = this.query.split("&");
            for(String i : params){
                if(i.split("=")[0].equals(value)) return i.split("=")[1];
            }
        }
        return null;
    }
}