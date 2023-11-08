package org.example.model.graph;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
public class Noeud {

    private String classeName;
    private int nbAppel = 1;

    public Noeud(String classeName) {
        this.classeName = classeName;
    }

    public void setClasseName(String classeName) {
        this.classeName = classeName;
    }

    public void setNbAppel(int nbAppel) {
        this.nbAppel = nbAppel;
    }

    public void ajoutAppel() {
        this.nbAppel += 1;
    }


    @Override
    public String toString() {
        return "Noeud{" +
                "classeName='" + classeName + '\'' +
                ", nbAppel=" + nbAppel +
                '}';
    }
}
