package org.example.service;


import lombok.NoArgsConstructor;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.example.model.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class BindingService {
    public ArrayList<Inv> findSolution(
            ArrayList<Inv> invocations,
            HashMap<String, Field> fields,
            HashMap<String, Argument> parameters,
            List<ImportDeclaration> imports,
            HashMap<String, Variable> variables
    ) {
    if (invocations != null)
    {

        ArrayList<InvContruct> constructs = invocations.stream()
                .filter(inv -> inv instanceof InvContruct)
                .map(inv -> (InvContruct) inv)
                .collect(Collectors.toCollection(ArrayList::new));

        HashMap<String, InvContruct> constructsMap = new HashMap<>();
        for (InvContruct element : constructs) {
            constructsMap.put(element.getName(), element);
        }

       for (Inv invocation : invocations)
       {
           if (invocation instanceof InvMethod)
           {
              if (!constructs.isEmpty() )
              {
                  InvContruct construct = constructsMap.get(((InvMethod) invocation).getObj());
                  if (construct != null) {
                      ((InvMethod) invocation).setInstance(construct.getInstance());
                      continue;
                  }
              }

              if (!variables.isEmpty())
              {
                  Variable variable = variables.get(((InvMethod) invocation).getObj());
                  if (variable != null) {
                      ((InvMethod) invocation).setInstance(variable.getType());
                      continue;
                  }
              }

              if (parameters!= null && parameters.get(((InvMethod) invocation).getObj()) != null)
              {
                  Argument arg = parameters.get(((InvMethod) invocation).getObj());
                  ((InvMethod) invocation).setInstance(arg.getType());
                  continue;
              }

              if(fields != null && fields.get(((InvMethod) invocation).getObj()) != null)
              {
                Field field = fields.get(((InvMethod) invocation).getObj());
                  ((InvMethod) invocation).setInstance(field.getType());
                  continue;
              }

              if (((InvMethod) invocation).getObj().equals("this"))
              {
                  ((InvMethod) invocation).setInstance("THIS");
                  continue;
              }

              String[] thisObjet = ((InvMethod) invocation).getObj().split("this.");
              if (thisObjet.length > 1)
              {
                  assert fields != null;
                  Field field = fields.get(thisObjet[1]);
                  if (field != null){
                      ((InvMethod) invocation).setInstance(field.getType());
                  }
                  continue;
              }

           }
       }

    }
    return invocations;
    }
}
