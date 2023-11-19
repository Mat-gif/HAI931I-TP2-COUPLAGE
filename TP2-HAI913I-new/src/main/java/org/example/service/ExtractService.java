package org.example.service;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.example.model.*;
import org.example.parser.EclipseJDTParser;

import javax.swing.plaf.PanelUI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class ExtractService {
    private  final AstService service = new AstService();

    private  HashMap<String, Classe> classes = new HashMap<>();
    public HashMap<String, Classe>  browse (EclipseJDTParser parserEclipse ) throws IOException {
//         je parcours chaque fichier
        for (File content : parserEclipse.listJavaProjectFiles()) {
            parserEclipse.configure();
            CompilationUnit parse = parserEclipse.parse(content);

            HashMap<String, Field> fields = new HashMap<>();

            // Type du fichier
            Type typeName = service.extractType(parse);

            // tour suivant Si ni classe ni interface
            // TODO  a gerer plus tard
            if (typeName == null) continue;

//            System.out.println(typeName);

            // je stocks les attributs de la classe
            fields = fieldDeclarationExtraction(typeName);


            List<Field> recupConstructeur = fields.values().stream()
                    .filter(e -> !e.getType().equals("Primitive") && !e.getType().equals("ExternClass"))
                    .toList();



//            if(typeName.getName().equals("promotions.Etudiant") && !recupConstructeur.isEmpty()){
//                fields.values().forEach(System.out::println);
//            }
//            fields.forEach((k,v) -> System.err.println(v));

            HashMap<String,Method> methods = methodDeclarationExtraction(typeName, fields);

//
            for (Field field : recupConstructeur)
            {
                if(methods.get(String.format("%s.%s",typeName.getName(),typeName.getTheConstruc()))!= null)
                {
                    methods.get(String.format("%s.%s",typeName.getName(),typeName.getTheConstruc())).getInvocations().add( InvContruct.builder().instance(field.getType()).build());
                }
            }


//            System.out.println(" ");
            classes.put(
                    typeName.getName(),
                    Classe.builder()
                            .name(typeName.getName())
                            .methods(methods)
                            .build()
            );
        }


        return classes;
    }



    private  HashMap<String, Method> methodDeclarationExtraction(Type typeName, HashMap<String, Field> fields)
    {
        HashMap<String,Method> methods = new HashMap<>();
        for (MethodDeclaration methodDeclaration : typeName.getMethodDeclarations())
        {
//            if (typeName.getName().equals("promotions.Etudiant")) System.out.println(methodDeclaration.getName());
            Method method = service.extractMethod(methodDeclaration, fields);
//            if (typeName.getName().equals("promotions.Etudiant") && method.getParameters() != null) System.out.println(method.getParameters().keySet().size());
            if(methods.get(method.getName()) != null ) {
//                if (typeName.getName().equals("promotions.Etudiant") ) System.out.println("**//"+method);

                methods.get(method.getName()).getInvocations().addAll(method.getInvocations());
            }
            else {
                methods.put(method.getName(),method);
            }
//            if (typeName.getName().equals("promotions.Etudiant") ) System.out.println(methods);
        }
        return methods;
    }


    private    HashMap<String, Field> fieldDeclarationExtraction( Type typeName)
    {
        HashMap<String, Field> fds = new HashMap<>();
        for (FieldDeclaration fieldDeclaration : typeName.getFieldDeclarations())
        {
            Field field = service.extractField(fieldDeclaration,typeName);

            // Si des type de base je passe (String, int, Date etc etc ..)
            if (field == null) continue;
            // stock mes attributs
            fds.put(field.getName(),field);
        }
        return fds;
    }
}
