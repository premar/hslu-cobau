package ch.hslu.cobau.minij.ast.entity;

import ch.hslu.cobau.minij.ast.AstElement;
import ch.hslu.cobau.minij.ast.AstVisitor;
import ch.hslu.cobau.minij.ast.type.Type;

import java.util.Objects;

public class Declaration extends AstElement {
    private final String identifier;
    private final Type type;

    public Declaration(String identifier, Type type) {
        Objects.requireNonNull(identifier);
        Objects.requireNonNull(type);

        this.identifier = identifier;
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Type getType() {
        return type;
    }

    @Override
    public void accept(AstVisitor astVisitor) {
        astVisitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Declaration))
            return false;
        if (obj == this)
            return true;
        return this.identifier.equals(((Declaration) obj).identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode() * type.hashCode();
    }
}
