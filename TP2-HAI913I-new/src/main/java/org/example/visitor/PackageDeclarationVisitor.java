package org.example.visitor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;

@Getter
@NoArgsConstructor
public class PackageDeclarationVisitor extends ASTVisitor {
	String packageName = "";

	public boolean visit(PackageDeclaration node) {
		// Extraire le nom du package
		packageName = node.getName().getFullyQualifiedName();
		return super.visit(node);
	}
	public String getPackageName() {
		return packageName;
	}

}
