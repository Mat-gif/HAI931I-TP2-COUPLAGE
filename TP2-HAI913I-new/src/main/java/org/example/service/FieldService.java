package org.example.service;

import lombok.NoArgsConstructor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.example.model.Type;

import java.util.List;

@NoArgsConstructor
public class FieldService {


    public String extractType(FieldDeclaration fieldDeclaration, List<ImportDeclaration> imports, Type typeName, String pac){

        ITypeBinding type = fieldDeclaration.getType().resolveBinding();


        return  extract(type,imports,typeName, pac);
    }


    private String extract(ITypeBinding type, List<ImportDeclaration> imports, Type typeName, String pac)
    {

        if (type.isClass() || type.isInterface())
        {
            if (type.isFromSource())
            {

                for (ImportDeclaration imp : imports) {
                    if (imp.getName().getFullyQualifiedName().contains(String.format(".%s",type.getName()))) {
                        if(typeName.getName().equals("promotions.Etudiant")) System.out.println("///////" + imp.getName().getFullyQualifiedName());


                        return imp.getName().getFullyQualifiedName();
                    }
                }
                return String.format("%s.%s",pac,type.getName());
            }else {
                return  "ExternClass";
            }
        }else  if (type.isPrimitive())
        {
            return  "Primitive";
        }

        return null;
    }


    public String extractName(FieldDeclaration fieldDeclaration){

        String name;
        String[] parts = fieldDeclaration.fragments().get(0).toString().split("=");
        if (parts.length == 2) {
            name = parts[0].trim();
        } else {
            name = fieldDeclaration.fragments().get(0).toString();
        }
        return name;
    }



}
