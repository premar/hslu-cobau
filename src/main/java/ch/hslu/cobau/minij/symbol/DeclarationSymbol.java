package ch.hslu.cobau.minij.symbol;

import ch.hslu.cobau.minij.ast.type.Type;

import java.util.Objects;

public class DeclarationSymbol {
    public final String identifier;
    public final Type type;

    public DeclarationSymbol(String identifier, Type type) {
        this.identifier = identifier;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeclarationSymbol that = (DeclarationSymbol) o;
        return Objects.equals(identifier, that.identifier) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, type);
    }
}