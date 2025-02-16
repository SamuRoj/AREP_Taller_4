package edu.escuelaing.arep.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import static edu.escuelaing.arep.http.HttpServer.obtainFile;
import static edu.escuelaing.arep.http.HttpServer.processRequest;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine = "";
            boolean isFirstLine = true;
            String file = "";

            while((inputLine = in.readLine()) != null){
                if(isFirstLine){
                    file = inputLine.split(" ")[1];
                    isFirstLine = false;
                }
                if (!in.ready()) break;
            }

            URI resourceURI = new URI(file);
            HttpRequest req = new HttpRequest(resourceURI.getPath(), resourceURI.getQuery());
            HttpResponse res = new HttpResponse(out);
            if (req.getPath().startsWith("/app")) processRequest(req, res);
            else out.println(obtainFile(req, clientSocket.getOutputStream()));
            in.close();
            out.close();
        } catch (IOException | URISyntaxException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Client socket closed.");
            }
        }
    }
}
