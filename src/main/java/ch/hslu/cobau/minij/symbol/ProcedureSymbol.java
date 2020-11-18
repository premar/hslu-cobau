package ch.hslu.cobau.minij.symbol;

import ch.hslu.cobau.minij.ast.entity.Declaration;
import ch.hslu.cobau.minij.ast.entity.Parameter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class ProcedureSymbol {

    private final String identifier;
    HashSet<ParameterSymbol> parameters  = new HashSet<ParameterSymbol>();
    HashSet<DeclarationSymbol> declarations  = new HashSet<DeclarationSymbol>();

    public ProcedureSymbol(String identifier) {
        this.identifier = identifier;
    }

    public void addParameter(ParameterSymbol parameter) {
        if(parameters.contains(parameter)) {
            throw new RuntimeException("Parameter was already assigned");
        } else {
            parameters.add(parameter);
        }
    }

    public void addDeclaration(DeclarationSymbol declaration) {
        if(declarations.contains(declaration)) {
            throw new RuntimeException("Declaration was already assigned");
        } else {
            declarations.add(declaration);
        }
    }

    public boolean checkAssignment(String identifier) {
        return declarations.contains(new DeclarationSymbol(identifier, null));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcedureSymbol that = (ProcedureSymbol) o;
        return Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
}