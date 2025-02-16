package edu.escuelaing.arep.controller;

import edu.escuelaing.arep.annotations.PostMapping;
import edu.escuelaing.arep.http.HttpServer;
import edu.escuelaing.arep.annotations.GetMapping;
import edu.escuelaing.arep.annotations.RequestParam;
import edu.escuelaing.arep.annotations.RestController;

@RestController
public class ServerController {

    @GetMapping("/hello")
    public static String hello() {
        return "Hello World!";
    }

    @GetMapping("/greeting")
    public static String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name;
    }

    @PostMapping("/folder")
    public static void changeFolder(@RequestParam(value = "folder", defaultValue = "/static") String folder) {
        HttpServer.staticFiles(folder);
    }
}