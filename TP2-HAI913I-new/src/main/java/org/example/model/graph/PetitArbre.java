package org.example.model.graph;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Getter
@Data
@NoArgsConstructor
public class PetitArbre {

    private Noeud parent;
    private Set<Noeud> enfants = new HashSet<Noeud>();
    private int countTotalByClass = 0;


    public PetitArbre(Noeud parent) {
        this.parent = parent;
    }

    public void setParent(Noeud parent) {
        this.parent = parent;
    }

    public void setEnfants(Set<Noeud> enfants) {
        this.enfants = enfants;
    }

    public void addEnfant2(Noeud enfant) {

        boolean existe = false;
        if (!enfant.getClasseName().equals(getParent().getClasseName())) {
            for (Noeud e : enfants) {
                if (e.getClasseName().equals(enfant.getClasseName())) {
                    existe = true;
                    e.ajoutAppel();
                    break;
                }
            }
            if (!existe) {
                getEnfants().add(enfant);
            }

            countTotalByClass = getCountTotalByClass() + 1;
        }


    }


    @Override
    public String toString() {
        return "PetitArbre [parent=" + parent + ", enfants=" + enfants + ", countTotalByClass=" + countTotalByClass
                + "]";
    }


}
