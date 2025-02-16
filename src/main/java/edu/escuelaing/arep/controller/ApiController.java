package edu.escuelaing.arep.controller;

import edu.escuelaing.arep.annotations.*;
import edu.escuelaing.arep.http.HttpServer;
import edu.escuelaing.arep.model.Activity;

import java.util.List;
import java.util.function.Predicate;

@RestController
public class ApiController {

    @GetMapping("/get/activities")
    public static String getActivities() {
        StringBuilder json = new StringBuilder();
        json.append("[");

        boolean first = true;
        for (Activity a : HttpServer.getActivities()) {
            if (!first) {
                json.append(",");
            }
            first = false;
            json.append("{")
                    .append("\"time\": \"").append(a.getTime()).append("\", ")
                    .append("\"activity\": \"").append(a.getName()).append("\"")
                    .append("}");
        }

        json.append("]");
        return json.toString();
    }

    @PostMapping("/post/activities")
    public static String addActivity(@RequestParam(value = "time", defaultValue = "12:00 AM") String time,
                                     @RequestParam(value = "activity", defaultValue = "Sleep") String activity) {
        List<Activity> newActivities = HttpServer.getActivities();
        newActivities.add(new Activity(time, activity));
        HttpServer.setActivities(newActivities);
        return "HTTP/1.1 201 Accepted\r\n,"
                + "Content-Type: text/plain\r\n\r\n,";
    }

    @DeleteMapping("/delete/activities")
    public static String deleteActivity(@RequestParam(value = "time", defaultValue = "12:00 AM") String time) {
        Predicate<Activity> condition = activity -> activity.getTime().equals(time);
        List<Activity> newActivities = HttpServer.getActivities();
        newActivities.removeIf(condition);
        HttpServer.setActivities(newActivities);
        return "HTTP/1.1 201 Accepted\r\n,"
                + "Content-Type: text/plain\r\n\r\n,";
    }
}
