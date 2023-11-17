package org.example;

import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.reflect.CtModel;

import java.util.Objects;

public class MainSpoon {
    public static void main(String[] args) {
        SpoonAPI spoon = new Launcher();
        spoon.addInputResource("C:\\Users\\victo\\eclipse-workspace\\promotions");
//        spoon.addInputResource("C:\\Users\\victo\\Downloads\\TP01_Poo-master\\TP01_Poo");
//        spoon.addInputResource("C:\\Users\\victo\\eclipse-workspace\\HAI822I");
//        spoon.addInputResource("/home/mathieu/Téléchargements/promotions");
        spoon.buildModel();
        CtModel model = spoon.getModel();


        spoon.getModel().getAllTypes().forEach(e->{
            if(Objects.equals(e.getSimpleName(), "Examen"))
            {
                e.getAllMethods().forEach(System.out::println);
            }

        });
    }
}
