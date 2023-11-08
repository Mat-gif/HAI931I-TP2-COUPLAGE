package org.example.visitor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class EnhancedForStatementVisitor extends ASTVisitor {

    List<EnhancedForStatement> forStatements = new ArrayList<EnhancedForStatement>();// liste des imports

    @Override
    public boolean visit(EnhancedForStatement node)
    {
        forStatements.add(node);
        return super.visit(node);
    }


}
