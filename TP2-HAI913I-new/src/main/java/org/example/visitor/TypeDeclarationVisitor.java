package org.example.visitor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.List;


@Getter
@NoArgsConstructor
public class TypeDeclarationVisitor extends ASTVisitor {

    private String name ;
    private String type;
    private List body;
    private FieldDeclaration[] fieldDeclarations;
    private MethodDeclaration[] methodDeclarations;

    public boolean visit(TypeDeclaration node) {
        name=node.getName().getFullyQualifiedName();
        fieldDeclarations = node.getFields();
        methodDeclarations= node.getMethods();

        if (node.isInterface()) {
            type="INTERFACE";
        }
        else {
            type="CLASS";
        }
        return super.visit(node);
    }

}
