package edu.escuelaing.arep.http;

import edu.escuelaing.arep.annotations.RequestParam;
import edu.escuelaing.arep.model.Activity;
import edu.escuelaing.arep.model.Pair;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class HttpServer {

    private static List<Activity> activities = new ArrayList<>();
    private static final int PORT = 23727;
    private static String route = "target/classes/";
    private static Map<String, Pair<Method, LinkedHashMap<String, String>>> services= new HashMap();
    private static boolean isRunning = true;

    public static void start() throws IOException, InvocationTargetException, IllegalAccessException, URISyntaxException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started through port " + PORT);
        while(isRunning){
            Socket clientSocket = serverSocket.accept();
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
            clientSocket.close();
        }
        serverSocket.close();
    }

    static void processRequest(HttpRequest req, HttpResponse res) throws InvocationTargetException, IllegalAccessException {
        Pair<Method, LinkedHashMap<String, String>> service = services.get(req.getPath());
        Method method = service.getFirst();
        LinkedHashMap<String, String> params = service.getSecond();
        String ans = "";
        ArrayList<String> keys = new ArrayList<>(params.keySet());
        String header ="HTTP/1.1 200 OK\r\n,"
                + "Content-Type: application/json\r\n\r\n,";

        if(req.getPath().contains("greeting") ){
            String name = req.getValues(keys.get(0)) != null ? req.getValues(keys.get(0)) : params.get(keys.get(0));
            ans = "{\"response\":\""+ method.invoke(null, name) +"\"}";
        }
        else if(req.getPath().contains("folder")){
            String folder = req.getValues(keys.get(0)) != null ? req.getValues(keys.get(0)) : params.get(keys.get(0));
            method.invoke(null, folder);
            ans = "{\"response\":\" Folder has been changed to " + route +"\"}";
        }
        else if(req.getPath().contains("get/activities")){
            ans = (String) method.invoke(null);
        }
        else if(req.getPath().contains("post/activities")){
            String time = req.getValues(keys.get(0)) != null ? req.getValues(keys.get(0)) : params.get(keys.get(0));
            String name = req.getValues(keys.get(1)) != null ? req.getValues(keys.get(1)) : params.get(keys.get(1));
            header = (String) method.invoke(null, time, name);
        }
        else if(req.getPath().contains("delete/activities")){
            String time = req.getValues(keys.get(0)) != null ? req.getValues(keys.get(0)) : params.get(keys.get(0));
            header = (String) method.invoke(null, time);
        }
        else{
            ans = "{\"response\":\""+ method.invoke(null) +"\"}";
        }

        res.parseValues(header + ans);
        res.send();
    }

    public static String obtainFile(HttpRequest req, OutputStream out) throws IOException {
        String path = req.getPath();
        String file = path.equals("/") ? "index.html" : path.split("/")[1];
        String extension = file.split("\\.")[1];
        String header = obtainContentType(extension);
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: " + header + "\r\n" +
                "\r\n";
        String notFound = "HTTP/1.1 404 Not Found\r\n"
                + "Content-Type: text/plain\r\n"
                + "\r\n";
        String inputLine;
        StringBuilder fileContent = new StringBuilder();
        if(extension.equals("html") || extension.equals("css") || extension.equals("js")){
            try {
                BufferedReader in = new BufferedReader(new FileReader((route +  "/" +  file)));
                while((inputLine = in.readLine()) != null){
                    fileContent.append(inputLine).append("\n");
                    if (!in.ready()) break;
                }
                in.close();
                return response + fileContent;
            } catch (FileNotFoundException e) {
                return notFound;
            }
        }
        else if(extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png")){
            obtainImage(file, response, out);
            return response;
        }
        else return notFound;
    }

    public static String obtainContentType(String extension){
        if(extension.equals("html") || extension.equals("css")) return "text/" + extension;
        else if(extension.equals("js")) return "text/javascript";
        else if(extension.equals("jpg") || extension.equals("jpeg")) return "image/jpeg";
        else if(extension.equals("png")) return "image/png";
        return "text/plain";
    }

    static void obtainImage(String file, String response, OutputStream out) throws IOException {
        File imageFile = new File(route + "/" + file);
        if(imageFile.exists()){
            FileInputStream fis = new FileInputStream(imageFile);
            byte[] imageBytes = new byte[(int) imageFile.length()];
            fis.read(imageBytes);
            out.write(response.getBytes());
            out.write(imageBytes);
        }
        else out.write(("HTTP/1.1 404 Not Found\r\n"
                + "Content-Type: text/plain\r\n"
                + "\r\n").getBytes());
    }

    public static List<Activity> getActivities(){
        return activities;
    }

    public static void setActivities(List<Activity> newActivities){
        activities = newActivities;
    }

    public static void get(String route, Method method){
        LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
        for(Parameter p : method.getParameters()){
            if(p.isAnnotationPresent(RequestParam.class)){
                RequestParam a = p.getAnnotation(RequestParam.class);
                parameters.put(a.value(), a.defaultValue());
            }
        }
        Pair<Method, LinkedHashMap<String, String>> methodPair = new Pair<>(method, parameters);
        services.put("/app" + route, methodPair);
    }

    public static void staticFiles(String path){
        route = "target/classes/" + path;
    }

    public static void stop(){
        isRunning = false;
    }
}
