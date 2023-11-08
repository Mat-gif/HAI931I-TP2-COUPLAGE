package org.example.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

@Data
@Getter
@Builder
public class Type {
    private String name ;
    private String type ;
    private FieldDeclaration[] fieldDeclarations;
    private MethodDeclaration[] methodDeclarations;

    @Override
    public String toString() {
        return "Type{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
