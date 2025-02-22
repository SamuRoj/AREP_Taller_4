package edu.escuelaing.arep;

import edu.escuelaing.arep.annotations.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static edu.escuelaing.arep.http.HttpServer.*;

public class WebApplication {

    public static void main(String[] args) throws Exception {
        loadComponents();
        staticFiles("static");
        start(getPort());
    }

    private static void loadComponents() throws ClassNotFoundException {
        // Loading the files from the hard drive
        String env = getClassFolder();
        File folder = new File(env);
        List<Class<?>> classes = new ArrayList<>();

        for (File file : folder.listFiles()) {
            if (file.getName().endsWith(".class")) {
                String className = "edu.escuelaing.arep.controller." +
                        file.getName().replace(".class", "");
                classes.add(Class.forName(className));
            }
        }

        // Going through the classes and loading the methods in them
        for(Class<?> c : classes){
            if(c.isAnnotationPresent(RestController.class)){
                for(Method m : c.getDeclaredMethods()){
                    if(m.isAnnotationPresent(GetMapping.class)){
                        GetMapping a = m.getAnnotation(GetMapping.class);
                        get(a.value(), m);
                    }
                    else if(m.isAnnotationPresent(PostMapping.class)){
                        PostMapping a = m.getAnnotation(PostMapping.class);
                        get(a.value(), m);
                    }
                    else if(m.isAnnotationPresent(DeleteMapping.class)){
                        DeleteMapping a = m.getAnnotation(DeleteMapping.class);
                        get(a.value(), m);
                    }
                }
            }
        }
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 23727;
    }

    private static String getClassFolder() {
        if (System.getenv("CLASSES_DIR") != null) {
            return System.getenv("CLASSES_DIR");
        }
        return "target/classes/edu/escuelaing/arep/controller";
    }
}
