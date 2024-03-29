package org.example.service;

import lombok.NoArgsConstructor;
import org.example.model.ClasseResume;
import org.example.model.Coupling;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@NoArgsConstructor
public class CouplingService {

    public HashSet<String> classes = new HashSet<>();

    public ArrayList<Coupling> extractValue(HashMap<String, ClasseResume> resume, int total) {
        float tot = 0;
        ArrayList<Coupling> couplings = new ArrayList<>();
        for (String key1 : resume.keySet())
        {
            ClasseResume classe1 = resume.get(key1);
            for (String key2 : classe1.getCountByClass().keySet())
            {
                int value1 = classe1.getCountByClass().get(key2);
                int value2 = 0;
                if (resume.get(key2) != null && resume.get(key2).getCountByClass().get(key1) != null) {
                    value2 = resume.get(key2).getCountByClass().get(key1);
                }
                float resultat = calculate(value1,value2,total);
                HashSet<String> keyCouple = new HashSet<>();
                keyCouple.add(key1);
                keyCouple.add(key2);
                classes.add(key1);
                classes.add(key2);
                boolean exist = false;
                for (Coupling coupling : couplings)
                {
                    if (coupling.getClasses().equals(keyCouple)) {
                        exist = true;

                        break;
                    }
                }
                if (!exist) {
                    tot+=resultat;
                    couplings.add(Coupling.builder().classes(keyCouple).value(resultat).build());
                }
            }
        }
        System.err.println(tot);
        return couplings;
    }


    private float calculate(
            int incomingRelationships,
            int outgoingRelationships,
            int totalBinaryMethodRelations
    ) {
        DecimalFormat df = new DecimalFormat("0.0000");
        String formattedResult = df.format((float) (incomingRelationships + outgoingRelationships) / totalBinaryMethodRelations);
        formattedResult = formattedResult.replace(',', '.');
        return Float.parseFloat(formattedResult);
    }
}
