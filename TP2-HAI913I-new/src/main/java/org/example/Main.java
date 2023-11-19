package org.example;

import org.example.model.*;
import org.example.parser.EclipseJDTParser;
import org.example.service.*;
import org.example.ui.CouplingTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.example.ui.DendroStructTemplate;
import org.example.ui.DendrogrammTemplate;

public class Main {

//    private static final String path = "/home/mathieu/Documents/TP01_Poo-master/TP01_Poo";
//    private static final String path = "C:\\Users\\victo\\eclipse-workspace\\HAI822I";
//    private static final String path = "C:\\Users\\victo\\Downloads\\TP01_Poo-master\\TP01_Poo";
//    private static final String path = "C:\\Users\\victo\\eclipse-workspace\\promotions";
//
//        private static final String path = "/home/mathieu/Téléchargements/promotions(1)/promotions";
        private static  String path ;
    private static final AstService service = new AstService();


    private static HashMap<String, Classe> classes = new HashMap<>();


    public static void main(String[] args) throws IOException {
        System.out.println("████████╗████████╗██████╗░");
        System.out.println("╚══██╔══╝██╔═══██╗╚════██╗");
        System.out.println("░░░██║░░░███████╔╝░░░██╔═╝");
        System.out.println("░░░██║░░░██╔═══╝░░░░██╔═╝░");
        System.out.println("░░░██║░░░██║░░░░░░███████╗");
        System.out.println("░░░╚═╝░░░╚═╝░░░░░░╚══════╝");




        System.out.println();
        Scanner scanner = new Scanner(System.in);
        String projectPath = "";

        while (true) {
            System.out.print("Veuillez saisir le chemin du projet : ");
            projectPath = scanner.nextLine();

            // Vérifie si le chemin est valide en vérifiant s'il existe
            File file = new File(projectPath+"/src");
            if (file.exists() && file.isDirectory()) {
                break; // Sort de la boucle si le chemin est valide
            } else {
                System.out.println("Chemin invalide. Veuillez saisir un chemin valide.");
            }
        }

        System.out.println("Le chemin du projet que vous avez saisi est : " + projectPath);
        path = projectPath;
        run();

        scanner.close();
    }





    public static void run() throws IOException {
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

    }

}