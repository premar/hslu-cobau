package ch.hslu.cobau.minij.ast.entity;

import ch.hslu.cobau.minij.ast.type.Type;

public class Parameter extends Declaration {
    private final boolean reference;

    public Parameter(String identifier, Type type, boolean reference) {
        super(identifier, type);
        this.reference = reference;
    }
}
