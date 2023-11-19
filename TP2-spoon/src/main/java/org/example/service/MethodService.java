package org.example.service;

import org.example.model.Classe;
import org.example.model.InvContruct;
import org.example.model.InvMethod;
import org.example.model.Method;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MethodService {
    public HashMap<String, Classe> extract(CtModel model, HashMap<String, Classe> classes)
    {
        List<CtInvocation<?>> invocations = model.getElements(new TypeFilter<>(CtInvocation.class));
        for (CtInvocation<?> invocation : invocations) {
            CtType<?> declaringClass = invocation.getParent(CtClass.class);
            // Vérification si l'invocation est un commentaire
            if (!invocation.getComments().isEmpty()) {
                continue; // Ignorer
            }
            CtExpression<?> target = invocation.getTarget();
            if (target != null) {
                CtTypeReference<?> targetType = target.getType();
                if (targetType != null) {
                    CtType<?> targetTypeElement = targetType.getDeclaration();
                    if (targetTypeElement != null) {
                        CtTypeReference<?> targetClass = targetTypeElement.getReference();
                        if (!targetClass.getQualifiedName().equals(declaringClass.getQualifiedName())) {
                            if (classes.get(declaringClass.getQualifiedName()) == null)
                            {
                                Method method = Method.builder().name("AllInvocation").invocations(new ArrayList<>()).build();
                                InvMethod invMethod = InvMethod.builder().instance(targetClass.getQualifiedName().toString()).build();
                                method.getInvocations().add(invMethod);
                                classes.put(declaringClass.getQualifiedName(), Classe.builder().name(declaringClass.getQualifiedName()).methods(new HashMap<>()).build());
                                classes.get(declaringClass.getQualifiedName()).getMethods().put(method.getName(),method);
                            }else
                            {
                                InvMethod invMethod = InvMethod.builder().instance(targetClass.getQualifiedName().toString()).build();
                                classes.get(declaringClass.getQualifiedName()).getMethods().get("AllInvocation").getInvocations().add(invMethod);
                            }
                        }
                    }
                }
            }
        }
        return classes;
    }

}
