package org.example.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.model.*;

import java.util.HashMap;

@Getter
@NoArgsConstructor
public class PreTreatmentService {

    private int globalTot=0;

    public HashMap<String, ClasseResume> preTreatment(HashMap<String, Classe> classes) {

        HashMap<String, ClasseResume> classeResumes = new HashMap<>();



         for (Classe classe : classes.values())
         {
             int localTot =0;
             HashMap<String, Integer> countByClass = new HashMap<>();
//             System.out.println("----> " + classe.getName() );
             if (classe.getMethods().isEmpty())continue;

             for (Method method : classe.getMethods().values())
             {
                 if (method.getInvocations().isEmpty())continue;
//                 System.out.println(method.getName());
//                 System.out.println(method.getParameters());
                 for(Inv invocation : method.getInvocations())
                 {

//                     System.out.println(invocation);
                     if (invocation instanceof InvMethod )
                     {

                         if (((InvMethod) invocation).getInstance()!=null && !((InvMethod) invocation).getInstance().equals("THIS")
                         ){
                             int val = 0;
                             if (countByClass.get(((InvMethod) invocation).getInstance()) != null)
                                 val = countByClass.get(((InvMethod) invocation).getInstance());
                             val += 1;
                             localTot += 1;
                             countByClass.put(((InvMethod) invocation).getInstance(), val);
                         }
                         continue;
                     }
                     if (invocation instanceof InvContruct )
                     {
                         if (((InvContruct) invocation).getInstance()!=null)
                         {
                             int val = 0;

                             if (countByClass.get(((InvContruct) invocation).getInstance()) != null && !((InvContruct) invocation).getInstance().equals("THIS") )
                                 val = countByClass.get(((InvContruct) invocation).getInstance());
                             val += 1;
                             localTot += 1;
                             countByClass.put(((InvContruct) invocation).getInstance(), val);
                         }
                         continue;
                     }
                 }
             }
             classeResumes.put(classe.getName(),ClasseResume.builder().name(classe.getName()).tot(localTot).countByClass(countByClass).build());
             globalTot = globalTot+localTot;
         }

        return classeResumes;
    }
}
