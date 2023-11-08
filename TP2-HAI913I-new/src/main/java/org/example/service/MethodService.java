package org.example.service;

import lombok.NoArgsConstructor;
import org.eclipse.jdt.core.dom.*;
import org.example.model.*;
import org.example.visitor.EnhancedForStatementVisitor;
import org.example.visitor.MethodInvocationVisitor;
import org.example.visitor.VariableDeclarationFragmentVisitor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
public class MethodService {
    public String extractName(MethodDeclaration methodDeclaration) {
        return methodDeclaration.getName().getFullyQualifiedName();
    }

    public HashMap<String, Argument> extractParameters(MethodDeclaration methodDeclaration, List<ImportDeclaration> imports, String currentPackage) {
        List<SingleVariableDeclaration> paramaters = methodDeclaration.parameters();
        if (!paramaters.isEmpty())
        {
            HashMap<String, Argument> saveParameters = new HashMap<>();
            for (SingleVariableDeclaration param : paramaters)
            {
                String type = extractType(param,  imports, currentPackage);

                saveParameters.put(param.getName().getFullyQualifiedName(), Argument.builder().name(param.getName().getFullyQualifiedName()).type(type).build());

            }return saveParameters;
        }
         return null;
    }



    public String extractType(SingleVariableDeclaration variable, List<ImportDeclaration> imports, String currentPackage){

        ITypeBinding type = variable.resolveBinding().getType();
        if (type.isClass() || type.isInterface())
        {
                for (ImportDeclaration imp : imports) {
                    if (imp.getName().getFullyQualifiedName().contains(String.format(".%s",type.getName()))) {
                        return imp.getName().getFullyQualifiedName();
                    }
                }
                if (type.isLocal()) {
                    return String.format("%s.%s", currentPackage, type.getName());
                } else {
                    return type.getName();
                }
        }else  if (type.isPrimitive())
        {
            return  "Primitive";
        }

        return null;


    }


    public ArrayList<Inv> extractInvocations(
            MethodInvocationVisitor methodInvocationVisitor,
            VariableDeclarationFragmentVisitor constructorInvocationVisitor,
            List<ImportDeclaration> imports,
            String currentPackage)
    {

        ArrayList<Inv> invovations = new ArrayList<>();

        for (MethodInvocation meth : methodInvocationVisitor.getMethods())
        {
            String name = meth.getName().getFullyQualifiedName();
            String field = null;
            if (meth.resolveMethodBinding() != null && meth.getExpression() != null) {
                field = meth.getExpression().toString();
                invovations.add(InvMethod.builder().method(name).obj(field).build());
            }
        }

        for (VariableDeclarationFragment   cons : constructorInvocationVisitor.getFrags())
        {

            if(cons.getInitializer().resolveTypeBinding() != null ) {
                String name =  cons.getName().getFullyQualifiedName();
                String type = null;
                if(cons.getInitializer() instanceof ClassInstanceCreation) {
                    for (ImportDeclaration imp : imports) {
                        if (imp.getName().getFullyQualifiedName().contains(String.format(".%s", cons.getInitializer().resolveTypeBinding().getName()))) {
                            type = imp.getName().getFullyQualifiedName();
                            break;
                        }
                    }
                    if (type == null) {
                        type = String.format("%s.%s", currentPackage, cons.getInitializer().resolveTypeBinding().getName());
                    }
                    invovations.add(InvContruct.builder().instance(type).name(name).build());
                }
            }

        }

        return  invovations;
    }

    public HashMap<String, Variable> extractVar(EnhancedForStatementVisitor enhancedForStatementVisitor,
                           List<ImportDeclaration> imports,
                           String currentPackage
    ) {
        HashMap<String, Variable> variables = new HashMap<>();
        for ( EnhancedForStatement boucleFor :enhancedForStatementVisitor.getForStatements())
        {
            if (boucleFor == null) continue;
            String name =  boucleFor.getParameter().getName().getFullyQualifiedName();
            String type = null;
//            System.err.println(boucleFor.getParameter().getType() + " :: " + boucleFor.getParameter().getName());


            for (ImportDeclaration imp : imports)
            {
                if (imp.getName().getFullyQualifiedName().contains(String.format(".%s", boucleFor.getParameter().getType()))) {
                    type = imp.getName().getFullyQualifiedName();
                    break;
                }
            }
            if ( type == null )
            {
                type = String.format("%s.%s", currentPackage, boucleFor.getParameter().getType());
            }

            variables.put(name, Variable.builder().name(name).type(type).build());
        }
        return variables;
    }
}
