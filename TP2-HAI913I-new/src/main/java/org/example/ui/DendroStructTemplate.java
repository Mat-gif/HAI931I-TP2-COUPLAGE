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
        System.out.println("\n\n#-- VALEURS DE COUPLAGE --#");
        for(Coupling couple : couplings){
            System.out.println(couple);
        }

        ArrayList<Coupling> couplingsRestants = couplings;

        //tant que toutes les valeurs de couplages ne sont pas traitees
        while(!couplingsRestants.isEmpty()){

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
            newDendrogramme.add(dendrogramme);
            if(classeAAjouter.size()==1){
                newDendrogramme.add(classeAAjouter.get(0));
            } else {
                newDendrogramme.add(classeAAjouter);
            }
            dendrogramme = newDendrogramme;


            //remove de copyOfResultsCoupling le couplage maxKey,maxValue
            for (Coupling couple :  couplings) {
                if(couple.getClasses() == max && couple.getValue() == maxValue) {
                    copyOfResultsCoupling.remove(couple);
                    break;
                }
            }
            //Si deux set ont les memes classes, suppr tous sauf 1 et additionner les couplages

            ArrayList<Couplage> copyOfResultsCouplingTempo = new ArrayList<>();
//                System.err.println("***");
            for (Couplage couple : copyOfResultsCoupling) {
                Set<String> key = couple.getClasses();

//                    System.err.println(key);
                float value = 0;
                for (Couplage coupleATester : copyOfResultsCoupling) {

                    if(coupleATester.getClasses().equals(key) && !Objects.equals(coupleATester.getValue(), couple.getValue())) {
                        System.out.println(coupleATester.getValue());
                        value = coupleATester.getValue()+couple.getValue();
                    }
                }
                if (value!=0) {
                    copyOfResultsCouplingTempo.add(new Couplage(key, value));
                } else {
                    copyOfResultsCouplingTempo.add(couple);
                }
            }
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
}
