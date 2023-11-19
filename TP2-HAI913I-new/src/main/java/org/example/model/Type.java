package org.example.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

@Data
@Getter
@Builder
@ToString
public class Type {
    private String name ;
    private String type ;
    private String theConstruc;
    private FieldDeclaration[] fieldDeclarations;
    private MethodDeclaration[] methodDeclarations;

}
