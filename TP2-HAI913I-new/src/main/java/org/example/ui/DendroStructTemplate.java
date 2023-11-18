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
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.util.List;
import java.util.*;

@NoArgsConstructor
public class DendroStructTemplate {

    private static final float CP = 0.03F;
    private int nModule ;

    private List<Object> dendrogramme = new ArrayList<>();

    private List<Float> couplingValuesList = new ArrayList<>();

    private List<Object> clusters = new ArrayList<>();

    public void createDendroStruct(ArrayList<Coupling> couplings) {

        //affichage des valeurs de couplage
//        System.out.println("\n\n#-- VALEURS DE COUPLAGE INITIALE--#");
//        for(Coupling couple : couplings){
//            System.out.println(couple);
//        }

        ArrayList<Coupling> couplingsRestants = couplings;
        couplingValuesList.add(0f);


        //tant que toutes les valeurs de couplages ne sont pas traitees
        while(!couplingsRestants.isEmpty()){

//            System.out.println("\n\n#-- VALEURS DE COUPLAGE (en construction) --#");
//            for(Coupling couple : couplingsRestants){
//                System.out.println(couple);
//            }

            //Recuperation de la valeur max de couplage et des classes associees
            Set<String> maxCoupleClassNames = null;
            float maxCoupleClassValue = Float.MIN_VALUE;

            for(Coupling couple : couplings){
                if(couple.getValue()>maxCoupleClassValue){
                    maxCoupleClassValue = couple.getValue();
                    maxCoupleClassNames = couple.getClasses();
                }
            }


            couplingValuesList.add(maxCoupleClassValue+couplingValuesList.get(couplingValuesList.size()-1));

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


//            System.out.println("\n#---- Dendrogramme en construction ----#");
//            System.out.println(newDendrogramme);
            dendrogramme = newDendrogramme;


            //remove de copyOfResultsCoupling le couplage maxKey,maxValue
            for (Coupling couple : couplings) {
                if(couple.getClasses() == maxCoupleClassNames && couple.getValue() == maxCoupleClassValue) {
                    couplings.remove(couple);
                    break;
                }
            }
            //Si deux set ont les memes classes, suppr tous sauf 1 et additionner les couplages
            HashSet<Coupling> copyOfResultsCouplingTempo = new HashSet<>();
            for (int i = 0; i < couplings.size(); i++){
                HashSet<String> classNames = couplings.get(i).getClasses();
                float value = 0;
                for (int j = 0; j < couplings.size(); j++){
                    //verif les deux objets et parcourir pour voir s'ils ont les memes string
                    if(couplings.get(j).getClasses().toString().equals(classNames.toString()) && !(i == j)) {
                        value = couplings.get(j).getValue()+ couplings.get(i).getValue();
                    }
                }

                if (value>0) {
                    Coupling newCoupleFusion = new Coupling((HashSet<String>) classNames, value);
                    if(!copyOfResultsCouplingTempo.contains(newCoupleFusion)){
                        copyOfResultsCouplingTempo.add(newCoupleFusion);
                    }
                } else {
                    Coupling newCoupleFusion = couplings.get(i);
                    if(!Objects.equals(newCoupleFusion.getClasses().toString(), maxCoupleClassNames.toString())) {
                        copyOfResultsCouplingTempo.add(couplings.get(i));
                    }
                }
            }
            couplingsRestants = new ArrayList<>(copyOfResultsCouplingTempo);
            couplings = couplingsRestants;
        }

        System.out.println("\n\n#-- DENDROGRAMME --#");
        printDendro(dendrogramme, 0);

        System.out.println("\n#-- DENDROGRAMME (structure) --#");
        System.out.println(dendrogramme);

        System.out.println("\n#-- VALEURS DE COUPLAGES à chaque niveau --#");
        System.out.println(couplingValuesList);

        System.out.println("\n#-- Max Clusters pour l'app : " + nModule + " | cp : " + CP + " --#");
        System.out.println("Profondeur : " + profondeurCluster(CP, couplingValuesList));

        System.out.println("\n#-- CLUSTERS --#");
        this.clusters = clusters(profondeurCluster(CP, couplingValuesList), dendrogramme);
        System.out.println("Nombre de cluster : " +clusters.size());
        for(Object cl : clusters){
            System.out.println("--"+cl);
        }
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
                    System.out.print("°-----");
                }
                System.out.println(item);
            }
        }
    }


    public void setNModule(int nbClass){
        this.nModule=nbClass/2;
    }

    public int profondeurCluster(Float CP, List<Float> couplingValuesList){

        Float v = couplingValuesList.get(couplingValuesList.size() - 1);
        for (int i = couplingValuesList.size() - 2; i >= 0; i--) {
            if(v-couplingValuesList.get(i) < CP){
                return i;
            }
        }
        return 0;
    }

    public List<Object> clusters(int profondeur, List<Object> dendrogramme ){
        List<Object> clusters = new ArrayList<>();

        for (Object item : dendrogramme) {
            if (item instanceof List) {
                if(profondeur > 1) {
                    clusters.addAll(clusters(profondeur - 1, (List<Object>) item));
                } else {
                    clusters.add(item);
                }
            } else {
                clusters.add(item);
            }
        }

        return clusters;
    }
}


