package edu.escuelaing.arep.model;

public class Activity {
    private String time;
    private String name;

    public Activity(String time, String name){
        this.time = time;
        this.name = name;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}