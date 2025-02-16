package edu.escuelaing.arep.controller;

import edu.escuelaing.arep.annotations.GetMapping;
import edu.escuelaing.arep.annotations.RestController;

@RestController
public class MathController {

    @GetMapping("/pi")
    public static String pi() {
        return Double.toString(Math.PI);
    }

    @GetMapping("/e")
    public static String e() {
        return Double.toString(Math.E);
    }
}