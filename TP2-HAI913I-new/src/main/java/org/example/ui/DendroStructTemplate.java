package org.example.ui;


import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import lombok.NoArgsConstructor;
import org.example.model.Coupling;
import org.example.model.Pair;
import org.example.service.ClusterService;
import org.example.service.DendrogrammService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

@NoArgsConstructor
public class DendroStructTemplate {

    private static final float CP = 0.15F;
    private int nModule ;
    private Map<Set<String>,Object> sommets = new HashMap<>();

    private List<Object> dendrogramme = new ArrayList<>();


    public void createDendroStruct(ArrayList<Coupling> couplings) {

        //affichage des valeurs de couplage
        System.out.println("\n\n#-- VALEURS DE COUPLAGE INITIALE--#");
        for(Coupling couple : couplings){
            System.out.println(couple);
        }

        ArrayList<Coupling> couplingsRestants = couplings;

        //tant que toutes les valeurs de couplages ne sont pas traitees
        while(!couplingsRestants.isEmpty()){

            System.out.println("\n\n#-- VALEURS DE COUPLAGE (en construction) --#");
            for(Coupling couple : couplings){
                System.out.println(couple);
            }

            //Recuperation de la valeur max de couplage et des classes associees
            Set<String> maxCoupleClassNames = null;
            float maxCoupleClassValue = 0;
            for(Coupling couple : couplings){
                if(couple.getValue()>maxCoupleClassValue){
                    maxCoupleClassValue = couple.getValue();
                    maxCoupleClassNames = couple.getClasses();
                }
            }

            //Determination des classes du plus grand couplage qui ne sont pas dans le dendro existant
            List<String> classeAAjouter = new ArrayList<>();
            for(String className : maxCoupleClassNames){
                if(!existInDendro(dendrogramme,className)){
                    classeAAjouter.add(className);
                }
            }

            //Ajout dans le dendrogramme de la/des classes du plus grand couplage
            List<Object> newDendrogramme = new ArrayList<>();

            if(!dendrogramme.isEmpty()){
                newDendrogramme.add(dendrogramme);
                if(classeAAjouter.size()==1){
                    newDendrogramme.add(classeAAjouter.get(0));
                } else {
                    newDendrogramme.add(classeAAjouter);
                }
            } else {
                newDendrogramme.add(classeAAjouter.get(0));
                newDendrogramme.add(classeAAjouter.get(1));
            }

            //fusion des noms de clés nécessaires
            for (Coupling couple : couplingsRestants) {
                boolean aAjouter = false;

                for (String classeMaxKey : maxCoupleClassNames) {
                    if (couple.getClasses().contains(classeMaxKey)) {
                        aAjouter = true;
                        break;
                    }
                }
                if (aAjouter) {
                    couple.getClasses().addAll(maxCoupleClassNames);
                }
            }


            System.out.println("\n#---- Dendrogramme en construction ----#");
            System.out.println(newDendrogramme);
            dendrogramme = newDendrogramme;



            ArrayList<Coupling> couplingsCopy = couplings;

            //remove de copyOfResultsCoupling le couplage maxKey,maxValue

            for (Coupling couple :  couplingsCopy) {
                if(couple.getClasses() == maxCoupleClassNames && couple.getValue() == maxCoupleClassValue) {
                    couplingsCopy.remove(couple);
                    break;
                }
            }
            //Si deux set ont les memes classes, suppr tous sauf 1 et additionner les couplages
            ArrayList<Coupling> copyOfResultsCouplingTempo = new ArrayList<>();
            for (Coupling couple : couplingsCopy) {
                HashSet<String> classNames = couple.getClasses();
                float value = 0;
                for (Coupling coupleATester : couplingsCopy) {
                    System.out.println("#######################################");
                    //verif les deux objets et parcourir pour voir s'ils ont les memes string
                    System.out.println("1 "+classNames);
                    System.out.println("2 "+coupleATester.getClasses());
                    System.out.println(coupleATester.getClasses().toString().equals(classNames.toString()));
                    System.out.println(!Objects.equals(coupleATester.getValue(), couple.getValue()));
                    if(coupleATester.getClasses().toString().equals(classNames.toString()) && !Objects.equals(coupleATester.getValue(), couple.getValue())) {
                        value = coupleATester.getValue()+couple.getValue();
                        System.out.println("DING DING DING "+value);
                    }
                }
                if (value!=0) {
                    copyOfResultsCouplingTempo.add(new Coupling((HashSet<String>) classNames, value));
                    System.out.println("KDFJGSKDLGHDKJG"+copyOfResultsCouplingTempo);
                } else {
                    copyOfResultsCouplingTempo.add(couple);
                }
            }

            couplingsRestants = copyOfResultsCouplingTempo;
        }

        System.out.println("\n\n#-- DENDROGRAMME --#");
        printDendro(dendrogramme, 0);
        System.out.println("\n#-- DENDROGRAMME (structure) --#");
        System.out.println(dendrogramme);

    }

    private boolean existInDendro(List<Object> dendro, String className) {
        for (Object c : dendro) {
            if (c instanceof List) {
                boolean existsInInnerList = existInDendro((List<Object>) c, className);
                if (existsInInnerList) {
                    return true;
                }
            } else if (c instanceof String && c.equals(className)) {
                return true;
            }
        }
        return false;
    }

    public static void printDendro(List<Object> nestedList, int depth) {
        for (Object item : nestedList) {
            if (item instanceof List) {
                printDendro((List<Object>) item, depth + 1);
            } else {
                // Ajoute des espaces pour décaler l'affichage en fonction de la profondeur
                for (int i = 0; i < depth; i++) {
                    System.out.print("-----");
                }
                System.out.println(item);
            }
        }
    }
}


