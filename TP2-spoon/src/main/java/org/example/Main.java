package org.example;

import org.example.model.Classe;
import org.example.model.ClasseResume;
import org.example.model.Coupling;
import org.example.service.ConstructService;
import org.example.service.CouplingService;
import org.example.service.MethodService;
import org.example.service.PreTreatmentService;
import org.example.ui.CouplingTemplate;
import org.example.ui.DendrogrammTemplate;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    private static String path = "/home/mathieu/Téléchargements/promotions(1)/promotions";
//   private static  String path = "/home/mathieu/Documents/TP01_Poo-master/TP01_Poo";

    private static HashMap<String, Classe> classes = new HashMap<>();

    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        launcher.addInputResource(path);
        CtModel model = launcher.buildModel();

        ConstructService constructService = new ConstructService();
        classes = constructService.extract(model,classes);
//        classes.values().forEach(System.out::println);



        MethodService methodService = new MethodService();
        classes =methodService.extract(model,classes);

//        classes.values().forEach(System.err::println);


        //Pretraitement pour couplage
        PreTreatmentService preTreatmentService = new PreTreatmentService();
        HashMap<String, ClasseResume>  resume =preTreatmentService.preTreatment(classes);
        int total = preTreatmentService.getGlobalTot();

        System.out.println(total);
        resume.values().forEach(System.out::println);

        //Calcul coupling
        CouplingService couplingService = new CouplingService();
        ArrayList<Coupling> couplings = couplingService.extractValue(resume,total);
//        System.out.println(" ");
        couplings.forEach(System.out::println);

        CouplingTemplate couplingTemplate = new CouplingTemplate();
        couplingTemplate.createGraph(couplings);
//
        int numRow = couplingService.classes.size();
        DendrogrammTemplate dendrogrammTemplate = new DendrogrammTemplate();
        dendrogrammTemplate.createGraph(couplings,numRow);

        DendroStructTemplate dendroStructTemplate = new DendroStructTemplate();
        dendroStructTemplate.setNModule(classes.size());
        dendroStructTemplate.createDendroStruct(couplings);


    }
}