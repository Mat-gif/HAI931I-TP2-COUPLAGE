package org.example.visitor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ImportDeclaration;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
public class ImportDeclarationVisitor extends ASTVisitor {


    List<ImportDeclaration> imports = new ArrayList<ImportDeclaration>();// liste des imports

    @Override
    public boolean visit(ImportDeclaration node) {
        imports.add(node);
        return super.visit(node);
    }

}
