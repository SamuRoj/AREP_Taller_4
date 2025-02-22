package edu.escuelaing.arep.threads;

import java.io.IOException;
import java.net.URL;

public class ClientThread extends Thread {

    private final String action;
    private final int identifier;

    public ClientThread(String action, int identifier){
        this.action = action;
        this.identifier = identifier;
    }

    public int getIdentifier(){
        return this.identifier;
    }

    @Override
    public void run() {
        URL localhost;
        if(action.equals("get")){
            try {
                localhost = new URL("http://localhost:23727/app/get/activities");
                localhost.openStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if(action.equals("post")){
            try {
                localhost = new URL("http://localhost:23727/app/post/activities?time=" + getIdentifier() +
                                    "&activity=" + getIdentifier());
                localhost.openStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if(action.equals("delete")) {
            try {
                localhost = new URL("http://localhost:23727/app/delete/activities?time=" + getIdentifier());
                localhost.openStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
