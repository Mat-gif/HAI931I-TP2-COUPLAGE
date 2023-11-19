package org.example.service;

import org.example.model.Classe;
import org.example.model.InvContruct;
import org.example.model.Method;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConstructService {

    public HashMap<String, Classe> extract(CtModel model, HashMap<String, Classe> classes)
    {
        // Pour constructeur
        List<CtConstructorCall<?>> constructorCalls = model.getElements(new TypeFilter<>(CtConstructorCall.class));
        for (CtConstructorCall<?> constructorCall : constructorCalls) {
            CtType<?> declaringClass = constructorCall.getParent(CtClass.class);
            // Vérification si l'invocation est un commentaire
            if (!constructorCall.getComments().isEmpty()) {
                continue; // Ignorer les invocations de méthode qui sont des commentaires
            }
            if (!declaringClass.getQualifiedName().equals(constructorCall.getType().getQualifiedName())) {

                if(constructorCall.getType().getDeclaration()!= null) {
//                    System.out.println("####> " + declaringClass.getQualifiedName() + "---> " + constructorCall.toString() + " \n " + constructorCall.getType().getQualifiedName());
//                    System.out.println("####> " + declaringClass.getQualifiedName() + "---> " +constructorCall.getType().getQualifiedName());

                    if (classes.get(declaringClass.getQualifiedName()) == null)
                    {
                        Method method = Method.builder().name("AllInvocation").invocations(new ArrayList<>()).build();
                        InvContruct invContruct = InvContruct.builder().instance(constructorCall.getType().getQualifiedName().toString()).build();
                        method.getInvocations().add(invContruct);
                        classes.put(declaringClass.getQualifiedName(),Classe.builder().name(declaringClass.getQualifiedName()).methods(new HashMap<>()).build());
                        classes.get(declaringClass.getQualifiedName()).getMethods().put(method.getName(),method);
                    }else
                    {
                        InvContruct invContruct = InvContruct.builder().instance(constructorCall.getType().getQualifiedName().toString()).build();



                        classes.get(declaringClass.getQualifiedName()).getMethods().get("AllInvocation").getInvocations().add(invContruct);



                    }

                }
            }
        }
        return classes;
    }

}
