package org.example;

import org.example.model.*;
import org.example.parser.EclipseJDTParser;
import org.example.service.*;
import org.example.ui.ClusteringDetermination;
import org.example.ui.CouplingTemplate;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import org.example.ui.DendroStructTemplate;
import org.example.ui.DendrogrammTemplate;
import smile.io.*;

public class Main {

//    private static final String path = "/home/mathieu/Documents/TP01_Poo-master/TP01_Poo";
//    private static final String path = "C:\\Users\\victo\\eclipse-workspace\\HAI822I";
//    private static final String path = "C:\\Users\\victo\\Downloads\\TP01_Poo-master\\TP01_Poo";
    private static final String path = "C:\\Users\\victo\\eclipse-workspace\\promotions";
//
//        private static final String path = "/home/mathieu/Téléchargements/promotions(1)/promotions";
    private static final AstService service = new AstService();


    private static HashMap<String, Classe> classes = new HashMap<>();


    public static void main(String[] args) throws IOException {

        EclipseJDTParser parserEclipse = new EclipseJDTParser(path);

        // Extract des classes et des methodes
        ExtractService extractService = new ExtractService();
        classes  = extractService.browse(parserEclipse);

        classes.values().forEach(System.out::println);

        //Pretraitement pour couplage
        PreTreatmentService preTreatmentService = new PreTreatmentService();
        HashMap<String, ClasseResume>  resume =preTreatmentService.preTreatment(classes);
        int total = preTreatmentService.getGlobalTot();

//        resume.values().forEach(System.out::println);

        //Calcul coupling
        CouplingService couplingService = new CouplingService();
        ArrayList<Coupling> couplings = couplingService.extractValue(resume,total);
        ArrayList<Coupling> coupling2s = couplingService.extractValue(resume,total);

        couplings.forEach(System.out::println);


        //Graph de couplage
        CouplingTemplate couplingTemplate = new CouplingTemplate();
        couplingTemplate.createGraph(couplings);

        int numRow = couplingService.classes.size();
        DendrogrammTemplate dendrogrammTemplate = new DendrogrammTemplate();
        dendrogrammTemplate.createGraph(couplings,numRow);

        DendroStructTemplate dendroStructTemplate = new DendroStructTemplate();
        dendroStructTemplate.setNModule(classes.size());
        dendroStructTemplate.createDendroStruct(coupling2s);


        //Test de clustering 2 (non concluant)
//        ClusteringDetermination clusterDeter = new ClusteringDetermination();
//        clusterDeter.setCouplings(couplings);
//        List<List<String>> clusters = clusterDeter.findClusters(dendroStructTemplate.getDendrogramme(),dendroStructTemplate.getNModule(),dendroStructTemplate.getCP());
//
//        System.err.println("#############"+clusters);



//        couplingService.classes;

//        int numRow = couplingService.classes.size();
//        MatriceService matriceService = new MatriceService();
//        double[][] matrix = matriceService.uptdateMatrix(numRow,couplings,couplingService.classes);
    }


}