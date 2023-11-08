package org.example.visitor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ConstructorInvocationVisitor extends ASTVisitor {
	List<ClassInstanceCreation> constructors = new ArrayList<ClassInstanceCreation>();

	public boolean visit(ClassInstanceCreation node) {
		constructors.add(node);
		return super.visit(node);
	}


}
