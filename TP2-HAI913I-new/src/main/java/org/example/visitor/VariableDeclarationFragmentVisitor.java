package org.example.visitor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class VariableDeclarationFragmentVisitor extends ASTVisitor {

    List<VariableDeclarationFragment> frags = new ArrayList<VariableDeclarationFragment>();

    public boolean visit(VariableDeclarationFragment node) {
        frags.add(node);
        return super.visit(node);
    }

}
