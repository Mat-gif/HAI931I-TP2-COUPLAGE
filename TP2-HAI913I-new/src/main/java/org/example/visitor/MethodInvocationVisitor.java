package org.example.visitor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class MethodInvocationVisitor extends ASTVisitor {
	List<MethodInvocation> methods = new ArrayList<MethodInvocation>();

	public boolean visit(MethodInvocation node) {
		methods.add(node);
		return super.visit(node);
	}

}
