package org.example.service;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.example.model.Classe;
import org.example.model.Field;
import org.example.model.Method;
import org.example.model.Type;
import org.example.parser.EclipseJDTParser;

import javax.swing.plaf.PanelUI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;


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
//            fields.forEach((k,v) -> System.err.println(v));

            HashMap<String,Method> methods = methodDeclarationExtraction(typeName, fields);


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
            Method method = service.extractMethod(methodDeclaration, fields);
            methods.put(method.getName(),method);
        }
        return methods;
    }


    private    HashMap<String, Field> fieldDeclarationExtraction( Type typeName)
    {
        HashMap<String, Field> fds = new HashMap<>();
        for (FieldDeclaration fieldDeclaration : typeName.getFieldDeclarations())
        {
            Field field = service.extractField(fieldDeclaration);

            // Si des type de base je passe (String, int, Date etc etc ..)
            if (field == null) continue;
            // stock mes attributs
            fds.put(field.getName(),field);
//                System.out.println(fieldName);
        }
        return fds;
    }
}
