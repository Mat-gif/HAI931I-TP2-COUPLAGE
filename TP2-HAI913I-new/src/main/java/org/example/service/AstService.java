package org.example.service;

import lombok.NoArgsConstructor;
import org.eclipse.jdt.core.dom.*;
import org.example.model.*;
import org.example.model.Type;
import org.example.visitor.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
public class AstService {


    private List<ImportDeclaration> imports = new ArrayList<ImportDeclaration>();// liste des imports
    private String currentPackage ;
    private String currentClass;

    public Type extractType(CompilationUnit parse) {

        ImportDeclarationVisitor importDeclarationVisitor = new ImportDeclarationVisitor();
        parse.accept(importDeclarationVisitor);
        imports = importDeclarationVisitor.getImports();

        TypeDeclarationVisitor visitorType = new TypeDeclarationVisitor();
        parse.accept(visitorType);
        currentClass = visitorType.getName();

        PackageDeclarationVisitor visitorPackage = new PackageDeclarationVisitor();
        parse.accept(visitorPackage);
        currentPackage = visitorPackage.getPackageName();
        if (visitorType.getName()!=null) {
            return Type.builder()
                    .name(String.format("%s.%s", visitorPackage.getPackageName(), visitorType.getName()))
                    .type(visitorType.getType())
                    .fieldDeclarations(visitorType.getFieldDeclarations())
                    .methodDeclarations(visitorType.getMethodDeclarations())
                    .build();
        }
        return null;
    }





    public Field extractField(FieldDeclaration fieldDeclaration)
    {
        FieldService service = new FieldService();
        String name = service.extractName(fieldDeclaration);
//        System.out.println(name);
//        if (imports.isEmpty()) System.err.println("error");
        String type = service.extractType(fieldDeclaration,  imports);

        if (type==null) return null;

        return Field.builder()
                .name(name)
                .type(type)
                .build();
    }


    public Method extractMethod(MethodDeclaration methodDeclaration, HashMap<String, Field> fields)
    {


        MethodService service = new MethodService();
        String name = String.format("%s.%s.%s",currentPackage,currentClass,service.extractName(methodDeclaration));
//        System.out.println("    ---> "+name);

        HashMap<String, Argument> parameters = service.extractParameters(methodDeclaration,  imports, currentPackage);

//        if (parameters != null)parameters.values().forEach(System.out::println);

        MethodInvocationVisitor methodInvocationVisitor = new MethodInvocationVisitor();
        methodDeclaration.accept(methodInvocationVisitor);

        EnhancedForStatementVisitor enhancedForStatementVisitor = new EnhancedForStatementVisitor();
        methodDeclaration.accept(enhancedForStatementVisitor);

        HashMap<String, Variable> variables= service.extractVar(enhancedForStatementVisitor,imports,currentPackage);

        VariableDeclarationFragmentVisitor variableDeclarationFragmentVisitor = new VariableDeclarationFragmentVisitor();
        methodDeclaration.accept(variableDeclarationFragmentVisitor);

        ArrayList<Inv> invocations= service.extractInvocations(methodInvocationVisitor,variableDeclarationFragmentVisitor, imports,currentPackage);
//        invocations.forEach(System.out::println);

        BindingService  bindingService = new BindingService();
//        System.out.println("***************");
        invocations = bindingService.findSolution(invocations,fields,parameters,imports, variables);
//        invocations.forEach(System.out::println);
        return  Method.builder()
                .name(name)
                .parameters(parameters)
                .invocations(invocations)
                .build();
    }
}
