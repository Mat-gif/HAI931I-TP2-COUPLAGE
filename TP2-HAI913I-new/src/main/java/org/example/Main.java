package org.example;

import org.eclipse.jdt.core.dom.*;
import org.example.model.*;
import org.example.model.Type;
import org.example.parser.EclipseJDTParser;
import org.example.service.AstService;
import org.example.service.CouplingService;
import org.example.service.ExtractService;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Main {

//    private static final String path = "/home/mathieu/Documents/TP01_Poo-master/TP01_Poo";
    private static final String path = "/home/mathieu/Téléchargements/promotions";
    private static final AstService service = new AstService();


    private static HashMap<String, Classe> classes = new HashMap<>();

    public static void main(String[] args) throws IOException {

        EclipseJDTParser parserEclipse = new EclipseJDTParser(path);

        // Extract des classes et des methodes
        ExtractService extractService = new ExtractService();
        classes  = extractService.browse(parserEclipse);

        //Pretraitement pour couplage
        CouplingService couplingService = new CouplingService();
        HashMap<String, ClasseResume>  resume =couplingService.preTreatment(classes);

        resume.values().forEach(System.out::println);

    }



}