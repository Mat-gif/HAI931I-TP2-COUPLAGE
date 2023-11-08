package org.example.service;

import lombok.NoArgsConstructor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import java.util.List;

@NoArgsConstructor
public class FieldService {


    public String extractType(FieldDeclaration fieldDeclaration, List<ImportDeclaration> imports){

        ITypeBinding type = fieldDeclaration.getType().resolveBinding();
        return  extract(type,imports);
    }


    private String extract(ITypeBinding type, List<ImportDeclaration> imports)
    {

        if (type.isClass() || type.isInterface())
        {
            if (type.isFromSource())
            {
                for (ImportDeclaration imp : imports) {
                    if (imp.getName().getFullyQualifiedName().contains(String.format(".%s",type.getName()))) {
                        return imp.getName().getFullyQualifiedName();
                    }
                }
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
