package org.example;

import org.example.model.*;
import org.example.parser.EclipseJDTParser;
import org.example.service.AstService;
import org.example.service.CouplingService;
import org.example.service.PreTreatmentService;
import org.example.service.ExtractService;
import org.example.ui.CouplingTemplate;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

import smile.io.*;

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
        PreTreatmentService preTreatmentService = new PreTreatmentService();
        HashMap<String, ClasseResume>  resume =preTreatmentService.preTreatment(classes);
        int total = preTreatmentService.getGlobalTot();

        //Calcul coupling
        CouplingService couplingService = new CouplingService();
        ArrayList<Coupling> couplings = couplingService.extractValue(resume,total);

        couplings.forEach(System.out::println);

        //Graph de couplage
//        CouplingTemplate couplingTemplate = new CouplingTemplate();
//        couplingTemplate.createGraph(couplings);


//        couplingService.classes;

        int numRow = couplingService.classes.size();
        double[][] matrix = initMatrix(numRow);

        afficherM(matrix,numRow);
        matrix = uptdateMatrix(matrix,numRow,couplings,couplingService.classes);
    }


    private static double[][] initMatrix(int max)
    {
        double[][] matrix = new double[max][max];

        // Remplir la matrice avec des zéros
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                matrix[i][j] = 0.0000000000;
            }
        }
        return matrix;
    }

    private static void afficherM(double[][] matrix,int max)
    {
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                System.out.print(matrix[i][j] + " ---------");
            }
            System.out.println();
        }
    }


    private static double[][] uptdateMatrix(double[][] matrix,int max,ArrayList<Coupling> couplings, HashSet<String> names )
    {
        ArrayList<String> arrayList = new ArrayList<>(names);

        System.out.println(arrayList);

        for (Coupling coupling : couplings)
        {
            String classe1 = (String) coupling.getClasses().toArray()[0];
            String classe2 = (String) coupling.getClasses().toArray()[1];
            int index1 = arrayList.indexOf(classe1);
            int index2 = arrayList.indexOf(classe2);
            matrix[index1][index2] = coupling.getValue();
            matrix[index2][index1] = coupling.getValue();

        }


        afficherM(matrix,max);

        return matrix;
    }


}