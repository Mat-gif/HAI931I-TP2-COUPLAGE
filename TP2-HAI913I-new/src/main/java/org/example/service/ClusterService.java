package org.example.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.model.Module;

import java.util.*;

@Getter
@NoArgsConstructor
public class ClusterService {
    private  LinkedList<Module> modules = new LinkedList<>();
    private int tot = 0;
    private float somme = 0;
    private float moyenne = 0;

    public void calcul(float value, Set<String> classeAB, int nModule, float cp) {
//        System.out.println(classeAB);
        tot+=1;
        somme+=value;
        moyenne = somme/tot;
        if(modules.isEmpty()) {
            modules.add(new Module());
        }
        if (moyenne>cp && modules.size() <= nModule)
        {
            modules.getLast().getClasses().addAll(classeAB);
            modules.getLast().setMoyenne(moyenne);
            return;
        }
        if(moyenne<cp && modules.size() <= nModule)
        {
            modules.add(new Module());
            tot=1;
            somme=value;
            moyenne = somme/tot;
            // boff boofffff
            modules.getLast().getClasses().addAll(classeAB);
            modules.getLast().setMoyenne(moyenne);
        }

    }
}
