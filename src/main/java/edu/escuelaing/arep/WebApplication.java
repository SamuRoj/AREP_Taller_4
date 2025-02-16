package edu.escuelaing.arep;

import edu.escuelaing.arep.annotations.*;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import static edu.escuelaing.arep.http.HttpServer.*;

public class WebApplication {

    public static void main(String[] args) throws Exception {
        loadComponents();
        staticFiles("static");
        start();
    }

    private static void loadComponents() throws URISyntaxException, ClassNotFoundException {
        // Loading the files from the hard drive
        String packageName = "edu.escuelaing.arep.controller";
        String path = packageName.replace('.', '/');
        File folder = new File("target/classes/" + path);
        List<Class<?>> classes = new ArrayList<>();

        for (File file : folder.listFiles()) {
            if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().replace(".class", "");
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
}
