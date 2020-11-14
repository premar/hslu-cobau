package ch.hslu.cobau.minij.ast.entity;

import ch.hslu.cobau.minij.ast.AstVisitor;
import ch.hslu.cobau.minij.ast.statement.Block;
import ch.hslu.cobau.minij.ast.statement.Statement;

import java.util.List;
import java.util.Objects;

public class Procedure extends Block {
    private final String identifier;
    private final List<Declaration> declaration;
    private final List<Parameter> parameter;

    public Procedure(String identifier, List<Parameter> parameter, List<Declaration> declaration, List<Statement> statements) {
        super(statements);
        Objects.requireNonNull(identifier);

        this.identifier = identifier;
        this.parameter = parameter;
        this.declaration = declaration;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<Declaration> getFormalDeclaration() {
        return declaration;
    }

    public List<Parameter> getFormalParameter() {
        return parameter;
    }

    public void accept(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }

    @Override
    public void visitChildren(AstVisitor astVisitor) {
        parameter.forEach(parameter -> parameter.accept(astVisitor));
        super.visitChildren(astVisitor); // statements
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Procedure))
            return false;
        if (obj == this)
            return true;
        return this.identifier.equals(((Procedure) obj).identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode() * parameter.hashCode() * declaration.hashCode();
    }
}
