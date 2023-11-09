package org.example;

import org.example.model.*;
import org.example.parser.EclipseJDTParser;
import org.example.service.AstService;
import org.example.service.CouplingService;
import org.example.service.PreTreatmentService;
import org.example.service.ExtractService;
import org.example.ui.CouplingTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Main {

    private static final String path = "/home/mathieu/Documents/TP01_Poo-master/TP01_Poo";
//    private static final String path = "/home/mathieu/Téléchargements/promotions";
    private static final AstService service = new AstService();


    private static HashMap<String, Classe> classes = new HashMap<>();

    public static void main(String[] args) throws IOException {

        EclipseJDTParser parserEclipse = new EclipseJDTParser(path);

        // Extract des classes et des methodes
        ExtractService extractService = new ExtractService();
        classes  = extractService.browse(parserEclipse);

        //Pretraitement pour couplage
        PreTreatmentService preTreatmentService = new PreTreatmentService();
        HashMap<String, ClasseResume>  resume =preTreatmentService.preTreatment(classes);
        int total = preTreatmentService.getGlobalTot();

        //Calcul coupling
        CouplingService couplingService = new CouplingService();
        ArrayList<Coupling> couplings = couplingService.extractValue(resume,total);

        couplings.forEach(System.out::println);

        //Graph de couplage
        CouplingTemplate couplingTemplate = new CouplingTemplate();
        couplingTemplate.createGraph(couplings);

    }



}